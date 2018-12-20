package lab4.view;

import org.knowm.xchart.QuickChart;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.style.markers.SeriesMarkers;
import lab4.spline.Function;

import java.util.ArrayList;

public class ChartBuilder {
    static private final double STEP = 0.01;

    private XYChart chart;

    private double left;
    private double right;

    public ChartBuilder(String title, double l, double r, Function func, String funcName) {
        left = l;
        right = r;
        ArrayList<Double> xs = new ArrayList<>();
        ArrayList<Double> ys = new ArrayList<>();
        for (double i = left; i < right- STEP; i += STEP) {
            xs.add(i);
            ys.add(func.calculate(i));
        }
        chart = QuickChart.getChart(title, "X", "Y", funcName, xs, ys);
    }

    public ChartBuilder AddFunc(Function func, String funcName) {
        ArrayList<Double> xs = new ArrayList<>();
        ArrayList<Double> ys = new ArrayList<>();
        for (double i = left; i < right - STEP; i += STEP) {
            xs.add(i);
            ys.add(func.calculate(i));
        }
        chart.addSeries(funcName, xs, ys).setMarker(SeriesMarkers.NONE);
        return this;
    }

    public Chart build() {
        return chart;
    }

}
