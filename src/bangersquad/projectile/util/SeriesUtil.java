package bangersquad.projectile.util;

import bangersquad.projectile.model.MathFunction;
import bangersquad.projectile.util.calculator.Calculator;
import javafx.scene.chart.XYChart;

public class SeriesUtil {

	public static XYChart.Series<Double, Double> getFunctionSeries(MathFunction function, String name, Double startX, Double endX, Double increment) {
		XYChart.Series<Double, Double> points = new XYChart.Series<>();
		String equation = function.getEquation();
		Double y;
		
		points.setName(name);
		
		for (Double x = startX; x < endX; x += increment) {
			y = Double.valueOf(Calculator.eval(Calculator.plugIn(equation, x), false));
			points.getData().add(new XYChart.Data<>(x, y));
		}
		
		return points;
	}

	public static XYChart.Series<Double, Double> getFunctionSeries(String function, String name, Double startX, Double endX) {
		XYChart.Series<Double, Double> points = new XYChart.Series<>();
		Double y;
		
		points.setName(name);
		
		for (Double x = startX; x < endX; x += 0.01) {
			y = Double.valueOf(Calculator.eval(Calculator.plugIn(function, x), false));
			points.getData().add(new XYChart.Data<>(x, y));
		}
		
		return points;
	}
		
}
