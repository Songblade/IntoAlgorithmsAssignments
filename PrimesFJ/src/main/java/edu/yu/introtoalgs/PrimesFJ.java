package edu.yu.introtoalgs;

/* Implements PrimeCalculator interface with Java's Fork/Join parallelism
 * framework.  The implementation must determine what threshold value to use,
 * but should favor thresholds that produce good results for "end" values of
 * (at least) hundreds of millions).
 *
 * Students may not change the constructor signature or add any other
 * constructor!
 *
 * @author Avraham Leff
 */

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class PrimesFJ implements PrimeCalculator {

    private int threshold;

    /** Constructor
     *
     */
    public PrimesFJ() {
        // your code (if any) goes here
        threshold = 2500; // I have no idea if this is a good threshold
        // I will change it later
    }

    /**
     * This method is used for testing, so I can easily change the threshold on the fly
     * Since I am only using it internally, I am not checking to make sure it works
     * @param newThreshold what we are changing threshold to
     */
    protected void changeThreshold(int newThreshold) {
        threshold = newThreshold;
    }

    @Override
    public int nPrimesInRange(final long start, final long end) {
        SerialPrimes.verifyInput(start, end);
        //System.out.println("Starting searching for end = " + end);

        ForkJoinPool pool = new ForkJoinPool(); // I am using the default constructor, which makes based on
            // the number of cores I have
        PrimeSum task = new PrimeSum(threshold, start, end);
        int sum = pool.invoke(task);
        //System.out.println("Invoking shutdown");
        pool.shutdown();
        // your code goes here
        return sum;
    }

    private static class PrimeSum extends RecursiveTask<Integer> {

        private final int threshold;
        private final long start;
        private final long end;

        protected PrimeSum(int threshold, long start, long end) {
            this.threshold = threshold;
            this.start = start;
            this.end = end;
        }

        /**
         * The main computation performed by this task.
         *
         * @return the result of the computation
         */
        @Override
        protected Integer compute() {
            if ((end - start) <= threshold){
                //System.out.println("Using driver for " + start + " to " + end);
                SerialPrimes driver = new SerialPrimes();
                return driver.nPrimesInRange(start, end);
            }
            PrimeSum left = new PrimeSum(threshold, start, (end + start) / 2);
            PrimeSum right = new PrimeSum(threshold, (end + start) / 2 + 1, end);
            //System.out.println("Splitting between start=" + start + ", midpoint=" + ((end + start) /2) + ", end=" + end);
            left.fork();
            int rightAnswer = right.compute();
            int leftAnswer = left.join();
            return rightAnswer + leftAnswer;
        }
    }
}   // PrimesFJ
