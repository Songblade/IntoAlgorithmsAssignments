package edu.yu.introtoalgs;

/** Implements PrimeCalculator using a "naive" serial computation.
 *
 * Students may not change the constructor signature or add any other
 * constructor!
 *
 * @author Avraham Leff
 */

public class SerialPrimes implements PrimeCalculator {

    /** Constructor
     *
     */
    public SerialPrimes() {
        // your code (if any) goes here
    }

    @Override
    public int nPrimesInRange(final long start, final long end) {
        verifyInput(start, end);
        // Ideally, I would use an n^2/lgn solution, where I would test each prime on each number
        // But I can't, because that isn't parrallelizable
        // So instead, I will check each number between 2 and sqrt(num) (inclusive)
        // If it is divisible, we exit that loop
        // If it is not, we continue
        // If we go through a loop without finding it divisible, we increase the prime counter
        int numPrimes = 0;
        for (long number = start; number <= end; number++) {
            boolean foundPrime = true;
            for (long factor = 2; factor <= Math.sqrt(number); factor++) {
                double difference = (double) number / factor;
                if (difference == (long) difference) { // if number is divisible by factor
                    foundPrime = false;
                    break;
                }
            }
            if (foundPrime) {
                numPrimes++;
            }
        }

        return numPrimes;
    }

    // I am making it protected, so I can use it elsewhere
    protected static void verifyInput(final long start, final long end) {
        if (start <= 1) {
            throw new IllegalArgumentException("start " + start + " is <= 1");
        }
        if (end == Long.MAX_VALUE) {
            throw new IllegalArgumentException("end is at max value");
        }
        if (end < start) {
            throw new IllegalArgumentException("end: " + end + " is less than start: " + start);
        }
    }
}
