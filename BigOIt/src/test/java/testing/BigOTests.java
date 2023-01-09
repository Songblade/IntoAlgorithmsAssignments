package testing;

import edu.yu.introtoalgs.*;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class BigOTests {

    // This is my to-do list for the night
    // 1) I need to check for simple illegal inputs
    // 2) I need to research standard deviations and see if that makes my life easier
    // 3) I need to make more tests, to ensure that I still get good responses at higher levels
    // 4) I need to write tests for illegal arguments
    // 5) I need to figure out if there is any other way I can improve my algorithm
    // 6) I need to go to Maariv and take a shower (and pick up my laundry)

    private final BigOItBase tester;

    public BigOTests() {
        tester = new BigOIt();
    }

    @Test
    public void LogNDemoTest() {
        double ratio = tester.doublingRatio("testing.BigOTests$LogN", 60000);
        // I made the timeout 10 minutes, which for logN should never matter
        System.out.println(ratio);
        if (Double.toString(ratio).equals("NaN")) { // why does IntelliJ think this is always false, it isn't
            fail("Didn't get a ratio, should have");
        }
        if (ratio < 0.7 || ratio > 1.3) {
            fail("Ratio is off");
        }
    }

    public static class LogN extends BigOMeasurable {

        private int[] nums;

        @Override
        public void setup(final int n) {
            super.setup(n);
            nums = new int[n];
            for (int i = 0; i < n; i++) {
                nums[i] = i;
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
            Random rand = new Random();
            int target = rand.nextInt(n);
            Arrays.binarySearch(nums, target);
            // this is a log n performance, since I have a sorted array
            // but its length could vary considerably
        }
    }

    @Test
    public void NDemoTest() {
        double ratio = tester.doublingRatio("testing.BigOTests$OofN", 600000);
        // I made the timeout 10 minutes, which should hopefully never matter
        System.out.println(ratio);
        if (Double.toString(ratio).equals("NaN")) { // why does IntelliJ think this is always false, it isn't
            fail("Didn't get a ratio, should have");
        }
        if (ratio < 1.7 || ratio > 2.3) {
            fail("Ratio is off");
        }
    }

    public static class OofN extends BigOMeasurable {

        // I don't need a setup here, because I don't need a pregenerated array to binary search
        // I can just generate it on the fly in the for-loop

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
            Random rand = new Random();
            int target = rand.nextInt(n);
            for (int i = 0; i < n; i++) {
                if (i == target) {
                    return;
                }
            }
            // this is a log n performance, since I have a sorted array
            // but its length could vary considerably
        }
    }

    @Test
    public void N2DemoTest() {
        double ratio = tester.doublingRatio("testing.BigOTests$OofN2", 60000);
        // I made the timeout 10 minutes, which should hopefully never matter
        System.out.println(ratio);
        if (Double.toString(ratio).equals("NaN")) { // why does IntelliJ think this is always false, it isn't
            fail("Didn't get a ratio, should have");
        }
        if (ratio < 3.3 || ratio > 4.7) {
            fail("Ratio is off");
        }
    }

    public static class OofN2 extends BigOMeasurable {

        // I don't need a setup here, because I don't need a pregenerated array to binary search
        // I can just generate it on the fly in the for-loop

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
            int sum = 0;
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    sum += i * j;
                }
            }
            // this is a log n performance, since I have a sorted array
            // but its length could vary considerably
        }
    }
    
    // now I want to make a test that the timeout works
    // Let's start by just doing O(n^2), which right now takes absolutely forever
        // But make a timeout of 1 minute
        // It needs to test that the method is over by the end of the minute
    @Test
    public void timeoutN2Test() {
        assertTimeout(Duration.ofMillis(60000), () -> {
            double ratio = tester.doublingRatio("testing.BigOTests$OofN2", 60000);
            System.out.println(ratio);
        });
        // I made the timeout 10 minutes, which should hopefully never matter
    }

    @Test
    public void N3DemoTest() {
        double ratio = tester.doublingRatio("testing.BigOTests$OofN3", 120000);
        // I made the timeout 10 minutes, which should hopefully never matter
        System.out.println(ratio);
        if (Double.toString(ratio).equals("NaN")) { // why does IntelliJ think this is always false, it isn't
            fail("Didn't get a ratio, should have");
        }
        if (ratio < 7.0 || ratio > 9.0) {
            fail("Ratio is off");
        }
    }

    public static class OofN3 extends BigOMeasurable {

        // I don't need a setup here, because I don't need a pregenerated array to binary search
        // I can just generate it on the fly in the for-loop

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
            int sum = 0;
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    for (int k = 0; k < n; k++) {
                        sum += i * j * k;
                    }
                }
            }
            // this is a log n performance, since I have a sorted array
            // but its length could vary considerably
        }
    }

    // now I want to make a test that the timeout works
    // Let's start by just doing O(n^2), which right now takes absolutely forever
    // But make a timeout of 1 minute
    // It needs to test that the method is over by the end of the minute
    @Test
    public void timeoutN3Test() {
        assertTimeout(Duration.ofMillis(60000), () -> {
            double ratio = tester.doublingRatio("testing.BigOTests$OofN4", 60000);
            System.out.println(ratio);
        });
        // I made the timeout 10 minutes, which should hopefully never matter
    }

    @Test
    public void N4DemoTest() {
        double ratio = tester.doublingRatio("testing.BigOTests$OofN4", 600000);
        // I made the timeout 10 minutes, which should hopefully never matter
        System.out.println(ratio);
        if (Double.toString(ratio).equals("NaN")) { // why does IntelliJ think this is always false, it isn't
            fail("Didn't get a ratio, should have");
        }
        if (ratio < 14.5 || ratio > 17.5) {
            fail("Ratio is off");
        }
    }

    public static class OofN4 extends BigOMeasurable {

        // I don't need a setup here, because I don't need a pregenerated array to binary search
        // I can just generate it on the fly in the for-loop

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
            int sum = 0;
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    for (int k = 0; k < n; k++) {
                        for (int l = 0; l < n; l++) {
                            sum += i * j * k * l;
                        }
                    }
                }
            }
            // this is a log n performance, since I have a sorted array
            // but its length could vary considerably
        }
    }

    // now I want to make a test that the timeout works
    // Let's start by just doing O(n^2), which right now takes absolutely forever
    // But make a timeout of 1 minute
    // It needs to test that the method is over by the end of the minute
    @Test
    public void timeoutN4Test() {
        assertTimeout(Duration.ofMillis(60000), () -> {
            double ratio = tester.doublingRatio("testing.BigOTests$OofN4", 60000);
            System.out.println(ratio);
        });
        // I made the timeout 10 minutes, which should hopefully never matter
    }

    @Test
    public void makingSureNoCCETest() {
        assertThrows(IllegalArgumentException.class, () -> tester.doublingRatio("java.lang.String", 10000)
                );
    }

}
