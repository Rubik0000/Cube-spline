package lab4.spline;

import lab4.slau.SystemOfLinearEquations;
import lab4.slau.Vector;

/**
 * The cube spline with S'(a)=A; S'(b)=B edge conditions
 */
public class CubeSpline implements Function {
    // S[i](x) = a[i] + b[i] (x-x[i]) + c[i] (x-x[i])^2 + d[i] (x-x[i])^3

    /** The 'a' coefficients */
    private double[] aCoef;

    /** The 'b' coefficients */
    private double[] bCoef;

    /** The 'c' coefficients */
    private double[] cCoef;

    /** The 'd' coefficients */
    private double[] dCoef;

    /** The x values */
    private double[] xs;

    private int count;

    /**
     * A constructor
     *
     * @param xTable the 'x' values
     * @param yTable the functions values
     * @param leftCondition the left edge condition
     * @param rightCondition the right edge condition
     */
    public CubeSpline(double[] xTable, double[] yTable, double leftCondition, double rightCondition) {
        count = yTable.length;
        aCoef = getA(yTable, count);
        cCoef = getC(xTable, yTable, count, leftCondition, rightCondition);
        bCoef = getB(xTable, yTable, count, cCoef);
        dCoef = getD(xTable, count, cCoef);
        xs = xTable;
    }

    /**
     * Gets the 'a' coefficients
     *
     * @param yTable the functions values
     * @param count the count of the values
     * @return
     */
    static private double[] getA(double[] yTable, int count) {
        double[] result = new double[count];
        for (int i = 0; i < count; ++i) {
            result[i] = yTable[i];
        }
        return result;
    }

    /**
     * Gets the 'b' coefficients
     *
     * @param xTable the 'x' values
     * @param yTable the functions values
     * @param count the count of the values
     * @param c the 'c' coefficients
     * @return
     */
    static private double[] getB(double[] xTable, double[] yTable, int count, double[] c) {
        double[] result = new double[count];
        for (int i = 1; i < count; ++i) {
            double hi = getStep(xTable[i], xTable[i - 1]);
            result[i - 1] = (yTable[i] - yTable[i - 1]) / hi +
                    (2 * c[i - 1] + (i - 2 >= 0 ? c[i - 2] : 0)) * hi / 3;
        }
        return result;
    }

    /**
     * Gets the 'd' coefficients
     *
     * @param xTable the 'x' values
     * @param count the count of the values
     * @param c the 'c' coefficients
     * @return
     */
    static private double[] getD(double[] xTable, int count,double[] c) {
        double[] result = new double[count];
        for (int i = 1; i < count; ++i) {
            double hi = getStep(xTable[i], xTable[i - 1]);
            result[i - 1] = (c[i - 1] - (i - 2 >= 0 ? c[i - 2] : 0)) / (3 * hi);
        }
        return result;
    }

    /**
     * Gets the 'c' coefficients
     *
     * @param xTable the 'x' values
     * @param yTable the functions values
     * @param count the count of the values
     * @param leftCondition the left edge condition
     * @param rightCondition the right edge condition
     * @return
     */
    static private double[] getC(double[] xTable, double[] yTable, int count, double leftCondition, double rightCondition) {
        double[] a = new double[count];
        double[] b = new double[count];
        double[] c = new double[count];
        double[] d = new double[count];
        /*b[0] = 2 * (getStep(xTable[2], xTable[1]) + getStep(xTable[1], xTable[0]));
        c[0] = getStep(xTable[2], xTable[1]);
        d[0] = 3 * ((yTable[2] - yTable[1]) / getStep(xTable[2], xTable[1]) - (yTable[1] - yTable[0]) / getStep(xTable[1], xTable[0]));*/

        b[0] = -2 * getStep(xTable[1], xTable[0]);
        c[0] = - getStep(xTable[1], xTable[0]);
        d[0] = 3 * (leftCondition - (yTable[1] - yTable[0]) / getStep(xTable[1], xTable[0]));

        for (int i = 2; i < count; ++i) {
            double hi = getStep(xTable[i], xTable[i - 1]);
            double hi_1 = getStep(xTable[i - 1], xTable[i - 2]);
            a[i - 1] = hi_1;
            b[i - 1] = 2 * (hi_1 + hi);
            c[i - 1] = hi;
            d[i - 1] = 3 * ((yTable[i] - yTable[i - 1]) / hi - (yTable[i - 1] - yTable[i - 2]) / hi_1);
        }

        /*b[count - 1] = 2 * (getStep(xTable[count - 2], xTable[count - 3]) + getStep(xTable[count - 1], xTable[count - 2]));
        a[count - 1] = getStep(xTable[count - 2], xTable[count - 3]);
        d[count - 1] = 3 * ((yTable[count - 1] - yTable[count - 2]) / getStep(xTable[count - 1], xTable[count - 2]) - (yTable[count - 2] - yTable[count - 3]) / getStep(xTable[count - 2], xTable[count - 3]));*/

        a[count - 1] = getStep(xTable[count - 1], xTable[count - 2]);
        b[count - 1] = 2 * getStep(xTable[count - 1], xTable[count - 2]);
        d[count - 1] = 3 * (rightCondition - (yTable[count - 1] - yTable[count - 2]) / getStep(xTable[count - 1], xTable[count - 2]));

        Vector result;
        try {
            result = SystemOfLinearEquations.ResolveWithTridiagonalAlgorithm(
                    new Vector(a), new Vector(b), new Vector(c), new Vector(d)
            );
        } catch (Exception e) {
            return null;
        }
        return result.toArray();
    }

    /**
     * Gets the step between two values
     *
     * @param xi
     * @param xi_1
     * @return
     */
    static private double getStep(double xi,double xi_1) {
        return xi - xi_1;
    }

    private int getIndex(double x){
        for (int i = 0; i < count; ++i) {
            if (x < xs[i]) {
                return i - 1;
            }
        }
        return -1;
    }

    @Override
    public double calculate(double arg) {
        int ind = getIndex(arg);
        double diff = arg - xs[ind];
        double result = (aCoef[ind] + bCoef[ind] * diff + cCoef[ind] * Math.pow(diff, 2) + dCoef[ind] * Math.pow(diff, 3));
        return result;
    }
}
