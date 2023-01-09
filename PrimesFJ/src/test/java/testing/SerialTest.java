package testing;

import edu.yu.introtoalgs.BigOMeasurable;
import edu.yu.introtoalgs.PrimeCalculator;
import edu.yu.introtoalgs.SerialPrimes;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SerialTest {

    // this will test the serial application

    // I think I will make public methods testing correctness which will be called by this class
    // and by the classes for the other tests

    // I'm not entirely sure how this class is supposed to work
    // Should I test if this is divisible by every number from 2 to here, or only the primes?
    // I would think only the primes, because that is the only way that makes sense
    // and besides, I am accumulating a list of primes as I solve the problem
    // But that wouldn't really work when parallelized, because then I wouldn't have a list of primes
    // How would I even divide the space between them?
    // The only way to do this is to test every number between this and its square root (<=)
    // Which feels really stupid, when a better way of doing things is so obvious
    // Even by 2-thread, I feel like my initial ideas of doing it are really suboptimal, except it doesn't look like
    // he wants anything besides PrimesFJ to be optimal

    // Anyway, let's stop Kvetching and write tests at some point

    private final PrimeCalculator calc;

    public SerialTest() {
        calc = new SerialPrimes();
    }

    // test that throws IAE if start < 1
    @Test
    public void throwsWhen1OrLessTest() {
        throwsWhen1OrLestTest(calc);
    }

    protected static void throwsWhen1OrLestTest(PrimeCalculator calculator) {
        assertThrows(IllegalArgumentException.class, ()->calculator.nPrimesInRange(1, 5));
        assertThrows(IllegalArgumentException.class, ()->calculator.nPrimesInRange(0, 5));
        assertThrows(IllegalArgumentException.class, ()->calculator.nPrimesInRange(-10, 5));
    }

    // test that throws IAE if end == Long.MAX_VALUE
    @Test
    public void throwsAtMaxValue() {
        throwsAtMaxValue(calc);
    }

    protected static void throwsAtMaxValue(PrimeCalculator calculator) {
        assertThrows(IllegalArgumentException.class, ()->calculator.nPrimesInRange(2, Long.MAX_VALUE));
    }

    // test that returns 1 when 2 -> 2
    @Test
    public void testWorksFirst() {
        testWorksFirst(calc);
    }

    protected static void testWorksFirst(PrimeCalculator calculator) {
        assertEquals(1, calculator.nPrimesInRange(2, 2));
    }

    // test that returns 2 when 2 -> 3 and 2 -> 4
    @Test
    public void testWorks2Space() {
        testWorks2Space(calc);
    }

    protected static void testWorks2Space(PrimeCalculator calculator) {
        assertEquals(2, calculator.nPrimesInRange(2, 3));
        assertEquals(2, calculator.nPrimesInRange(2, 4));
    }

    // test that returns 1 when 3 -> 3 and 3 -> 4
    @Test
    public void testWorksLater1Space() {
        testWorksLater1Space(calc);
    }

    protected static void testWorksLater1Space(PrimeCalculator calculator) {
        assertEquals(1, calculator.nPrimesInRange(3, 3));
        assertEquals(1, calculator.nPrimesInRange(3, 4));
    }

    // test that returns 2 when 3 -> 5
    @Test
    public void testWorksLater2Space() {
        testWorksLater2Space(calc);
    }

    protected static void testWorksLater2Space(PrimeCalculator calculator) {
        assertEquals(1, calculator.nPrimesInRange(3, 3));
        assertEquals(1, calculator.nPrimesInRange(3, 4));
    }

    // test that returns correct for a much longer input set
    @Test
    public void testWorksTo100() {
        testWorksToNLimit(calc, 100, 25);
        testWorksToNLimit(calc, 101, 26);
    }

    @Test
    public void testWorksHighNumbers() {
        testWorksToNLimit(calc, 1000, 168);
        testWorksToNLimit(calc, 10000, 1229);
        testWorksToNLimit(calc, 100000, 9592);
    }

    protected static void testWorksToNLimit(PrimeCalculator calculator, int n, int primeCount) {
        assertEquals(primeCount, calculator.nPrimesInRange(2, n));
        assertEquals(primeCount - 1, calculator.nPrimesInRange(3, n));
    }

    // test that returns the data I need for the graph

}
