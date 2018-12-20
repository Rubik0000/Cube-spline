package lab4;

import lab4.spline.CubeSpline;
import lab4.spline.Function;
import lab4.spline.Splitting;
import lab4.spline.Utils;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.internal.chartpart.Chart;
import lab4.view.ChartBuilder;

import java.util.*;

public class Main {
    static public void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            double a = getNumber("Input the left border:", scanner);
            double b = getNumber("Input the right border:", scanner);
            int N = (int)getNumber("Input the number of segments:", scanner);
            double A = getNumber("Input the left edge condition:", scanner);
            double B = getNumber("Input the right edge condition:", scanner);
            Splitting splitting = chooseSplitting(scanner);

            Function pureFunc = x -> x * x + 2;

            double table[][] = Utils.getTableNet(a, b, N, pureFunc, splitting);
            CubeSpline cubeSpline = new CubeSpline(table[0], table[1], A, B);

            Function error = x -> Math.abs(cubeSpline.calculate(x) - pureFunc.calculate(x));

            List<Chart> charts = new ArrayList<>();
            charts.add(
                    new ChartBuilder("Graphics", table[0][0], table[0][table[0].length - 1], pureFunc, "Pure")
                            .AddFunc(cubeSpline, "Spline").build());

            charts.add(new ChartBuilder("Error", table[0][0], table[0][table[0].length - 1], error, "error").build());
            new SwingWrapper<Chart>(charts).displayChartMatrix();
        }
    }

    static private double getNumber(String message, Scanner scanner) {
        double result;
        while (true) {
            System.out.println(message);
            try {
                result = scanner.nextDouble();
                break;
            } catch (InputMismatchException ex) {
                System.out.println("Wrong input");
                System.out.println();
                scanner.next();
            }
        }
        return result;
    }

    static private Splitting chooseSplitting(Scanner scanner) {
        int ans = 0;
        do {
            System.out.println("Choose the splitting:");
            System.out.println("1 - Even splitting");
            System.out.println("2 - Chebyshev's splitting");
            try {
                ans = scanner.nextInt();
            }
            catch (InputMismatchException ex) {
                System.out.println("Wrong input");
                System.out.println();
            }
        } while (ans < 1 || ans > 2);
        if (ans == 1) {
            return Splitting.Even;
        }
        return Splitting.Chebyshev;
    }
}
