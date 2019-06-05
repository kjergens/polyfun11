import org.dalton.polyfun.Atom;
import org.dalton.polyfun.Coef;
import org.dalton.polyfun.Polynomial;
import org.dalton.polyfun.Term;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

/**
 * Randomly generate 1000 polynomials in the v6 library and in the v11 library and make sure they match.
 */

@RunWith(value = Parameterized.class)
public class AddPolynomials {
    private String poly_v6;
    private String polly_v11;

    // Inject via constructor
    public AddPolynomials(String poly_v6, String polly_v11) {
        this.poly_v6 = poly_v6;
        this.polly_v11 = polly_v11;
    }

    @Parameters(name = "{index} {0}")
    public static Collection<Object[]> data() {
        // Create list of random polynomials
        String[][] polyPairs = new String[1000][2];

        Random random = new Random();

        for (int i = 0; i < polyPairs.length; i++) {
            // Get random length from 2 - 6 (so they will be at least a 1 degree poly)
            int numCoefficients = random.nextInt(5) + 2;

            // Create 2 identical Coef arrays.
            polyfun.Coef[] oldCoefs = new polyfun.Coef[numCoefficients];
            Coef[] newCoefs = new Coef[numCoefficients];

            // Fill them using randomly selected numericalCoefficients.
            for (int j = 0; j < oldCoefs.length; j++) {
                double numericalCoefficient = random.nextDouble() * random.nextInt(10);
                oldCoefs[j] = new polyfun.Coef(numericalCoefficient);
                newCoefs[j] = new Coef(numericalCoefficient);
            }

            // Finally, create 2 (hopefully) identical Polynomials
            polyfun.Polynomial polynomial_v6 = new polyfun.Polynomial(oldCoefs);
            Polynomial polynomial_v11 = new Polynomial(newCoefs);

            /** Second poly */
            // Get random length from 2 - 6 (so they will be at least a 1 degree poly)
            numCoefficients = random.nextInt(5) + 2;

            // Create 2 identical Coef arrays.
            oldCoefs = new polyfun.Coef[numCoefficients];
            newCoefs = new Coef[numCoefficients];

            // Fill them using randomly selected numericalCoefficients.
            for (int j = 0; j < oldCoefs.length; j++) {
                double numericalCoefficient = random.nextDouble() * random.nextInt(10);
                oldCoefs[j] = new polyfun.Coef(numericalCoefficient);
                newCoefs[j] = new Coef(numericalCoefficient);
            }

            // Finally, create 2 (hopefully) identical Polynomials
            polyfun.Polynomial polynomial_v6_2 = new polyfun.Polynomial(oldCoefs);
            Polynomial polynomial_v11_2 = new Polynomial(newCoefs);

            /* Add the polys */
            polyfun.Polynomial sum_v6 = polynomial_v6.plus(polynomial_v6_2);
            Polynomial sum_v11 = polynomial_v11.plus(polynomial_v11_2);

            // Get the strings

            // Point System.out to another output stream so I can capture the print() output.
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            PrintStream originalOut = System.out;
            System.setOut(new PrintStream(outContent));

            sum_v6.print();
            polyPairs[i][0] = outContent.toString();

            // Point System.out back to console.
            System.setOut(originalOut);

            polyPairs[i][1] = sum_v11.toString();
        }

        return Arrays.asList(polyPairs);
    }

    @Test
    public void test_RandomPolynomials_Compare_v6_v11() {
        Assert.assertEquals(poly_v6, polly_v11);
    }

}