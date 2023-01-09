package edu.yu.introtoalgs;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

public class GraphMeasurerTest {
    // this class will have tests that, rather than testing for correctness, get me data

    // first I will get PrimesFJ threshold data
    // note that I need to change the sample size to something larger for the actual data, this is just to calibrate the threshold

    private final Stopwatch timer;
    private final SerialPrimes serial;
    private final TwoThreadPrimes twoThread;
    private final PrimesFJ calc;

    public GraphMeasurerTest() {
        this.timer = new Stopwatch();
        this.serial = new SerialPrimes();
        this.twoThread = new TwoThreadPrimes();
        this.calc = new PrimesFJ();
        System.out.println("Starting a test");
    }

    //@Test
    public void getThresholdData() {
        System.out.println("\nGetting data for threshold (start=2, end=1_000_000)");
        System.out.println("x is value of threshold, y is FJPrimes length / SerialPrimes length\n");
        getSpeedOfThreshold(3000, false);
        getSpeedOfThreshold(4000, false);
        // to warm up the JVM
        for (int threshold = 500; threshold <= 6000; threshold += 500) {
            getSpeedOfThreshold(threshold, true);
        }
    }

    @Test
    public void getHundredMillionTime() {
        System.out.println("Starting testing FJ with a large number");
        timer.startTimer();
        calc.nPrimesInRange(2, 100_000_000);
        long millisecondTime = timer.getElapsedTime();
        System.out.println("FJ time with hundred million: " + (double)(millisecondTime / 1000) / 60 + " minutes");
    }

    @Test
    public void getHundredMillionTimeSerial() {
        System.out.println("Starting testing Serial with a large number");
        timer.startTimer();
        serial.nPrimesInRange(2, 100_000_000);
        long millisecondTime = timer.getElapsedTime();
        int timeInSeconds = (int) (millisecondTime / 1000);
        System.out.println("\nSerial time with hundred million: " + (double)timeInSeconds / 60 + " minutes");
        System.out.println("Time in seconds: " + timeInSeconds);
        if (timeInSeconds > 400) {
            fail();
        }
    }

    private void getSpeedOfThreshold(int threshold, boolean shouldPrint) {
        calc.changeThreshold(threshold);
        // this was when I was trying to find the ideal threshold
        /*long totalTime = 0;
        for (int i = 0; i < 50; i++) {
            totalTime += getTimeOfN(calc, 1_000_000);
        }
        long averageTime = totalTime / 50;*/
        if (shouldPrint) {
            System.out.println("threshold x: " + threshold);
            System.out.println("efficiency y: " + getEfficiencyForN(calc, 1_000_000));
        }
    }

    @Test
    public void getOOfSerial() {
        System.out.println("Getting order of growth for SerialPrimes");
        BigOIt oGetter = new BigOIt();
        double ratio = oGetter.doublingRatio("edu.yu.introtoalgs.GraphMeasurerTest$SerialMeasurable", 10000);
        System.out.println("Serial Ratio: " + ratio);
    }

    @Test
    public void getOOfTwoThread() {
        System.out.println("Getting order of growth for TwoThreadPrimes");
        BigOIt oGetter = new BigOIt();
        double ratio = oGetter.doublingRatio("edu.yu.introtoalgs.GraphMeasurerTest$TwoThreadMeasurable", 20000);
        System.out.println("TwoThread Ratio: " + ratio);
    }

    @Test
    public void getOOfFJ() {
        System.out.println("Getting order of growth for FJPrimes");
        BigOIt oGetter = new BigOIt();
        double ratio = oGetter.doublingRatio("edu.yu.introtoalgs.GraphMeasurerTest$FJMeasurable", 20000);
        System.out.println("FJ Ratio: " + ratio);
    }

    //@Test
    public void getTwoThreadEfficiency() {
        System.out.println();
        System.out.println("Getting efficiency of TwoThreadPrimes");
        System.out.println("x is value of end (start=2), y is TwoThreadPrimes length / SerialPrimes length");
        System.out.println();
        getEfficiency(twoThread);
    }

    private long getTimeOfN(PrimeCalculator calcType, int end) {
        long totalTime = 0;
        for (int i = 0; i < 50; i++) {
            timer.startTimer();
            calcType.nPrimesInRange(2, end);
            totalTime += timer.getElapsedTime();
        }
        return totalTime / 50;
    }

    private void getEfficiency(PrimeCalculator calcType) {
        // warm up the JVM
        getEfficiencyForN(calcType, 500_000);
        getEfficiencyForN(calcType, 600_000);

        for (int end = 100_000; end < 1_000_000; end += 100_000) {
            System.out.println("end x: " + end);
            System.out.println("efficiency y: " + getEfficiencyForN(calcType, end));
        }
    }

    private double getEfficiencyForN(PrimeCalculator calcType, int end) {
        long serialTime = getTimeOfN(serial, end);
        long calcTime = getTimeOfN(calcType, end);
        return (double) calcTime / serialTime;
    }

    //@Test
    public void getFJEfficiency() {
        System.out.println();
        System.out.println("Getting efficiency of FJPrimes");
        System.out.println("x is value of end (start=2), y is FJPrimes length / SerialPrimes length");
        System.out.println();
        getEfficiency(calc);
    }

    private static class SerialMeasurable extends BigOMeasurable {

        private PrimeCalculator calc;

        public SerialMeasurable() {
            calc = new SerialPrimes();
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
            calc.nPrimesInRange(2, n);
            // I wish that there were a way of testing correctness too, but there isn't
        }
    }

    private static class TwoThreadMeasurable extends SerialMeasurable {

        public TwoThreadMeasurable() {
            super.calc = new TwoThreadPrimes();
        }

    }

    private static class FJMeasurable extends SerialMeasurable {

        public FJMeasurable() {
            super.calc = new PrimesFJ();
        }

    }

    private static class Stopwatch {
        private long startTime;

        private Stopwatch() {
            startTimer();
        }

        private void startTimer() {
            startTime = System.currentTimeMillis();
        }

        private long getElapsedTime() {
            return System.currentTimeMillis() - startTime;
        }
    }
}
