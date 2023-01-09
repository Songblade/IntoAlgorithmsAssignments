package edu.yu.introtoalgs;

/** Defines the API for the XenoHematology assignment: see the requirements
 * document for more information.
 *
 * Students MAY NOT change the constructor signature.  My test code will only
 * invoke the API defined below.
 *
 * @author Avraham Leff
 */

public class XenoHematology {

    private final RivalUnionFind population;

    /** Constructor: specifies the size of the xeno population.
     *
     * @param populationSize a non-negative integer specifying the number of
     * aliens in the xeno population.  Members of the population are uniquely
     * identified by an integer 0..populationSize -1.
     */
    public XenoHematology(final int populationSize) {
        if (populationSize < 0) {
            throw new IllegalArgumentException("Population cannot be negative. You gave " + populationSize);
        }
        population = new RivalUnionFind(populationSize);
    } // constructor

    /** Specifies that xeno1 and xeno2 are incompatible.  Once specified
     * as incompatible, the pair can never be specified as being
     * "compatible".  In that case, don't throw an exception, simply
     * treat the method invocation as a "no-op".  A xeno is always
     * compatible with itself, is never incompatible with itself:
     * directives to the contrary should be treated as "no-op"
     * operations.
     *
     * Both parameters must correspond to a member of the population.
     *
     * @param xeno1 non-negative integer that uniquely specifies a member of the
     * xeno population, differs from xeno2
     * @param xeno2 non-negative integer that uniquely specifies a member of the
     * xeno population.
     * @throws IllegalArgumentException if the supplied values are
     * incompatible with the @param Javadoc.
     */
    public void setIncompatible(int xeno1, int xeno2) {
        ensureInPopulation(xeno1);
        ensureInPopulation(xeno2);

        // this sets the xenos as incompatible
        population.setRival(xeno1, xeno2);

    }

    /** Specifies that xeno1 and xeno2 are compatible.  Once specified
     * as compatible, the pair can never be specified as being
     * "incompatible".  In that case, don't throw an exception, simply
     * treat the method invocation as a "no-op".  A xeno is always
     * compatible with itself, is never incompatible with itself:
     * directives to the contrary should be treated as "no-op"
     * operations.
     *
     * Both parameters must correspond to a member of the population.
     *
     * @param xeno1 non-negative integer that uniquely specifies a member of the
     * xeno population.
     * @param xeno2 non-negative integer that uniquely specifies a member of the
     * xeno population
     * @throws IllegalArgumentException if the supplied values are
     * incompatible with the @param Javadoc.
     */
    public void setCompatible(int xeno1, int xeno2) {
        ensureInPopulation(xeno1);
        ensureInPopulation(xeno2);

        // this sets the xenos as compatible
        population.union(xeno1, xeno2);
    }

    private void ensureInPopulation(int xeno) {
        if (xeno < 0) {
            throw new IllegalArgumentException("Xeno ID " + xeno + " cannot be negative.");
        }
        if (xeno >= population.size()) {
            throw new IllegalArgumentException("Xeno ID " + xeno + " is outside population size.");
        }
    }

    /** Returns true iff xeno1 and xeno2 are compatible from a hematology
     * perspective, false otherwise (including if we don't know one way or the
     * other).  Both parameters must correspond to a member of the population.
     *
     * @param xeno1 non-negative integer that uniquely specifies a member of the
     * xeno population, differs from xeno2
     * @param xeno2 non-negative integer that uniquely specifies a member of the
     * xeno population
     * @return true iff compatible, false otherwise
     * @throws IllegalArgumentException if the supplied values are
     * incompatible with the @param Javadoc
     */
    public boolean areCompatible(int xeno1, int xeno2) {
        ensureInPopulation(xeno1);
        ensureInPopulation(xeno2);

        // this checks if they are compatible
        // if they are equal, it will automatically return true, because they must have the same parent
        // even if it is just themselves
        return population.findUnion(xeno1) == population.findUnion(xeno2);
    }

    /** Returns true iff xeno1 and xeno2 are incompatible from a hematology
     * perspective, false otherwise (including if we don't know one way or the
     * other).  Both parameters must correspond to a member of the population.
     *
     * @param xeno1 non-negative integer that uniquely specifies a member of the
     * xeno population, differs from xeno2
     * @param xeno2 non-negative integer that uniquely specifies a member of the
     * xeno population
     * @return true iff compatible, false otherwise
     * @throws IllegalArgumentException if the supplied values are
     * incompatible with the @param Javadoc.
     */
    public boolean areIncompatible(int xeno1, int xeno2) {
        ensureInPopulation(xeno1);
        ensureInPopulation(xeno2);

        // we check if xeno1's rival == xeno2, which returns true if the xenos are incompatible, and false otherwise
        // if xeno1 == xeno2, their rival will never equal themselves, because that violates an invariant
        // if either are -1, it will automatically be false, because xeno2's parent can never be negative
        return population.findRival(xeno1) == population.findUnion(xeno2);
    }

    /**
     * This is a node for my version of the Union Find I am making for this algorithm, which I am calling the
     *  Rival Union Find.
     *
     * This data structure represents groups that can be merged, where each group can also have a
     * rival group with which it is considered opposite. When 2 groups are merged, their rivals
     * are also merged. A group can never be merged with its rival.
     *
     * Each int node (a future implementation could easily be made generic) has a root and a rival
     * The root represents which union the node is part of. The rival represents which union is considered
     * opposite the node's union.
     *
     * All operations of Rival Union Find are O(1), except the constructor which is O(n).
     *
     * inUnion returns true if the 2 elements are in the same union, false otherwise
     * inRivalUnion returns true if the 2 elements are in the same rival union, false otherwise
     * merge(a, b) merges the two unions and their rival unions
     * setRival(a, b) merge's b's union with a's rival union
     * size() returns the size of the RivalUnionFind
     *
     * Public methods are expected to be used outside, private ones are not
     */
    private static class RivalUnionFind {

        // I need the Array of
        private final int[] parent; // the parent of the element at index i
        private final int[] rival; // the rival union of the root at index i, ignore value if not a root
        private final int[] treeSize; // the size of the tree at index i

        /**
         * This sets up the Union Find, and sets the node parents all equal to the union, and the rivals equal to
         * -1, which means that there is no rival yet
         * @param size of the union find
         */
        public RivalUnionFind(int size) {
            parent = new int[size]; // we set each index to itself
            rival = new int[size]; // we set everything to -1, because there are no rivals yet
            treeSize = new int[size]; // no node has a subtree so everything is 1
            for (int i = 0; i < size; i++) {
                parent[i] = i;
                rival[i] = -1;
                treeSize[i] = 1;
            }
        }

        /**
         * @return the size of the Rival Union Find
         */
        public int size() {
            return parent.length;
        }

        /**
         * Merges the unions of a and b
         * @param el1 element found at that index
         * @param el2 element found at that index
         * I would make it return true if successful, false otherwise, but then IntelliJ would
         *            complain I'm not using the return value
         */
        public void union(int el1, int el2) {
            int root1 = findUnion(el1);
            int root2 = findUnion(el2);
            if (root1 == root2 // already unioned, nothing for me to do
                    || root1 == rival[root2]) { // we can't union rivals
                return;
            }

            int biggerNode = merge(root1, root2);
            int smallerNode = root1 == biggerNode? root2 : root1;

            int rival1 = rival[biggerNode];
            int rival2 = rival[smallerNode];
            // we look directly in the array, because their root rivals are the same

            if (rival1 == rival2 || rival2 == -1) { // if they are the same, or the smaller is -1
                // then we don't need to do any sort of rival merging
                return;
            }

            // If both are -1, I can end the method here
            if (rival2 >= 0) {
                // this means that the smaller node has a rival, so we have to worry about the rival being correct
                if (rival1 >= 0) { // if both have rivals, we must merge the rivals
                    rival[biggerNode] = merge(rival1, rival2);
                } else { // if the bigger node doesn't have a rival, we just give it the smaller node's rival
                    rival[biggerNode] = rival2;
                }
                // either way, we updated the bigger node's rival, so we need to tell the rival
                rival[rival[biggerNode]] = biggerNode;
            }
        }

        /**
         * Util: Merges the roots
         * @param root1 valid Rival Union node
         * @param root2 valid Rival Union node != root1
         * @return the new root of the union
         */
        private int merge(int root1, int root2) { // make sure the stuff in the slides was in the textbook
            // making sure that we are setting the smaller root's parent as the bigger root
            if (treeSize[root1] >= treeSize[root2]) {
                int temp = root1 ;
                root1 = root2;
                root2 = temp;
            }

            // now we actually merge the trees
            parent[root1] = root2;
            treeSize[root2] += treeSize[root1];

            return root2;
        }

        /**
         * Merges el2's union with el1's rival union.
         * If el1 doesn't have an rival union, el2's root becomes its rival union
         * While the convention of this method is that we are adding el2's union to el1's rival Union,
         * this is a symmetric operation, and it could just as easily be represented the other way
         * @param el1 param whose rival union we are creating/adding to
         * @param el2 param which we are joining to the rival union
         */
        public void setRival(int el1, int el2) {
            int root1 = findUnion(el1);
            int root2 = findUnion(el2);

            if (root1 == root2) { // we can't set elements in the same set as rivals
                return;
            }

            if (rival[root1] == -1) { // if el1's union doesn't have a rival union yet
                // rival[root1] == -1 -> rival[root2] == -1, because rival unions are connected
                rival[root1] = root2; // el2's union becomes its rival-union
                rival[root2] = root1;
            } else { // if el1's root does have an rival-union
                union(rival[root1], root2); // we merge el1's rival union with el2's union
                // this method should update all rivals appropriately
            }
        }

        /**
         * Returns the root node of i's union, which could be i
         * @param i node we are looking for
         * @return the root of i's union
         */
        public int findUnion(int i) {
            while (i != parent[i]) {
                parent[i] = parent[parent[i]]; // I took this line from the slides to get the O(1) efficiency
                    // it promises
                i = parent[i];
            }
            return i;
        }

        /**
         * Returns the root node of i's rival union, or -1 if it doesn't have a rival union
         * @param i node whose rival union we are looking for
         * @return the root of i's rival union, -1 if it doesn't have one
         */
        public int findRival(int i) {
            return rival[findUnion(i)];
        }


    }


} // XenoHematology
