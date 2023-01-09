package testing;

import edu.yu.introtoalgs.BigOIt;
import edu.yu.introtoalgs.BigOMeasurable;
import edu.yu.introtoalgs.WealthTransfer;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class WealthTransferTest {
    // this will have the tests for WealthTransfer

    private WealthTransfer accountant;

    public WealthTransferTest() {
        accountant = new WealthTransfer(10);
    }

    // test when 1 person is transferring all his wealth to a second person, works
    @Test
    public void onlyChildTest() {
        accountant.intendToTransferWealth(1, 2, 100, false);
        accountant.setRequiredWealth(2, 1);
        assertEquals(1.0, accountant.solveIt());
    }

    // test that is wealth squared works
    @Test
    public void taxLoopholeTest() {
        accountant.intendToTransferWealth(1, 2, 100, true);
        accountant.setRequiredWealth(2, 4);
        assertEquals(2.0, accountant.solveIt());
    }

    // tests that wealth squared works when decimal
    @Test
    public void taxLoopholeIrrationalTest() {
        accountant.intendToTransferWealth(1, 2, 100, true);
        accountant.setRequiredWealth(2, 2);
        assertEquals(Math.sqrt(2), accountant.solveIt());
    }

    // test that when 1 person transfers wealth to 2 people, works
    @Test
    public void twoChildrenTest() {
        accountant.intendToTransferWealth(1, 2, 50, false);
        accountant.intendToTransferWealth(1, 3, 50, false);
        accountant.setRequiredWealth(2, 1);
        accountant.setRequiredWealth(3, 1);
        assertEquals(2.0, accountant.solveIt());
    }

    // test like above, but not 50/50
    @Test
    public void favoriteChildTest() {
        accountant.intendToTransferWealth(1, 2, 40, false);
        accountant.intendToTransferWealth(1, 3, 60, false);
        accountant.setRequiredWealth(2, 2);
        accountant.setRequiredWealth(3, 3);
        assertEquals(5.0, accountant.solveIt());
    }

    // test that works for 3 inheritors
    @Test
    public void threeChildrenTest() {
        accountant.intendToTransferWealth(1, 2, 30, false);
        accountant.intendToTransferWealth(1, 3, 30, false);
        accountant.intendToTransferWealth(1, 5, 40, false);
        accountant.setRequiredWealth(2, 3);
        accountant.setRequiredWealth(3, 3);
        accountant.setRequiredWealth(5, 4);
        assertEquals(10.0, accountant.solveIt());
    }

    // tests that inheritor can inherit to a third person
    @Test
    public void grandchildTest() {
        accountant.intendToTransferWealth(1, 2, 100, false);
        accountant.intendToTransferWealth(2, 3, 100, false);
        accountant.setRequiredWealth(3, 1);
        assertEquals(1.0, accountant.solveIt());
    }

    // tests that inheritor can give to 2 people
    @Test
    public void twoGrandchildrenTest() {
        accountant.intendToTransferWealth(1, 2, 100, false);
        accountant.intendToTransferWealth(2, 3, 50, false);
        accountant.intendToTransferWealth(2, 4, 50, false);
        accountant.setRequiredWealth(3, 1);
        accountant.setRequiredWealth(4, 1);
        assertEquals(2.0, accountant.solveIt());
    }

    // tests the above, but 1 also gives to another person
    @Test
    public void greatGrandchildTest() {
        accountant.intendToTransferWealth(1, 2, 100, false);
        accountant.intendToTransferWealth(2, 3, 50, false);
        accountant.intendToTransferWealth(2, 4, 50, false);
        accountant.intendToTransferWealth(3, 6, 100, false);
        accountant.setRequiredWealth(4, 1);
        accountant.setRequiredWealth(6, 1);
        assertEquals(2.0, accountant.solveIt());
    }

    // tests can have multiple in the third generation from different parents
    @Test
    public void cousinsTest() {
        accountant.intendToTransferWealth(1, 2, 50, false);
        accountant.intendToTransferWealth(1, 7, 50, false);
        accountant.intendToTransferWealth(2, 3, 100, false);
        accountant.intendToTransferWealth(7, 4, 100, false);
        accountant.setRequiredWealth(3, 1);
        accountant.setRequiredWealth(4, 1);
        assertEquals(2.0, accountant.solveIt());
    }

    // tests for when isWealthSquared recipient also donates
    @Test
    public void complexLoopholeTest() {
        accountant.intendToTransferWealth(1, 2, 100, true);
        accountant.intendToTransferWealth(2, 3, 50, false);
        accountant.intendToTransferWealth(2, 4, 50, false);
        accountant.setRequiredWealth(3, 2);
        accountant.setRequiredWealth(4, 2);
        assertEquals(2.0, accountant.solveIt());
    }

    // tests that works if setRequiredWealth is called first
    @Test
    public void whinyChildTest() {
        accountant.setRequiredWealth(2, 1);
        accountant.intendToTransferWealth(1, 2, 100, false);
        assertEquals(1.0, accountant.solveIt());
    }

    // tests that if one is donating more than another expects to receive, the other gets it anyway
    @Test
    public void allChildrenLovedEquallyTest() {
        accountant.intendToTransferWealth(1, 2, 50, false);
        accountant.intendToTransferWealth(1, 3, 50, false);
        accountant.setRequiredWealth(2, 2);
        accountant.setRequiredWealth(3, 1);
        assertEquals(4.0, accountant.solveIt());
    }

    // test that a loophole can be given to one child but not another
    @Test
    public void realTaxEvasionTest() {
        accountant.intendToTransferWealth(1, 2, 50, true);
        accountant.intendToTransferWealth(1, 3, 50, false);
        accountant.setRequiredWealth(2, 4);
        accountant.setRequiredWealth(3, 2);
        assertEquals(4.0, accountant.solveIt());
    }

    @Test
    public void surpriseTaxGiftTest() {
        accountant.intendToTransferWealth(1, 2, 50, true);
        accountant.intendToTransferWealth(1, 3, 50, false);
        accountant.setRequiredWealth(2, 2);
        accountant.setRequiredWealth(3, 2);
        assertEquals(4.0, accountant.solveIt());
    }

    // tests that can have 2->3 set before 1->2
    @Test
    public void advancedPlanningTest() {
        accountant.intendToTransferWealth(2, 3, 100, false);
        accountant.intendToTransferWealth(1, 2, 100, false);
        accountant.setRequiredWealth(3, 1);
        assertEquals(1.0, accountant.solveIt());
    }

    // test for three generations of squaring wealth
    @Test
    public void multiGenerationalTaxEvasionTest() {
        accountant.intendToTransferWealth(1, 2, 100, true);
        accountant.intendToTransferWealth(2, 3, 100, true);
        accountant.intendToTransferWealth(3, 4, 100, true);
        accountant.setRequiredWealth(4, 256);
        assertEquals(2.0, accountant.solveIt());
    }

    // test for giving a child-grandchild relationship, and then establishing a square parent-child relationship later
    @Test
    public void lastMinuteTaxEvasionTest() {
        accountant.intendToTransferWealth(2, 3, 100, false);
        accountant.intendToTransferWealth(1, 2, 100, true);
        accountant.setRequiredWealth(3, 4);
        assertEquals(2.0, accountant.solveIt());
    }

    // the above, but we then add a second grandchild
    @Test
    public void lastMinuteTaxEvasionComplicatedTest() {
        accountant.intendToTransferWealth(2, 3, 50, false);
        accountant.intendToTransferWealth(1, 2, 100, true);
        accountant.intendToTransferWealth(2, 4, 50, false);
        accountant.setRequiredWealth(3, 2);
        accountant.setRequiredWealth(4, 2);
        assertEquals(2.0, accountant.solveIt());
    }

    // a test where a child and both grandchildren are defined before the parent, and the grandchildren inherit
    // unequally in a way where one gets more
    @Test
    public void unequalInheritanceMissingParentTest() {
        accountant.intendToTransferWealth(2, 3, 50, false);
        accountant.intendToTransferWealth(2, 4, 50, false);
        accountant.intendToTransferWealth(1, 2, 100, false);
        accountant.setRequiredWealth(3, 1);
        accountant.setRequiredWealth(4, 2);
        assertEquals(4.0, accountant.solveIt());
    }

    // tests for a large number
    @Test
    public void largeNumberOfMethodCallsLinear() {
        int size = 1_000_000;
        accountant = new WealthTransfer(size);
        for (int i = 1; i <= size - 1; i++) {
            accountant.intendToTransferWealth(i, i + 1, 100, false);
        }
        accountant.setRequiredWealth(size, 1);
        assertEquals(1.0, accountant.solveIt());
    }

    @Test
    public void largeNumberOfMethodCallsTree() {
        // let's see how we do this
        // we will do log(i) to find the current donor
        // this will probably fail the first time, not because my code is wrong, but because I
            // got the math wrong
        int size = 1_010_101;
        accountant = new WealthTransfer(size);
        for (int i = 2; i <= 101; i++) {
            accountant.intendToTransferWealth(1, i, 1, false);
            for (int j = 1; j <= 100; j++) {
                int level2Donor = (i-1) * 100 + 1 + j;
                accountant.intendToTransferWealth(i, level2Donor, 1, false);
                for (int k = 1; k <= 100; k++) {
                    int recipient = (i-1) * 10000 + j * 100 + k + 1;
                    accountant.intendToTransferWealth(level2Donor, recipient, 1, false);
                    // who do I transfer to?
                    // I go 1 -> 100 -> 10_000 -> 1_000_000
                    // So, if this is the first one, I am donating to 10102
                    // while the second donor will be donating to 20102
                    // my math could be very off here
                    accountant.setRequiredWealth(recipient, 1);
                }
            }
        }
        assertEquals(1_000_000, accountant.solveIt());
    }

    private void assertIAE(Executable method) {
        assertThrows(IllegalArgumentException.class, method);
    }

    // tests that for valid ID for intendToTransfer
    @Test
    public void invalidIDIntendToTransferIAETest() {
        assertIAE(()->accountant.intendToTransferWealth(0, 2, 1, false));
        assertIAE(()->accountant.intendToTransferWealth(-1, 2, 1, false));
        assertIAE(()->accountant.intendToTransferWealth(1, 11, 1, false));
        assertIAE(()->accountant.intendToTransferWealth(1, 10000, 1, false));
        assertIAE(()->accountant.intendToTransferWealth(1, 1, 1, false));
        assertIAE(()->accountant.intendToTransferWealth(2, 2, 1, false));
    }

    // tests that intendToTransfer wealth throws if percentage is invalid
    @Test
    public void invalidPercentageIntendToTransferIAETest() {
        assertIAE(()->accountant.intendToTransferWealth(1, 2, 0, false));
        assertIAE(()->accountant.intendToTransferWealth(1, 2, 101, false));
        assertIAE(()->accountant.intendToTransferWealth(1, 2, -1, false));
    }

    // tests that you can't have circular inheritance
    @Test
    public void circularInheritanceIAETest() {
        accountant.intendToTransferWealth(1, 2, 1, false);
        assertIAE(()->accountant.intendToTransferWealth(2, 1, 1, false));
    }

    // test that valid ID for intend to receive
    @Test
    public void invalidIDSetRequiredIAETest() {
        assertIAE(()->accountant.setRequiredWealth(0, 1));
        assertIAE(()->accountant.setRequiredWealth(11, 1));
        assertIAE(()->accountant.setRequiredWealth(-1, 1));
        assertIAE(()->accountant.setRequiredWealth(1, 1));
    }

    // test that valid Wealth for intend to receive
    @Test
    public void invalidWealthSetRequiredIAETest() {
        assertIAE(()->accountant.setRequiredWealth(2, 0));
        assertIAE(()->accountant.setRequiredWealth(2, -1));
    }

    // throw IAE if trying to donate to someone who is already receiving money
    @Test
    public void multipleInheritanceIAETest() {
        accountant.intendToTransferWealth(1, 2, 1, false);
        assertIAE(()->accountant.intendToTransferWealth(3, 2, 1, false));
    }

    // throw IAE if trying to change required wealth
    @Test
    public void immutableWealthIAETest() {
        accountant.setRequiredWealth(2, 3);
        assertIAE(()->accountant.setRequiredWealth(2, 1));
    }

    // throw IAE if trying to donate more than 100%
    @Test
    public void donatingAirIAETest() {
        accountant.intendToTransferWealth(1, 2, 50, false);
        assertIAE(()->accountant.intendToTransferWealth(1, 2, 51, false));

        accountant = new WealthTransfer(10);
        accountant.intendToTransferWealth(1, 2, 50, false);
        assertIAE(()->accountant.intendToTransferWealth(1, 3, 51, false));
    }

    // throw IAE if donor set to receive and donate
    @Test
    public void donorIsRecipientIAETest() {
        accountant.intendToTransferWealth(2, 3, 50, false);
        assertIAE(()->accountant.setRequiredWealth(2, 1));
    }

    private void assertISE(Executable method) {
        assertThrows(IllegalStateException.class, method);
    }

    // test ISE if nothing happened
    @Test
    public void nothingHappenedISETest() {
        assertISE(()->accountant.solveIt());
    }

    // test that ISE if 1 hasn't given away everything
    @Test
    public void deadManHasMoneyISETest() {
        accountant.intendToTransferWealth(1, 2, 50, false);
        accountant.setRequiredWealth(2, 1);
        assertISE(()->accountant.solveIt());
    }

    // test that ISE if a donor hasn't given away everything
    @Test
    public void donorHasMoneyISETest() {
        accountant.intendToTransferWealth(1, 2, 100, false);
        accountant.intendToTransferWealth(2, 3, 50, false);
        accountant.setRequiredWealth(3, 1);
        assertISE(()->accountant.solveIt());
    }

    // throw ISE if someone wants to donate but never receives any money
    @Test
    public void donorIsBrokeISETest() {
        accountant.intendToTransferWealth(1, 2, 100, false);
        accountant.setRequiredWealth(2, 1);
        accountant.intendToTransferWealth(3, 4, 100, false);
        accountant.setRequiredWealth(4, 1);
        assertISE(()->accountant.solveIt());
    }

    // throw ISE if someone expects to receive but no one is donating to him
    @Test
    public void disownedFromInheritanceISETest() {
        accountant.intendToTransferWealth(1, 2, 100, false);
        accountant.setRequiredWealth(2, 1);
        accountant.setRequiredWealth(3, 1);
        assertISE(()->accountant.solveIt());
    }

    // I am testing the Order of growth, even though he didn't ask us to
    // It won't have a chance of failure, but it will print out the doubling ratio
    @Test
    public void linearBigOTest(){
        BigOIt doubler = new BigOIt();
        double ratio = doubler.doublingRatio("testing.WealthTransferTest$AccountantMeasurable", 30000);
        System.out.println("Linear ratio: " + ratio);
    }


    public static class AccountantMeasurable extends BigOMeasurable {

        // this is measuring assuming sizeOfPopulation = numberOfCommands - 1
        // it is testing for a case of linear non-square inheritance

        private WealthTransfer accountant;

        /**
         * Set up internal data-structures etc as a function of "n": default
         * implementation.  The duration of this method MUST NOT be counted when
         * evaluating algorithm performance.
         *
         * @param n size of this run
         */
        @Override
        public void setup(int n) {
            super.setup(n);
            accountant = new WealthTransfer(n);
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
            for (int i = 1; i < n; i++) {
                accountant.intendToTransferWealth(i, i+1, 100, false);
            }
            accountant.setRequiredWealth(n, 1);
            assertEquals(1, accountant.solveIt());
        }
    }
}
