package lab4.spline;

/**
 * An interface represents the function
 */
@FunctionalInterface
public interface Function {
    /**
     * Calculates the value of the function
     *
     * @param arg the function arguments
     * @return the result
     */
    double calculate(double arg);
}
