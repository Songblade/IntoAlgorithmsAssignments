package testing;

import edu.yu.introtoalgs.BigOIt;
import edu.yu.introtoalgs.BigOItBase;
import edu.yu.introtoalgs.BigOMeasurable;
import edu.yu.introtoalgs.HydratedHakofos;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HakofosTest {

    private final HydratedHakofos hakofos;
    private final int[] waterRequired;
    private final int[] unevenRequired;

    // I need to set up some tests
    // It looks like I don't need to test for bad input. I might do so anyway, but I won't test those tests
    public HakofosTest() {
        hakofos = new HydratedHakofos();
        waterRequired = new int[10];
        for (int i = 0; i < 10; i++) {
            waterRequired[i] = 1;
        }

        unevenRequired = new int[10];
        for (int i = 0; i < 10; i++) {
            unevenRequired[i] = i + 1;
        } // the total amount of water required equals 55
    }

    // testing that if the amount of water is less than the dehydration, it returns -1
    @Test
    public void dehydratedTest() {
        // I test with no water and then insufficient water, lumped and spread
        int[] waterGiven = new int[10];
        assertEquals(-1, hakofos.doIt(waterGiven, waterRequired));

        waterGiven[0] = 9;
        assertEquals(-1, hakofos.doIt(waterGiven, waterRequired));

        for (int i = 0; i < 9; i++) {
            waterGiven[i] = 1;
        }
        assertEquals(-1, hakofos.doIt(waterGiven, waterRequired));
    }

    // testing that if the first table is enough to get you through, that is returned
    @Test
    public void firstTableGoodTest() {
        // I test with both good and better
        int[] waterGiven = new int[10];
        waterGiven[0] = 10;
        assertEquals(1, hakofos.doIt(waterGiven, waterRequired));

        waterGiven[0] = 100;
        assertEquals(1, hakofos.doIt(waterGiven, waterRequired));
    }

    // same as above, but for a later table
    @Test
    public void laterTableGoodTest() {
        // I test with both good and better
        int[] waterGiven = new int[10];
        waterGiven[5] = 10;
        assertEquals(6, hakofos.doIt(waterGiven, waterRequired));

        waterGiven[5] = 100;
        assertEquals(6, hakofos.doIt(waterGiven, waterRequired));
    }

    // same as above, but it is in combination with a later table
    @Test
    public void tableAddingGoodTest() {
        // I test with both good and better
        int[] waterGiven = new int[10];
        waterGiven[0] = 6;
        waterGiven[1] = 4;
        assertEquals(1, hakofos.doIt(waterGiven, waterRequired));
    }

    // same as above, but you have to loop back to the beginning of the array to get the second table
    @Test
    public void tableAddingGoodOverflowTest() {
        // I test with both good and better
        int[] waterGiven = new int[10];
        waterGiven[9] = 6;
        waterGiven[0] = 4;
        assertEquals(10, hakofos.doIt(waterGiven, waterRequired));
    }

    // tests that when multiple tables would work, the first is returned
    @Test
    public void firstGoodTableReturnedTest() {
        // I test with both good and better
        int[] waterGiven = new int[10];
        waterGiven[0] = 6;
        waterGiven[6] = 4;
        assertEquals(1, hakofos.doIt(waterGiven, waterRequired));
    }

    // tests that it works even with a different waterRequired of uneven length when lump sum
    @Test
    public void unevenWalkLumpSumTest() {
        // I test with both good and better
        int[] waterGiven = new int[10];
        waterGiven[0] = 55;
        assertEquals(1, hakofos.doIt(waterGiven, unevenRequired));

        waterGiven[0] = 0;
        waterGiven[5] = 55;
        assertEquals(6, hakofos.doIt(waterGiven, unevenRequired));
    }

    // tests that it works when each gets you to the next, and no more, with uneven
    @Test
    public void unevenSpreadOutTest() {
        // I test with both good and better
        int[] waterGiven = new int[10];
        for (int i = 0; i < 10; i++) {
            waterGiven[i] = i + 1;
        }
        assertEquals(1, hakofos.doIt(waterGiven, unevenRequired));
    }

    @Test
    public void massiveInputTest() {
        int numOfTables = (int) Math.pow(2, 26);
        int[] largeWaterNeeded = new int[numOfTables];
        Arrays.fill(largeWaterNeeded, 1);

        int[] waterGiven = new int[numOfTables];
        waterGiven[0] = 1;
        for (int i = 0; i < numOfTables - 2; i+=2) { // adding a drink to every other place, to make
            // calculations potentially more complicated
            // we aren't going the whole way, because I am worried about a potential result where
            // the extra table means you could actually start one table earlier
            waterGiven[i] = 1;
        }
        // if this were Python, I would try to do the above with a Stream, but this is easier in Java
        waterGiven[numOfTables - 1] = numOfTables;
        long startingTime = System.currentTimeMillis();
        assertEquals(numOfTables, hakofos.doIt(waterGiven, largeWaterNeeded));
        assertTrue(System.currentTimeMillis() - startingTime < 10000);
        // making sure that it is right, and that it is done in a reasonable amount of time
    }

    // I feel like there are important tests that I am missing, but I can't think of them. I'll add them later

    // tests that we have O(n) efficiency in the worst case
    // With my current implementation, the worst case should actually be when there are a lot of
        // potential tables
    // So I need every other table not to be good enough, until the last one, which has for everything
    @Test
    public void EfficiencyTest() {
        BigOItBase bigOTester = new BigOIt();
        double ratio = bigOTester.doublingRatio("testing.HakofosTest$HakofosMeasurable", 10000);
        // timeout is 10 seconds
        System.out.println(ratio);
        assertTrue(ratio > 1.8);
        assertTrue(ratio < 2.2);
    }

    public static class HakofosMeasurable extends BigOMeasurable {

        private final HydratedHakofos hakofos;
        private int[] waterNeeded;
        private int[] waterGiven;

        /**
         * Constructor
         */
        public HakofosMeasurable() {
            hakofos = new HydratedHakofos();
        }

        /**
         * Set up internal data-structures etc as a function of "n": default
         * implementation.  The duration of this method MUST NOT be counted when
         * evaluating algorithm performance.
         *
         * @param n size to be setup for
         */
        @Override
        public void setup(int n) {
            super.setup(n);

            waterNeeded = new int[n];
            for (int i = 0; i < n; i++) {
                waterNeeded[i] = 1;
            }

            waterGiven = new int[n];
            for (int i = 0; i < n; i+=2) { // adding a drink to every other place, to make
                    // calculations potentially more complicated
                waterGiven[i] = 1;
            }
            waterGiven[n - 1] = n;
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
            hakofos.doIt(waterNeeded, waterGiven);
        }
    }

}
