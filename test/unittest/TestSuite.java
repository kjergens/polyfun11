package unittest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
        PolynomialTest.class,
        CoefTest.class,
        TermTest.class,
        AtomTest.class
})


public class TestSuite {
}
