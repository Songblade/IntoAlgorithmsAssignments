package testing;

import edu.yu.introtoalgs.PrimeCalculator;
import edu.yu.introtoalgs.PrimesFJ;
import org.junit.jupiter.api.Test;

public class FJTest {

    private final PrimeCalculator calc;

    public FJTest() {
        calc = new PrimesFJ();
    }

    @Test
    public void throwsWhen1OrLessTest() {
        SerialTest.throwsWhen1OrLestTest(calc);
    }

    // test that throws IAE if end == Long.MAX_VALUE
    @Test
    public void throwsAtMaxValue() {
        SerialTest.throwsAtMaxValue(calc);
    }

    // test that returns 1 when 2 -> 2
    @Test
    public void testWorksFirst() {
        SerialTest.testWorksFirst(calc);
    }

    // test that returns 2 when 2 -> 3 and 2 -> 4
    @Test
    public void testWorks2Space() {
        SerialTest.testWorks2Space(calc);
    }

    // test that returns 1 when 3 -> 3 and 3 -> 4
    @Test
    public void testWorksLater1Space() {
        SerialTest.testWorksLater1Space(calc);
    }

    // test that returns 2 when 3 -> 5
    @Test
    public void testWorksLater2Space() {
        SerialTest.testWorksLater1Space(calc);
    }

    // test that returns correct for a much longer input set
    @Test
    public void testWorksTo100() {
        SerialTest.testWorksToNLimit(calc, 100, 25);
        SerialTest.testWorksToNLimit(calc, 101, 26);
    }

    @Test
    public void testWorksHighNumbers() {
        SerialTest.testWorksToNLimit(calc, 1000, 168);
        SerialTest.testWorksToNLimit(calc, 10000, 1229);
        SerialTest.testWorksToNLimit(calc, 100000, 9592);
    }

    @Test
    public void testWorksReallyHighNumbers() {
        SerialTest.testWorksToNLimit(calc, 1_000_000, 78_498);
        SerialTest.testWorksToNLimit(calc, 10_000_000, 664_579);
        SerialTest.testWorksToNLimit(calc, 100_000_000, 5_761_455);
    }

}
