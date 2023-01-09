package testing;

import edu.yu.introtoalgs.BTKeysAtSameLevel2;
import edu.yu.introtoalgs.BigOIt;
import edu.yu.introtoalgs.BigOMeasurable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EfficiencyTest {
    // only run this from the command line

    private final BigOIt tester;

    public EfficiencyTest() {
        tester = new BigOIt();
    }

    @Test
    public void skinnyCaseTest() {
        double dRatio = tester.doublingRatio("testing.EfficiencyTest$SkinnyTreeMeasurable", 10000);
        System.out.println(dRatio);
        if (dRatio < 1.8 || dRatio > 2.2) {
            Assertions.fail("dRatio " + dRatio + " is not linear");
        }
    }

    @Test
    public void fatCaseTest() {
        double dRatio = tester.doublingRatio("testing.EfficiencyTest$FatTreeMeasurable", 10000);
        System.out.println(dRatio);
        if (dRatio < 1.8 || dRatio > 2.2) {
            Assertions.fail("dRatio " + dRatio + " is not linear");
        }
    }

    public static class SkinnyTreeMeasurable extends BigOMeasurable {

        private final BTKeysAtSameLevel2 treeReader;
        private String input;

        public SkinnyTreeMeasurable() {
            treeReader = new BTKeysAtSameLevel2();
        }

        // this will be testing
        @Override
        public void setup(int n) {
            super.setup(n);
            StringBuilder strRepBuilder = new StringBuilder(n);
            strRepBuilder.append(0);
            for (int i = 0; i < n - 1; i++) {
                strRepBuilder.append('(');
                strRepBuilder.append(i % 10);
            }
            strRepBuilder.append(")".repeat(n - 1)); // to close all the parentheses
            input = strRepBuilder.toString();
        }

        /**
         * Performs a single execution of an algorithm: MAY ONLY be invoked after
         * setup() has previously been invoked.  The algorithm must scale as a
         * function of the parameter "n" supplied to setup().
         * <p>
         * NOTE: ONLY the duration of this method should be considered when
         * evaluating algorithm performance.
         */
        @Override
        public void execute() {
            treeReader.compute(input);
        }
    }

    public static class FatTreeMeasurable extends BigOMeasurable {

        private final BTKeysAtSameLevel2 treeReader;
        private String input;

        public FatTreeMeasurable() {
            treeReader = new BTKeysAtSameLevel2();
        }

        // this will be testing
        @Override
        public void setup(int n) {
            super.setup(n);

            // now I need to figure out how to set up a balanced tree
            // I think I am going to have to do it recursively
            // So, I keep going until I reach a depth of log2(n)
            // Since at n==1, log2(1)==0, at n==2, log2(2)==1, so have levels 0 and 1
            // At log2(3), <2&&>1, but I need 2 levels still, so I would have to truncate it, so it would
            // be considered <=1
            // At each level, we first check if this is within the depth
            // If it isn't, we immediately return
            // If it is, we add a "(" (will do depth 0 separately), add a key, and recurse down to both
                // children
            // When we are done with the children, we put a ")" on the String before returning up

            // We will need to store a max count for the bottom row, which is n - (int) 2^log(n)
            // Whenever we add something to that depth, we increase a count by 1
            // If the two numbers are equal, we have maxed out the bottom row, and no longer add to it
            // We will return the current count to keep track of it
            // I think I am ready to actually build this now
            StringBuilder strRepBuilder = new StringBuilder(n); // will be bigger, but whatever

            // calculating the maximum depth in the tree
            int maxDepth = (int) logBase2(n); // should get me log base 2
            // calculating the number of elements at the bottom
            int maxBottomCount = n - (int) Math.pow(2, logBase2(n));
            // I really hope I calculated that correctly

            strRepBuilder.append(0); // this is the first one, not in parentheses
            int bottomCount = recursiveBuild(strRepBuilder, maxDepth, 1, maxBottomCount, 0); // for left child
            recursiveBuild(strRepBuilder, maxDepth, 1, maxBottomCount, bottomCount); // for right child
            input = strRepBuilder.toString();

        }

        private double logBase2(int n) {
            return Math.log(n) / Math.log(2);
        }

        private int recursiveBuild(StringBuilder builder, int maxDepth, int currentDepth, int maxBottomCount, int currentBottomCount) {
            // At each level, we first check if this is within the depth
            // If it isn't, we immediately return
            if (currentDepth > maxDepth || // remember, we want to be <= max depth
                    (currentDepth == maxDepth && maxBottomCount == currentBottomCount)) {
                    // if we are at the bottom row, but it is full
                return currentBottomCount;
            }

            // If it is, we add a '(' (will do depth 0 separately), add a key, and recurse down to both
                // children
            builder.append('(');
            builder.append(currentDepth % 10); // this will be a valid digit, and means not always the same one
            if (currentDepth == maxDepth) {
                currentBottomCount++; // because we just added an element to the bottom row
            } else { // if this isn't the bottom row, we need to recurse more
                currentBottomCount = recursiveBuild(builder, maxDepth, currentDepth + 1, maxBottomCount, currentBottomCount);
                currentBottomCount = recursiveBuild(builder, maxDepth, currentDepth + 1, maxBottomCount, currentBottomCount);
                // we do it twice, once for the left child, once for the right
            }

            // When we are done with the children, we put a ')' on the String before returning up
            builder.append(')');
            return currentBottomCount;
        }

        /**
         * Performs a single execution of an algorithm: MAY ONLY be invoked after
         * setup() has previously been invoked.  The algorithm must scale as a
         * function of the parameter "n" supplied to setup().
         * <p>
         * NOTE: ONLY the duration of this method should be considered when
         * evaluating algorithm performance.
         */
        @Override
        public void execute() {
            treeReader.compute(input);
        }
    }

}
