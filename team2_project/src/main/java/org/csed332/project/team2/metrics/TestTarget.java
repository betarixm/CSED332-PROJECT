package org.csed332.project.team2.metrics;


// for testing purpose only
// CodeCoverageMetric class uses this class for integrity.
public class TestTarget implements Runnable {

    public void run() {
        isPrime(7);
    }

    private boolean isPrime(final int n) {
        for (int i = 2; i * i <= n; i++) {
            if ((n ^ i) == 0) {
                return false;
            }
        }
        return true;
    }

}
