import org.dalton.polyfun.Atom;
import org.dalton.polyfun.Coef;
import org.dalton.polyfun.Polynomial;
import org.dalton.polyfun.Term;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import testlib.PolyPair;

// TODO: add test cases with negative coefficients.
public class PolynomialTest {

    // Create output streams to capture .print() output.
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Before
    public void setUp() {
        // Point System.out to another output stream so I can test the print() outputs.
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void restoreStreams() {
        // Point System.out back to console.
        System.setOut(System.out);
    }

    @Test
    public void createAndCompareRandomPolyPairs() {
        PolyPair polyPair = new PolyPair();

        // Print to System.err
        System.err.println(polyPair.polynomial_refactored);

        // Compare each part
        comparePolynomials(polyPair.polynomial_orig, polyPair.polynomial_refactored);

        // Compare printed string
        polyPair.polynomial_orig.print();
        Assert.assertEquals(outContent.toString(), polyPair.polynomial_refactored.toString());

    }

    @Test
    public void printPolynomials_CompareV6V11() {
        PolyPair polyPair = new PolyPair();

        polyPair.polynomial_orig.print();
        System.err.println(outContent.toString());
        Assert.assertEquals(outContent.toString(), polyPair.polynomial_refactored.toString());
    }

    @Test
    public void polynomialParts_CompareV6V11() {
        PolyPair polyPair = new PolyPair();

        comparePolynomials(polyPair.polynomial_orig, polyPair.polynomial_refactored);
    }

    @Test
    public void addPolynomialsToSelf_CompareV6V11() {
        PolyPair polyPair = new PolyPair();

        polyfun.Polynomial sum_orig = polyPair.polynomial_orig.plus(polyPair.polynomial_orig);
        Polynomial sum_refactored = polyPair.polynomial_refactored.plus(polyPair.polynomial_refactored);
        sum_orig.print();
        Assert.assertEquals(outContent.toString(), sum_refactored.toString());
    }

    @Test
    public void multiplyPolynomialsToSelf_CompareV6V11() {
        PolyPair polyPair = new PolyPair();
        PolyPair productPair = new PolyPair(polyPair.polynomial_orig.times(polyPair.polynomial_orig),
                polyPair.polynomial_refactored.times(polyPair.polynomial_refactored));

        productPair.polynomial_orig.print();
        Assert.assertEquals(outContent.toString(), productPair.polynomial_refactored.toString());
    }

    @Test
    public void addTangent_CompareV6V11() {
        PolyPair polyPair = new PolyPair();
        PolyPair sumPair = new PolyPair(polyPair.polynomial_orig.addTangent(),
                polyPair.polynomial_refactored.addTangent());

        sumPair.polynomial_orig.print();
        Assert.assertEquals(outContent.toString(), sumPair.polynomial_refactored.toString());
    }

    @Test
    public void testEvaluate_CompareToPolyfunOld() {
        double[] coefficients = {1, -3, 0, 2};

        PolyPair polyPair = new PolyPair(coefficients);

        double old_result = polyPair.polynomial_orig.evaluate(3).getTerms()[0].getTermDouble();
        double new_result = polyPair.polynomial_refactored.evaluate(3).getTerms()[0].getNumericalCoefficient();

        Assert.assertTrue(old_result == new_result);
    }

    @Test
    public void testPlus() {
        double[] coefficients = {1, -3, 0, 2};
        Polynomial poly = new Polynomial(coefficients);

        // Create the same polynomial in a different way.
        Polynomial a = new Polynomial(2.0, 3); // creates the "polynomial" 2x^3
        Polynomial b = new Polynomial(-3.0, 1); // creates the "polynomial" -3x
        Polynomial c = new Polynomial(1.0);     // creates the constant "polynomial" 1

        //Create our polynomial by adding the three we have just created.
        Polynomial P = a.plus(b.plus(c));

        Assert.assertEquals(poly.toString(), P.toString());
    }

    @Test
    public void testEvaluate() {
        double[] coefficients = {1, -3, 0, 2};
        Polynomial poly = new Polynomial(coefficients);

        double y = poly.evaluate(3).getTerms()[0].getNumericalCoefficient();

        Assert.assertTrue(46.0 == y);
    }

    @Test
    public void testTo_CompareV6V11() {
        // Create 2 identical polynomials
        double[] coefficients = {1, -3, 0, 2};

        PolyPair polyPair = new PolyPair(coefficients);

        // Raise both to the power of 3
        polyfun.Polynomial old_result = polyPair.polynomial_orig.to(3);
        Polynomial new_result = polyPair.polynomial_refactored.to(3);

        // Compare both
        comparePolynomials(old_result, new_result);
    }

    @Test
    public void testToRandom_CompareV6V11() {
        PolyPair polyPair = new PolyPair();
        PolyPair raisePolys = new PolyPair(polyPair.polynomial_orig.to(5), polyPair.polynomial_refactored.to(5));

        // Compare parts
        comparePolynomials(raisePolys.polynomial_orig, raisePolys.polynomial_refactored);

        // Also test printed versions
        raisePolys.polynomial_orig.print();
        Assert.assertEquals(outContent.toString(), raisePolys.polynomial_refactored.toString());
    }

    @Test
    public void testTo_0_CompareToPolyfunOld() {
        // Create 2 identical polynomials
        double[] coefficients = {1, -3, 0, 2};

        PolyPair polyPair = new PolyPair(coefficients);
        PolyPair raisedPolyPair = new PolyPair(polyPair.polynomial_orig.to(0),
                polyPair.polynomial_refactored.to(0));

        // Compare both
        comparePolynomials(raisedPolyPair.polynomial_orig, raisedPolyPair.polynomial_refactored);
    }

    @Test
    public void testPlus_CompareToPolyfunOld() {
        // Create 2 identical polynomials
        double[] coefficients = {1, -3, 0, 2};

        polyfun.Polynomial oldPoly = new polyfun.Polynomial(coefficients);
        Polynomial newPoly = new Polynomial(coefficients);

        // Raise both to the power of 0
        polyfun.Polynomial oldResult = oldPoly.plus(oldPoly);
        Polynomial newResult = newPoly.plus(newPoly);

        // Compare both
        comparePolynomials(oldResult, newResult);
    }

    @Test
    public void testTimes_CompareToPolyfunOld() {
        // Create 2 identical polynomials
        double[] coefficients = {1, -3, 0, 2};

        polyfun.Polynomial oldPoly = new polyfun.Polynomial(coefficients);
        Polynomial newPoly = new Polynomial(coefficients);

        // Raise both to the power of 0
        polyfun.Polynomial oldResult = oldPoly.times(oldPoly);
        Polynomial newResult = newPoly.times(newPoly);

        // Compare both
        comparePolynomials(oldResult, newResult);
    }

    @Test
    public void testMinus_CompareToPolyfunOld() {
        // Create 2 identical polynomials
        double[] coefficients = {1, -3, 0, 2};
        polyfun.Polynomial oldPoly = new polyfun.Polynomial(coefficients);
        Polynomial newPoly = new Polynomial(coefficients);

        // Create another 2 identical polynomials
        double[] coefficients2 = {4, -3, 2};
        polyfun.Polynomial oldPoly2 = new polyfun.Polynomial(coefficients2);
        Polynomial newPoly2 = new Polynomial(coefficients2);

        // Subtract from self.
        polyfun.Polynomial oldResult = oldPoly.minus(oldPoly2);
        Polynomial newResult = newPoly.minus(newPoly2);

        // Compare both
        comparePolynomials(oldResult, newResult);
    }

    @Test
    public void testOf_CompareToPolyfunOld() {
        // Create 2 identical polynomials
        double[] coefficients = {1, -3, 0, 2};

        polyfun.Polynomial oldPoly = new polyfun.Polynomial(coefficients);
        Polynomial newPoly = new Polynomial(coefficients);

        // Compose new poly of the poly and itself
        polyfun.Polynomial oldResult = oldPoly.of(oldPoly);
        Polynomial newResult = newPoly.of(newPoly);

        // Compare both
        comparePolynomials(oldResult, newResult);
    }

    @Test
    public void testAddTangent_CompareToPolyfunOld() {
        // Create 2 identical polynomials
        double[] coefficients = {1, -3, 0, 2};

        polyfun.Polynomial oldPoly = new polyfun.Polynomial(coefficients);
        Polynomial newPoly = new Polynomial(coefficients);

        // Compose new poly of the poly and itself
        polyfun.Polynomial oldResult = oldPoly.addTangent();
        Polynomial newResult = newPoly.addTangent();

        // Compare both
        comparePolynomials(oldResult, newResult);
    }

    private void comparePolynomials(polyfun.Polynomial oldPoly, Polynomial newPoly) {
        // Compare number of coefficients.
        polyfun.Coef[] oldCoefs = oldPoly.getCoefficients();
        Coef[] newCoefs = newPoly.getCoefs();

        Assert.assertEquals(oldCoefs.length, newCoefs.length);

        // Compare Coef by Coef.
        for (int i = 0; i < oldCoefs.length; i++) {
            compareCoefs(oldCoefs[i], newCoefs[i]);
        }
    }

    private void compareCoefs(polyfun.Coef oldCoef, Coef newCoef) {
        polyfun.Term[] oldTerms = oldCoef.getTerms();
        Term[] newTerms = newCoef.getTerms();

        Assert.assertEquals(oldTerms.length, newTerms.length);

        // For each Term array, compare Term by Term
        for (int j = 0; j < oldTerms.length; j++) {
            compareTerms(oldTerms[j], newTerms[j]);
        }
    }

    private void compareTerms(polyfun.Term oldTerm, Term newTerm) {
        // Compare term coefficients
        Assert.assertTrue(oldTerm.getTermDouble() == newTerm.getNumericalCoefficient());

        // For each Term, compare Atom array by Atom array
        polyfun.Atom[] oldAtoms = oldTerm.getTermAtoms();
        Atom[] newAtoms = newTerm.getAtoms();

        Assert.assertEquals(oldAtoms.length, newAtoms.length);

        // For each Atom, compare the parts.
        for (int i = 0; i < newAtoms.length; i++) {
            Assert.assertEquals(oldAtoms[i].getLetter(), newAtoms[i].getLetter());
            Assert.assertEquals(oldAtoms[i].getPower(), newAtoms[i].getPower());
            Assert.assertEquals(oldAtoms[i].getSubscript(), newAtoms[i].getSubscript());
        }
    }
}
