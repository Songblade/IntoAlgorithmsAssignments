package edu.yu.introtoalgs;

import java.util.LinkedList;
import java.util.Queue;

/** Defines the API for the LinelandNavigation assignment: see the requirements
 * document for more information.
 *
 * Students MAY NOT change the constructor signature.  My test code will only
 * invoke the API defined below.
 *
 * @author Avraham Leff
 */

public class LinelandNavigation {

    /** Even though Lineland extends forward (and backwards) infinitely, for
     * purposes of this problem, the navigation goal cannot exceed this value
     */
    public final static int MAX_FORWARD = 1_000_000;

    private final int goal;
    private final int speed;
    private final boolean[] marked;

    /** Constructor.  When the constructor completes successfully, the navigator
     * is positioned at position 0.
     *
     * @param g a positive value indicating the minimum valued position for a
     * successful navigation (a successful navigation can result in a position
     * that is greater than g).  The value of this parameter ranges from 1 to
     * MAX_FORWARD (inclusive).
     * @param m a positive integer indicating the exact number of positions that
     * must always be taken in a forward move. The value of this parameter cannot
     * exceed MAX_FORWARD.
     * @throws IllegalArgumentException if the parameter values violate the
     * specified semantics.
     */
    public LinelandNavigation(final int g, final int m) {
        cleanConstructor(g, m);
        // fill me in!
        // this should create our base array that we are navigating
        // it is a boolean array, with a length of g + m
        // false means it is not mined/visited, while true means it is
        goal = g;
        speed = m;
        marked = new boolean[g + m];
    }

    private void cleanConstructor(final int goal, final int speed) {
        cleanLocation("g", goal);
        cleanLocation("m", speed);
    }

    private void cleanLocation(final String name, final int distance) {
        if (distance > MAX_FORWARD) {
            throw new IllegalArgumentException(name + " " + distance + " > MAX_FORWARD (" + MAX_FORWARD +")");
        }
        if (distance <= 0) {
            throw new IllegalArgumentException(name + " " + distance + " must be positive, is " + distance);
        }
    }

    /** Adds a mined line segment to Lineland (an optional operation).
     *
     * NOTE: to simplify computation, assume that no two mined line segments can
     * overlap with one another, even at their end-points.  You need not test for
     * this (although if it's easy to do so, consider sprinkling asserts in your
     * code).
     *
     * @param start a positive integer representing the start (inclusive)
     * position of the line segment
     * @param end a positive integer representing the end (inclusive) position of
     * the line segment, must be greater or equal to start, and less than
     * MAX_FORWARD.
     */
    public void addMinedLineSegment(final int start, final int end) {
        // fill me in!
        cleanAddMine(start, end);
        // now we mark the spots on the boolean array where the mine is
        for (int location = start; location <= end && location < marked.length; location++) {
            // if this would be going over the array, we just ignore the mine
            // Even though it runs over, since it is less than the end of the world, it is legal
            assert !marked[location]; // because it is trivial to check, he told us to
            marked[location] = true;
        }
    }

    private void cleanAddMine(final int start, final int end) {
        cleanLocation("start", start);
        cleanLocation("end", end);
        if (start > end) {
            throw new IllegalArgumentException("Start " + start + " must be < end " + end);
        }
    }


    /** Determines the minimum number of moves needed to navigate from position 0
     * to position g or greater, where forward navigation must exactly m
     * positions at a time and backwards navigation can be any number of
     * positions.
     *
     * @return the minimum number of navigation moves necessary, or "0" if no
     * navigation is possible under the specified constraints.
     */
    public final int solveIt() {
        // now we actually implement solveIt()
        // it is just BFS, except we have to make up the tree as we go

        Queue<Integer> queue = new LinkedList<>();
        int[] depth = new int[marked.length]; // stores the depth of the node, number of jumps from the start
        marked[0] = true;
        queue.add(0);
        while (!queue.isEmpty()) {
            int location = queue.remove();
            if (location >= goal) { // if we have reached the goal or later
                return depth[location];
            }
            // if this is not at the goal yet, we add its jumps to the stack
            // if they aren't marked
            // which means, only if we haven't been there and there are no mines there
            if (!marked[location + speed]) {
                // we don't have to check if the forward jump is within the area we care about
                // because if it isn't, we have already reached the goal
                // there is no problem of the place having been visited, because it is ahead of us
                // But it will be marked if there is a mine there
                queue.add(location + speed);
                depth[location + speed] = depth[location] + 1;
                marked[location + speed] = true;
                // we add the location to the queue and set its depth
            }
            // now we add all the locations reachable by jumping backwards, that aren't
            // reachable from g-m
            for (int newLoc = location - 1; //newLoc > location - speed &&
                    newLoc > 0
                    && depth[newLoc] == 0; newLoc--) {
                // we start at right before here, and then go backwards until we reach a node that we
                // have already given a depth to, in which case nothing before here is relevant
                // I moved the newLoc > 0 test to the for loop, because once it is true for one node,
                // it is also true for subsequent nodes
                if (!marked[newLoc]) {
                    // we aren't allowed to jump to before the beginning of the array
                    // if this isn't a mine
                    queue.add(newLoc);
                    depth[newLoc] = depth[location] + 1;
                    marked[newLoc] = true;
                }
            }
        }
        return 0; // means we can't find a way to the goal
    }
} // LinelandNavigation

/*
* Now it is time to figure out the solution to the problem
* I have a feeling that it has to do with graph traversals, since Leff kind of said so in class
* We could make the nodes equal the positions, and the edges equal to the potential moves
* Theoretically, each node has a number of edges equal to the number of nodes behind it + 1
* Node k has k edges
* That is a quadratic number of edges to even set up, to then get rid of via mines
* So perhaps we only form the graph in solveIt
* But I could easily make a problem where there are no mines but a massive graph, so that won't work
* Instead, I probably want to generate a graph on the fly, based on my current position
* At each point, if I can jump forward, I want to, but it could lead me to a dead end
* My question is, do I really need edges all the way back to the start?
* If DePoint is on point k, then I can be certain that he jumped from k - m
* So, for any point k - m < p < k, I need to jump to k in order to jump back to that point
* But, any point p < k-m, it would be more efficient to instead jump there directly from k - m
* So, the maximum number of edges a node could have is actually m (m-1 backwards, 1 forwards)
* Still, if I generate this graph in advance, it's going to be complicated
* Plus, when I add mines...that might not be the problem
* So instead, I need to make the graph on the fly, by checking each backwards link to make sure there isn't a mine there
* I want a more efficient implementation of BFS, but linear is rather hard to beat, and I'm not sure it is possible
* So, let's just build a BFS
* Mines will be implemented as updating an array and adding the values of the mine as already visited
* After all, why distinguish between mines and spots we have already visited? There's no point, they are treated the same
* I will need a length of the array equal to g + m, because if we need to jump past the goal, we will be able to
* jump from g - 1 by m, landing us at g + m - 1
* But the length is 1 greater than the final slot, so g + m
* Okay, let's start making it
*
* Let's just figure out what the efficiency is
* The number of vertices equals g + m
* The number of edges equals (g+m)*m or gm + m^2 in the worst case
* If m << g, that's not a problem, but for big ms, that could be a problem
* He might not care about that, but he might
* Let's first try an implementation based on BFS stam, then let's fix its inefficiencies
* And I should do Big(O) tests both based on g and m
* And hope my code doesn't try n > 1,000,000
* */