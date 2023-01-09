package edu.yu.introtoalgs;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;

/** Defines the API for the WealthTransfer assignment: see the requirements
 * document for more information.
 *
 * Students MAY NOT change the constructor signature.  My test code will only
 * invoke the API defined below.
 *
 * @author Avraham Leff
 */

public class WealthTransfer {

    /*
    * I deleted most of my notes from before then, because it is about an earlier implementation that I
    * rejected as too complicated and less efficient.
    * Having started the assignment, I have thought of two problems.
    * First, this is getting really complicated.
    * Second, I'm not sure that the efficiency is actually better. And even if it is, I don't think that I know
    * how to actually prove that.
    *
    * Also, I realized something else. Even if I have a linked-list problem, insertion is still O(1), because I would
    * have an array implementation of the tree. So I could just go directly to 'to' and change its parent to 'from'.
    *
    * If I change my implementation to this, it would have an O(N) time for solveIt() and O(1) for everything else.
    * So it would have amortized O(N) for the entire project. Let's figure out exactly what that means. solveIt()
    * starts at the parent and then goes down to its children. Wait, that could be a problem.
    *
    * In order to get solveIt() to work, each parent would need to know about each of its children. Well, that's simple.
    * I could store a LinkedList - or better yet, a HashSet, because that could help with error-detection - of each
    * child of a parent. I wouldn't even need to store ancestry, because I would start at 1, recurse down to the base
    * of the tree, and then get back up to 1, divide-and-conquer style. I could even do this with ForkJoin. You know
    * what, let's do this with ForkJoin. Why not? It will get me more used to using ForkJoin.
    *
    * So, I would have an array of HashSet<Integer> for each node, where the set would store the children of the node.
    * I would have an int representing what percentage of the parent this child is (because only needed on the way up),
    * where I could then determine the minimum amount needed using it.
    *
    * In fact, on the way up, rather than returning what this child needs, it would return what the most is that
    * this parent could need, given this child and this percentage. The parent will then see if this is more than
    * what it currently is asking for, and if it is, replaces it. Continue until each child has been checked. Then,
    * pass up this total to its parent. That would work.
    *
    * I would also need a boolean array for each node whether we need to square-root it when passing it up. If we are
    * doing so, first we square-root it, then we do the math for the minimum using it.
    *
    * I would also need an int array that holds the amount of wealth each root node requires.
    *
    * I think I will keep a population int variable to deal with off-by-one issues.
    * */

    private final int population; // to deal with off-by-one issues
    private final Set<Integer>[] children; // contains the index of the children of the node at this index
    private final int[] percentageOfParent; // contains the percentage the child is of its parents donations
    private final boolean[] isSquared; // contains if this child received a square donation
    private final int[] wealthRequired; // contains the amount of wealth required by the leaf
    private final int[] percentageDonated; // contains the percentage of the branch's wealth that has been noted
        // if any parent doesn't reach 100% or goes above it, it causes errors
    // of these, children is only available for those who are donating and is otherwise null
    // while wealthRequired is only available for leaf nodes who are the final recipients, and is otherwise 0

    /** Constructor: specifies the size of the population.
     *
     * @param populationSize a positive integer specifying the number of people
     * in the population.  Members of the population are uniquely identified by
     * an integer 1...populationSize.  Initial wealth transfer must be initiated
     * by the person with id of "1".
     */
    public WealthTransfer(final int populationSize) {
        if (populationSize < 2) {
            throw new IllegalArgumentException("Population must be at least 2, received " + populationSize);
        }
        population = populationSize;
        children = (Set<Integer>[]) new Set[population + 1]; // unfortunately, the way arrays and generics work
            // mean that there is no good way around this
            // but there is no way this can fail, because the array is literally empty
        percentageOfParent = new int[population + 1];
        isSquared = new boolean[population + 1];
        wealthRequired = new int[population + 1];
        percentageDonated = new int[population + 1];
    } // constructor

    /** Specifies that one person want to make a wealth transfer to another
     * person.
     *
     * @param from specifies who is doing the wealth transfer, must correspond to
     * a valid population id
     * @param to specifies who is receiving the wealth transfer, must correspond
     * to a valid population id, and can't be identical to "from"
     * @param percentage the percentage of "from"'s wealth that will be
     * transferred to "to": must be an integer between 1...100
     * @param isWealthSquared if true, the wealth received is the square of the
     * money transferred
     * @throws IllegalArgumentException if the parameter Javadoc specifications
     * aren't satisfied or if this "from" has previously specified a wealth
     * transfer to this "to".
     */
    public void intendToTransferWealth(final int from, final int to,
                                       final int percentage,
                                       final boolean isWealthSquared) {
        cleanWealthTransfer(from, to, percentage);

        if (children[from] == null) children[from] = new HashSet<>();
        children[from].add(to);
        percentageDonated[from] += percentage;
        percentageOfParent[to] = percentage;
        isSquared[to] = isWealthSquared;

    }

    private void cleanWealthTransfer(final int from, final int to, final int percentage) {
        cleanID(from);
        cleanID(to);
        if (from == to) {
            throw new IllegalArgumentException("Cannot transfer money within a single ID");
        }
        if (percentage < 1 || percentage > 100) {
            throw new IllegalArgumentException("Invalid percentage: " + percentage);
        }
        if (children[to] != null && children[to].contains(from)) {
            throw new IllegalArgumentException("Circular inheritance: #" + from + " is a child of #" + to);
        }
        if (percentageOfParent[to] != 0) { // if the child is already inheriting from someone else
            throw new IllegalArgumentException("Dual inheritance: #" + to + " is already receiving money");
        }
        int totalPercentage = percentageDonated[from] + percentage;
        if (totalPercentage > 100) {
            throw new IllegalArgumentException("#" + from + " is donating " + totalPercentage + "% of money");
        }
        if (wealthRequired[from] != 0) {
            throw new IllegalArgumentException("Heir #" + from + " is receiving $" + wealthRequired[from] + " so cannot also donate");
        }
    }

    private void cleanID(final int id) {
        if (id < 1) {
            throw new IllegalArgumentException("All IDs must be at least 1, this ID is #" + id);
        }
        if (id > population) {
            throw new IllegalArgumentException("ID #" + id + " is outside of population " + (population - 1));
        }
    }

    /** Specifies the wealth that the person must have in order for the overall
     * wealth transfer problem to be considered solved.
     *
     * @param id must correspond to a member of the population from 2...populationSize
     * @param wealth the wealth that the designated person must have as a result
     * of wealth transfers, must be positive.
     * @throws IllegalArgumentException if parameter Javadoc specifications aren't
     * met.
     */
    public void setRequiredWealth(final int id, final int wealth) {
        cleanRequiredWealth(id, wealth);
        wealthRequired[id] = wealth;
    }

    private void cleanRequiredWealth(final int id, final int wealth) {
        cleanID(id);
        if (id == 1) {
            throw new IllegalArgumentException("Cannot donate to ID #1");
        }
        if (wealth < 1) {
            throw new IllegalArgumentException("Must give positive wealth, you requested: " + wealth);
        }
        if (wealthRequired[id] != 0) {
            throw new IllegalArgumentException("Wealth Change: " + id + " already requested $" + wealthRequired[id]);
        }
        if (children[id] != null) {
            throw new IllegalArgumentException("Parent #" + id + " has children so cannot donate");
        }
    }

    /** Solves the wealth transfer problem by determining the MINIMAL amount of
     * wealth that "person with id of 1" must transfer such that all members of
     * the population receive the wealth that they have been promised.
     *
     * The amount of wealth that a person has been promised is specified by
     * invocations of setRequiredWealth().  The amount of wealth that a person
     * actually receives is specified by invocations of intendToTransferWealth().
     * The "person with id of 1" initiates all wealth transfers between members
     * of the population.  This method returns the minimum amount of that
     * initiating wealth transfer that will satisfy the remaining population's
     * needs.
     *
     * NOTE: at the time that this method is invoked, all persons transferring
     * wealth MUST be on record as intending to transfer 100 percent of their
     * wealth.  If this pre-condition doesn't hold, the implementation MUST throw
     * an IllegalStateException in lieu of solving the problem.
     *
     * @return the minimum amount transferred by person with id #1: must be
     * accurate to four digits after the decimal point.
     */
    public double solveIt() {
        cleanSolveIt();
        percentageOfParent[1] = 100; // to help the return of the recursive method

        // now, we recursively, starting at 1, go through each child, find the most amount of money
        // needed, and pass it back up
        // this will make a non-recursive implementation that uses a stack
        class Frame {
            final int id;
            double wealth;
            Iterator<Integer> children;

            Frame(int id) {
                this.id = id;
            }
        }

        Stack<Frame> idStack = new Stack<>();
        idStack.push(new Frame(1));
        double wealth = 0;
        //int loopNum = 0;
        while (!idStack.isEmpty()) {
            //loopNum++;
            Frame heir = idStack.pop();
            if (heir.children == null) {
                // meaning that we haven't yet checked if it has wealth or children
                if (wealthRequired[heir.id] > 0) {
                    // if this is a child, we need to return its money
                    wealth = getParentDonation(heir.id, wealthRequired[heir.id]);
                } else { // meaning we have just discovered a parent, and need to start checking
                    // its children
                    heir.children = children[heir.id].iterator();
                    Frame child = new Frame(heir.children.next());
                    idStack.push(heir);
                    idStack.push(child);
                }
            } else { // this means that we have at least one child
                // and just calculated the child's net worth
                // first we check if the latest change to wealth changes this heir's wealth
                if (wealth > heir.wealth) {
                    heir.wealth = wealth;
                }
                // now we check if we have more children
                // if we do, we take another child out, and put this and the child on the stack
                // if we don't, we throw wealth back into the upper variable and don't put this
                // on the stack
                if (heir.children.hasNext()) {
                    Frame child = new Frame(heir.children.next());
                    idStack.push(heir);
                    idStack.push(child);
                } else {
                    wealth = getParentDonation(heir.id, heir.wealth);
                }
            }
        }
        //System.out.println(loopNum);
        return wealth;
    }

    private void cleanSolveIt() {
        if (percentageDonated[1] < 100) {
            throw new IllegalStateException("ID #1 must donate all money, but instead donated " + percentageDonated[1] + "%");
        }
        // in order to make sure that there are no children who ask but don't receive,
        // we will have to check the entire array to make sure of it, because checking recursively
        // via the tree will never find these people
        // while we are at it, we can also check if anyone has donated but less than 100%
        for (int id = 2; id <= population; id++) {
            if (wealthRequired[id] > 0 && percentageOfParent[id] == 0) {
                // if we have someone who wants money but was never given any
                throw new IllegalStateException("#" + id + " wants $" + wealthRequired[id] + " but no one donated to him");
            } else if (percentageDonated[id] > 0 && percentageDonated[id] < 100) {
                // if we have someone that only gave some of their money
                throw new IllegalStateException("#" + id + " only donated " + percentageDonated[id] + " of their money");
            } else if (percentageDonated[id] == 100 && percentageOfParent[id] == 0) {
                // if we have someone who donates money but never received any
                // the exception is 1, but this is checking 2+
                throw new IllegalStateException("#" + id + " is donating money but doesn't get any to donate.");
            }
        }
    }

    /* I know this is commented out code, I left it so that it could be clear what my original,
    recursive plan was. I changed it because I wanted to be able to handle stack overflows.
    It was originally going to be called on 1 in solveIt().

    private double getMoneyNeededBy(int id) {

        // base case: if id has wealth
        if (wealthRequired[id] > 0) {
            // we return the fractional amount of money that the parent would need if she is giving
            // this much to each percentage
            // or if she is giving the square-root to each percentage if we are using tax evasion
            return getParentDonation(id, wealthRequired[id]);
        } else { // if id is donating to children
            // then we need to calculate how much money we need for each child
            double moneyNeeded = 0;
            for (int child : children[id]) {
                double moneyNeededForChild = getMoneyNeededBy(child);
                if (moneyNeededForChild > moneyNeeded) {
                    moneyNeeded = moneyNeededForChild;
                    // if the amount of money needed from this child's percentage is greater than
                    // what we thought we needed before, we change what we need
                }
            }
            return getParentDonation(id, moneyNeeded);
        }
    }//*/

    private double getParentDonation(int id, double money) {
        // we return the fractional amount of money that the parent would need if she is giving
        // this much to each percentage
        // or if she is giving the square-root to each percentage if we are using tax evasion
        if (isSquared[id]) {
            return 100 * Math.sqrt(money) / percentageOfParent[id];
        } else {
            return 100 * money / percentageOfParent[id];
        }
    }
} // class
