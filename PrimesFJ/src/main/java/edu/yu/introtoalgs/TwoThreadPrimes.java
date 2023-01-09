package edu.yu.introtoalgs;

/* Implements PrimeCalculator interface by using exactly two threads to
 * partition the range of primes between them.  Each thread uses the "naive"
 * SerialPrimes algorithm to solve its part of the problem.
 *
 * Students may not change the constructor signature or add any other
 * constructor!
 *
 * @author Avraham Leff
 */

public class TwoThreadPrimes implements PrimeCalculator {

    /** Constructor
     *
     */
    public TwoThreadPrimes() {
        // your code (if any) goes here
    }

    @Override
    public int nPrimesInRange(final long start, final long end) {

        SerialPrimes.verifyInput(start, end);

        if (end - start == 0) {
            // this should stop problems where threads get exceptions due to bad midpoint values
            PrimeCalculator calc = new SerialPrimes();
            return calc.nPrimesInRange(start, end);
        }

        long midpoint = (start + end) / 2;

        int[] results = new int[2];

        Thread thread1 = new Thread(new PrimeThread(start, midpoint, results, 0));
        Thread thread2 = new Thread(new PrimeThread(midpoint + 1, end, results, 1));

        thread1.start();
        thread2.start();
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return results[0] + results[1];
    }

    private static class PrimeThread implements Runnable {

        private final PrimeCalculator calc;
        private final long start;
        private final long end;
        private final int[] results;
        private final int index;

        private PrimeThread(final long start, final long end, int[] results, int index) {
            this.start = start;
            this.end = end;
            this.results = results;
            this.index = index;
            calc = new SerialPrimes();
        }

        public void run () {
            results[index] = calc.nPrimesInRange(start, end);
        }
    }
}
