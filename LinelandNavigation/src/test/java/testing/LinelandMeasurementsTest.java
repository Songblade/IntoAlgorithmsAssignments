package testing;

import edu.yu.introtoalgs.BigOIt;
import edu.yu.introtoalgs.BigOMeasurable;
import edu.yu.introtoalgs.LinelandNavigation;
import org.junit.jupiter.api.Test;

public class LinelandMeasurementsTest {

    // Here, I will make a length 2 mine rotate around the track, so that it can always be jumped over
        // with appropriate backtracking

    // I will measure O(n) of both g and m

    private final BigOIt calc;

    public LinelandMeasurementsTest() {
        calc = new BigOIt();
    }

    @Test
    public void measureGoalO() {
        double ratio = calc.doublingRatio("testing.LinelandMeasurementsTest$GoalMeasurement", 10000);
        System.out.println("Ratio for goal is " + ratio);
    }

    @Test
    public void measureMovementO() {
        double ratio = calc.doublingRatio("testing.LinelandMeasurementsTest$MovementMeasurement", 100000, 1000);
        System.out.println("Ratio for movement is " + ratio);
    }

    public static class GoalMeasurement extends BigOMeasurable {

        private LinelandNavigation runway;

        /**
         * Set up internal data-structures etc as a function of "n": default
         * implementation.  The duration of this method MUST NOT be counted when
         * evaluating algorithm performance.
         *
         * @param n size of implementation
         */
        @Override
        public void setup(int n) {
            super.setup(n);
            runway = new LinelandNavigation(n, 5);
            for (int i = 1; i < n; i+=6) {
                runway.addMinedLineSegment(i, i+1);
            }
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
            runway.solveIt();
            // I want to also test for correctness, but I'm not sure how to calculate what the answer will be
        }
    }

    public static class MovementMeasurement extends BigOMeasurable {

        private LinelandNavigation runway;

        /**
         * Set up internal data-structures etc as a function of "n": default
         * implementation.  The duration of this method MUST NOT be counted when
         * evaluating algorithm performance.
         *
         * @param n size of implementation
         */
        @Override
        public void setup(int n) {
            super.setup(n);
            runway = new LinelandNavigation(10000, n);
            for (int i = 1; i < 10000; i+=n+1) {
                // I don't think these mines will be as impactful
                // minimum n is 100, so I will do the same ratio of size as before
                // there it was 2 for a jump of 5, so here it will also be / 5 * 2, which will be even
                // because I start with a multiple of 5
                runway.addMinedLineSegment(i, i+(n / 5 * 2));
            }
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
            runway.solveIt();
            // I want to also test for correctness, but I'm not sure how to calculate what the answer will be
        }
    }

}
