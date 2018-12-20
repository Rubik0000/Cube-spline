package lab4.spline;

public class Utils {
    static public double[][] getTableNet(double left, double right, int N, Function func, Splitting type) {
        double[] xs = new double[N + 1];
        double[] ys = new double[N + 1];
        if (type == Splitting.Even) {
            for (int i = 0; i <= N; ++i) {
                xs[i] = left + (right - left) * i / N;
                ys[i] = func.calculate(xs[i]);
            }
        }
        else {
            for (int i = 0; i <= N; ++i) {
                xs[i] = -((left + right) / 2 + (right - left) / 2 * Math.cos((double) (2 * i + 1) / (2 * N + 2) * Math.PI));
                ys[i] = func.calculate(xs[i]);
            }
        }
        double[][] table = new double[2][];
        table[0] = xs;
        table[1] = ys;
        return table;
    }
}
