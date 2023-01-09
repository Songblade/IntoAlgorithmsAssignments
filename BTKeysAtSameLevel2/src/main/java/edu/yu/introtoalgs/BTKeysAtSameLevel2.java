package edu.yu.introtoalgs;

/* Defines the API for the BTKeysAtSameLevel assignment: see the requirements
 * document for more information.
 *
 * Students MAY NOT change the constructor signature.  My test code will only
 * invoke the API defined below.
 *
 * @author Avraham Leff
 */

import java.util.ArrayList;
import java.util.List;

public class BTKeysAtSameLevel2 {

    /** Constructor
     */
    public BTKeysAtSameLevel2() {
        // fill this in!
        // I'm not entirely sure why we would fill this in, given that this class is essentially
        // a library with only a single function in it
        // What do I have to instantiate?
        // I guess you have to run this because it isn't a static method
        // But why isn't it a static method?
    }

    /** Given a String representation of a binary tree whose keys are Integers,
     * computes a List whose elements are List of keys at the same level (or
     * depth) from the root.  The top-level List is ordered by increasing
     * distance from the root so that the first List element consists of the
     * root's key, followed by the keys of all the root's immediate children,
     * etc.  At a given level, the List is ordered from left to right.  See the
     * requirements doc for an example.
     *
     * The String representation of the binary tree is defined as follows.  Keys
     * consist of a single integer value, from 0 to 9.  The string consists only
     * of parentheses and single digit integers: it must begin with an integer
     * (the value of the root node) followed by zero, one or two pairs of
     * parentheses. A pair of parentheses represents a child binary tree with
     * (recursively) the same structure. If a given node only has one child, that
     * child will be the left child node of the parent.
     *
     * Note: the "empty" tree is represented by the empty string, and this method
     * should therefore return an empty List.
     *
     * @param treeInStringRepresentation a binary tree represented in the
     * notation defined above.
     * @return a List whose elements are Lists of the tree's (integer) key
     * values, ordered in increasing distance from the root.  Each List element
     * contains the keys at a given level, ordered from left to right.
     * @throws IllegalArgumentException if the String is null, or doesn't
     * correspond to a valid String representation of a binary tree as defined
     * above.
     */
    public List<List<Integer>> compute(final String treeInStringRepresentation) {
        // When I look at the string:
        // First I check the first number, and add it to the list
        // If there is nothing else, we end
        // Otherwise, we check the open parentheses, going into a recursive method
        // This method first looks at the number, adding it to the list of its depth
        // (creating one if there isn't one already)
        // Then, we check what comes next
        // If there is an open parenthesis, we recurse further, increase the depth
        // If there is a closed parenthesis, we return
        // When we get back to this method, we take the list we have been passing around and return it

        // let's start with some initial filtering, that the string isn't null
        if (treeInStringRepresentation == null) {
            throw new IllegalArgumentException("Tree is null");
        }
        // when I replace with loop, no need to create tree outside loop
        if (treeInStringRepresentation.isBlank()) { // if we have an empty tree
            return new ArrayList<>();
        }
        // now we deal with examining the first number and any subsequent parentheses
        return parseTree(treeInStringRepresentation);
    }

    /**
     * This is my new version of examineNumber that will work iteratively and not recursively
     * This should avoid StackOverflowErrors when it runs out of room on the stack at high levels of input
     * Especially since it is O(n) anyway, might as well
     * @param treeRep tree in String form that must be parsed
     * @return the List of Lists form of the tree
     */
    private List<List<Integer>> parseTree(String treeRep) {

        List<List<Integer>> realTree = new ArrayList<>();
        TreeNode treeLoc = new TreeNode(); // what depth level we are in the tree
        char previousLetter = '('; // This way, it will allow the first number to go through without any
            // exceptions for the root
        char currentLetter; // will be initialized before being used in the loop

        for (int pointer = 0; pointer < treeRep.length(); pointer++) {
            // this is the central loop
            // pointer is what index of the String we are up to, since Strings don't have for-each loops in Java
            // I couldn't call it i or index, because I want to reuse code from my first implementation
            currentLetter = treeRep.charAt(pointer);
            if (currentLetter == '(') {
                treeLoc = evalOpenParen(previousLetter, treeLoc);
            } else if (currentLetter == ')') {
                treeLoc = evalCloseParen(previousLetter, treeLoc);
            } else if (!Character.isDigit(treeRep.charAt(pointer))) {
                // if we don't have a parenthesis now, we must have a digit. This isn't a digit
                throw new IllegalArgumentException(treeRep.charAt(pointer) + " is not a digit");
            } else {
                evalNumber(treeRep, realTree, previousLetter, treeLoc, pointer);
            }
            // either way, we move forward to the next letter
            previousLetter = currentLetter;
        }
        if (treeLoc.depth != 0) { // if we don't get back to 0 at the end, we missed some ')'s
            throw new IllegalArgumentException("Tree ends with open parenthesis");
        }
        return realTree;
    }

    /**
     * Evaluates when the character is '('
     * @param previousLetter that came before '('
     * @param treeLoc where we are in the tree
     * @return this location's child, if there were no exceptions
     */
    private TreeNode evalOpenParen(char previousLetter, TreeNode treeLoc) {
        if (previousLetter == '(') {
            throw new IllegalArgumentException("You can't have two parentheses in a row");
        }
        if (treeLoc.getNumOfChildren() == 2) {
            throw new IllegalArgumentException("Binary trees can't have more than 2 children per node");
        }
        // if that didn't happen
        return treeLoc.goToChild(); // we go to the next child to look at it
    }

    /**
     * Evaluates when the character is ')'
     * @param previousLetter that came before the ')'
     * @param treeLoc where we are on the tree
     * @return this location's parent, if there are no exceptions
     */
    private TreeNode evalCloseParen(char previousLetter, TreeNode treeLoc) {
        if (treeLoc.depth == 0) {
            throw new IllegalArgumentException("Root cannot be in parentheses");
        }
        if (previousLetter == '(') {
            throw new IllegalArgumentException("You can't have an empty node");
        }
        // if that didn't happen
        return treeLoc.parent; // we are going up
    }

    /**
     * Checks a number for bad inputs and then adds it to the tree
     * @param treeRep that we are trying to translate
     * @param realTree that we are trying to build using treeRep
     * @param previousLetter that was before the number
     * @param treeLoc where the number is located on the tree
     * @param index where we currently are in the treeRep
     */
    private void evalNumber(String treeRep, List<List<Integer>> realTree,
                            char previousLetter, TreeNode treeLoc, int index) {
        // if we are still here, we must be dealing with a digit
        if (previousLetter != '(') {
            throw new IllegalArgumentException("All digits must be in nodes. Where's the parenthesis?");
        }
        int key = Character.getNumericValue(treeRep.charAt(index));
        // if there is no list at this depth yet, we add it
        if (realTree.size() == treeLoc.depth) {
            realTree.add(new ArrayList<>());
        }
        realTree.get(treeLoc.depth).add(key); // either way, we add the key to the list at this depth
    }

    /**
     * This class is a data structure intended to help me keep track of how many children I have when
     * going through a loop.
     * Even though it is as recursive as the original implementation, it shouldn't cause the same problem
     * because this takes up much less memory than the call stack
     */
    private static class TreeNode {
        // the parent of the child, null if root to know that you are done
        private TreeNode parent;
        private TreeNode firstChild;
        private TreeNode secondChild;
        // tells you where you are in the tree, so that I don't need to store it separately
        private int depth; // I might want to get rid of this if it causes memory problems

        /**
         * Creates a root Node, to be called directly from the main class
         */
        private TreeNode() {// if root, we want everything null and depth 0
        }

        /**
         * Creates a branch node, should only be called from within this class
         * @param parent that is calling this method
         */
        private TreeNode(TreeNode parent) {
            this.parent = parent;
            this.depth = parent.depth + 1;
        }

        /**
         * Creates a new child for this node
         * @return the new child created by this node, to aid traversal
         */
        private TreeNode goToChild() {
            if (firstChild == null) {
                firstChild = new TreeNode(this);
                return firstChild;
            } else if (secondChild == null){
                secondChild = new TreeNode(this);
                return secondChild;
            }
            throw new IllegalStateException("You tried to go to the next child, but we already have 2 children");
        }

        /**
         * @return the number of children this node has
         */
        private int getNumOfChildren() {
            if (firstChild == null) {
                return 0;
            } else if (secondChild == null) {
                return 1;
            }
            return 2;
        }

    }

}
