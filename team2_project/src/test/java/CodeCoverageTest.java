import org.csed332.project.team2.MyToolWindowFactory;
import org.csed332.project.team2.metrics.CodeCoverageMetric;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;


public class CodeCoverageTest {

    public class DummuTestTarget implements Runnable {

        public void run() {
            isPrime(3);
            isPrime(5);
            isPrime(9);
            isPrime(15);
        }

        private boolean isPrime(final int n) {
            for (int i = 1; i * i <= n; i+=2) {
                if ((n ^ i) == 0) {
                    return false;
                }
            }
            return true;
        }

    }
    @Test
    public void testCodeMetric() throws Exception {
        CodeCoverageMetric codeCoverageMetric = new CodeCoverageMetric(System.out);
        // MyToolWindowFactory cannot read this but left for future reference
        // codeCoverageMetric.setTargetClass(MyToolWindowFactory.TestTarget.class.getName());
        codeCoverageMetric.calculate();
    }
}


