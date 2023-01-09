package testing;

import edu.yu.introtoalgs.*;
import edu.yu.introtoalgs.XenoHematology;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HematologyTest {

    private final XenoHematology testCenter;

    public HematologyTest() {
        testCenter = new XenoHematology(100);
    }

    // tests that if we have a negative member, IAE
    @Test
    public void throwsIfNegative() {
        assertThrows(IllegalArgumentException.class, ()->testCenter.areCompatible(-1, 0));
        assertThrows(IllegalArgumentException.class, ()->testCenter.areIncompatible(0, -10));
        assertThrows(IllegalArgumentException.class, ()->testCenter.setCompatible(-1, 0));
        assertThrows(IllegalArgumentException.class, ()->testCenter.setIncompatible(0, -2000));
    }

    // tests that if we have a >= populationSize member, IAE
    @Test
    public void throwsIfDoesNotExist() {
        assertThrows(IllegalArgumentException.class, ()->testCenter.areCompatible(0, 100));
        assertThrows(IllegalArgumentException.class, ()->testCenter.areIncompatible(1000, 0));
        assertThrows(IllegalArgumentException.class, ()->testCenter.setCompatible(1, 100));
        assertThrows(IllegalArgumentException.class, ()->testCenter.setIncompatible(101, 1));
    }

    // I need to test that before we give any information, any 2 are guaranteed to be false for both methods
    @Test
    public void testStartsFalse() {
        assertFalse(testCenter.areCompatible(0, 1));
        assertFalse(testCenter.areIncompatible(0, 1));
    }

    // When we have information, but not about this pair, returns false
    @Test
    public void testInfoStillFalse() {
        testCenter.setCompatible(2, 3);
        testCenter.setIncompatible(4, 5);

        assertFalse(testCenter.areCompatible(0, 1));
        assertFalse(testCenter.areIncompatible(0, 1));
    }

    // When we have information about one, but not the other, returns false
    @Test
    public void testOneStillFalse() {
        testCenter.setCompatible(0, 3);
        testCenter.setIncompatible(1, 5);

        assertFalse(testCenter.areCompatible(0, 1));
        assertFalse(testCenter.areIncompatible(0, 1));
    }

    // When we have direct information about 2 aliens, it is reflected, no matter which combination
        // Test for 2 set compatible, both check yes and no, and set incompatible, yes and no
    @Test
    public void testSetInfoWorks() {
        testCenter.setCompatible(0, 1);
        assertTrue(testCenter.areCompatible(0, 1));
        assertFalse(testCenter.areIncompatible(0, 1));

        testCenter.setIncompatible(2, 3);
        assertFalse(testCenter.areCompatible(2, 3));
        assertTrue(testCenter.areIncompatible(2, 3));
    }

    // Test that compatibility and incompatibility are symmetric
    @Test
    public void testSetInfoSymmetric() {
        testCenter.setCompatible(1, 0);
        assertTrue(testCenter.areCompatible(0, 1));
        assertFalse(testCenter.areIncompatible(0, 1));

        testCenter.setIncompatible(3, 2);
        assertFalse(testCenter.areCompatible(2, 3));
        assertTrue(testCenter.areIncompatible(2, 3));
    }

    // Test that compatibility is reflexive, even if no info has been given
    @Test
    public void testSelfStartsTrue() {
        assertTrue(testCenter.areCompatible(1, 1));
        assertFalse(testCenter.areIncompatible(1, 1));
    }

    // Test that if set x and y compatible and y and z compatible, x and z are compatible
    @Test
    public void testCompatibleTransitive() {
        testCenter.setCompatible(0, 1);
        testCenter.setCompatible(1, 2);

        assertTrue(testCenter.areCompatible(0, 2));
        assertFalse(testCenter.areIncompatible(0, 2));
    }

    // Test that if set x and y incompatible and y and z incompatible, x and z are compatible
    @Test
    public void testIncompatibleAntiTransitive() {
        testCenter.setIncompatible(0, 1);
        testCenter.setIncompatible(1, 2);

        assertTrue(testCenter.areCompatible(0, 2));
        assertFalse(testCenter.areIncompatible(0, 2));
    }

    // Test that if set x and y compatible and y and z incompatible, x and z are incompatible
    @Test
    public void testCompatibleExtendsToIncompatible() {
        testCenter.setCompatible(0, 1);
        testCenter.setIncompatible(1, 2);

        assertFalse(testCenter.areCompatible(0, 2));
        assertTrue(testCenter.areIncompatible(0, 2));
    }

    // tests that if set x and y one way, can't change to the other way (also for symmetric)
    @Test
    public void testSetInfoCantChange() {
        testCenter.setCompatible(0, 1);
        testCenter.setIncompatible(1, 0);

        assertTrue(testCenter.areCompatible(0, 1));
        assertFalse(testCenter.areIncompatible(0, 1));
    }

    // tests that if learn compatibility from transitive property, can't be changed
    @Test
    public void testCompatibleTransitiveCantChange() {
        testCenter.setCompatible(0, 1);
        testCenter.setCompatible(1, 2);
        testCenter.setIncompatible(0, 2);

        assertTrue(testCenter.areCompatible(0, 2));
        assertFalse(testCenter.areIncompatible(0, 2));
    }

    // tests that if learn compatibility from 2 incompatibilities, can't be changed
    @Test
    public void testIncompatibleAntiTransitiveCantChange() {
        testCenter.setIncompatible(0, 1);
        testCenter.setIncompatible(1, 2);
        testCenter.setIncompatible(0, 2);

        assertTrue(testCenter.areCompatible(0, 2));
        assertFalse(testCenter.areIncompatible(0, 2));
    }

    // tests that if learn incompatibility from 2 a compatibility and an incompatibility, can't be changed
    @Test
    public void testCompatibleExtendsToIncompatibleCantChange() {
        testCenter.setCompatible(0, 1);
        testCenter.setIncompatible(1, 2);
        testCenter.setCompatible(0, 2);

        assertFalse(testCenter.areCompatible(0, 2));
        assertTrue(testCenter.areIncompatible(0, 2));
    }

    // test that even after check, can still set more
    @Test
    public void testCanSetAndAdd() {
        testCenter.setCompatible(0, 1);
        assertTrue(testCenter.areCompatible(0, 1));
        assertFalse(testCenter.areIncompatible(0, 1));

        testCenter.setIncompatible(0, 3);
        assertFalse(testCenter.areCompatible(0, 3));
        assertTrue(testCenter.areIncompatible(0, 3));
    }

    // test that just because 2 pairs haven't been examined with each other doesn't mean they are incompatible
    @Test
    public void testIndependentAssociation() {
        testCenter.setCompatible(2, 3);
        testCenter.setCompatible(4, 5);

        assertFalse(testCenter.areIncompatible(2, 4));
        assertFalse(testCenter.areIncompatible(3, 5));
        assertFalse(testCenter.areIncompatible(3, 4));
        assertFalse(testCenter.areIncompatible(2, 5));
    }

    // test that joining 2 pairs causes all 4 to know each other
    @Test
    public void testBigUnion() {
        testCenter.setCompatible(2, 3);
        testCenter.setCompatible(4, 5);

        testCenter.setCompatible(2, 4);

        assertTrue(testCenter.areCompatible(2, 4));
        assertTrue(testCenter.areCompatible(3, 5));
        assertTrue(testCenter.areCompatible(3, 4));
        assertTrue(testCenter.areCompatible(2, 5));
    }

    // think of more tests, more complicated ones

    // test that works for n^27
    @Test
    public void testWorksLargeNumber() {
        long startTime = System.currentTimeMillis();
        BigOHematology nTester = new BigOHematology();
        nTester.setup((int) Math.pow(2, 27));
        nTester.execute();
        assertTrue(System.currentTimeMillis() - startTime < 10000);
    }

    // test to find out why Professor Leff found problems, copying his test
    // the problem was that I accidentally overwrite rival data when merging unions where each has a rival
    @Test
    public void testProfessor1() {
        XenoHematology newCenter = new XenoHematology(9);

        newCenter.setCompatible(0, 1);
        newCenter.setCompatible(1, 2);
        newCenter.setCompatible(3, 4);
        newCenter.setCompatible(5, 6);

        newCenter.setIncompatible(1, 7);
        newCenter.setIncompatible(5, 8);

        newCenter.setCompatible(2, 5);

        assertTrue(newCenter.areIncompatible(2, 8));
        assertFalse(newCenter.areCompatible(2, 8));
    }

    // test that O(n)
    // this means that we need to run N operations on a population size of N
    // I would like to test the worst case, but what would that be?
    // When the weirder rules are in play
    // So I think my test will do the setIncs for everything pairwise until N/2
    // Then I will do a get for N/2 pairs
    @Test
    public void testOOfN() {
        BigOItBase checkOOfN = new BigOIt();
        double ratio = checkOOfN.doublingRatio("testing.HematologyTest$BigOHematology", 10000);
        assertTrue(ratio < 2.2, ratio + " is too big");
    }

    public static class BigOHematology extends BigOMeasurable {

        private XenoHematology testCenter;

        /**
         * Set up internal data-structures etc as a function of "n": default
         * implementation.  The duration of this method MUST NOT be counted when
         * evaluating algorithm performance.
         *
         * @param n size of population and number of operations
         */
        @Override
        public void setup(int n) {
            super.setup(n);

            testCenter = new XenoHematology(n);
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
        public void execute() { // calls N operations, a mix of sets and gets
            for (int i = 0; i < n/2; i++) {
                testCenter.setIncompatible(i, i+1);
            }
            for (int i = 0; i < n/4; i++) {
                assertTrue(testCenter.areCompatible(i, n/2 - i), i + " and " + (n/2 - i) + " should be compatible.");
            }
            for (int i = 0; i < n/4; i++) {
                assertTrue(testCenter.areIncompatible(i, i + 1), i + " and " + (i + 1) + " should be incompatible.");
            }
        }
    }

}
