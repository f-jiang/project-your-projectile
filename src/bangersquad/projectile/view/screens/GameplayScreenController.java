/**
 * 
 */
package bangersquad.projectile.view.screens;

import java.util.List;

import bangersquad.projectile.MainApp;
import bangersquad.projectile.ScreenManager;
import bangersquad.projectile.model.MathFunction;
import bangersquad.projectile.util.SeriesUtil;
import bangersquad.projectile.util.calculator.Calculator;
import bangersquad.projectile.view.ControlledScreen;
import bangersquad.projectile.view.fillintheblanks.FillInTheBlanks;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.chart.NumberAxis;

/**
 * @author feilan
 *
 */
public class GameplayScreenController implements ControlledScreen {

	private enum TargetOrientation { VERTICAL, HORIZONTAL }
	
	private ScreenManager screenManager;
	private XYChart.Series<Double, Double> target;
	
	private LineChart<Double, Double> chart;
	private NumberAxis xAxis;
	private NumberAxis yAxis;
	
	@FXML
	private AnchorPane lineChartPane;
	
	@FXML
	private BorderPane borderPane;
	
	@FXML
	private FillInTheBlanks userInput;
	
	public void setScreenManager(ScreenManager manager) {
		screenManager = manager;
	}
	
	@FXML
	private void goToMain() {
		screenManager.setScreen(MainApp.MAIN_SCREEN);
	}	
	
	@SuppressWarnings("unchecked")
	@FXML
	private void initialize() {	// TODO: add styling to the graph
		xAxis = new NumberAxis("", -10, 10, 1);
		xAxis.setAutoRanging(false);
		xAxis.setAnimated(true);
		
		yAxis = new NumberAxis("", -10, 10, 1);
		yAxis.setAutoRanging(false);
		yAxis.setAnimated(true);
		
		chart = new LineChart(xAxis, yAxis);
		chart.setCreateSymbols(false);
		chart.setAnimated(true);
		chart.setLegendVisible(false);
		
		borderPane.setCenter(chart);
	}
	
	private void plotFunction(MathFunction function, Double startX, Double endX) {	// TODO: add a left to right animation for this
		// TODO: add a left to right animation for this
		String equation = function.getEquation(false);
		XYChart.Series<Double, Double> series = SeriesUtil.getFunctionSeries(function, equation, startX, endX); 
		chart.getData().add(series);
	}
	
	private void removeFunction(MathFunction function) {		
		for (XYChart.Series<Double, Double> item : chart.getData()) {
			if (item.getName().equals(function.getEquation(false))) {
				chart.getData().remove(item);
				break;
			}
		}
	}
	
	private void clearChart() {
		chart.getData().clear();
	}
	
	private void positionTarget(Double x, Double y, Double size, TargetOrientation orientation) {
		ObservableList<XYChart.Series<Double, Double>> items = chart.getData();
		
		if (items.contains(target)) {
			items.remove(target);
		}
		
		target = new XYChart.Series<Double, Double>();
		target.setName("Target");
		target.getData().add(new XYChart.Data<>(x, y));
		
		switch (orientation) {
		case VERTICAL:
			target.getData().add(new XYChart.Data<>(x, y + size));
			break;
		case HORIZONTAL:
			target.getData().add(new XYChart.Data<>(x + size, y));
			break;
		default:
			target.getData().add(new XYChart.Data<>(x, y));
			break;
		}
		
		items.add(target);
	}
	
	private void setXBounds(double lower, double upper) {
		xAxis.setLowerBound(lower);
		xAxis.setUpperBound(upper);
	}

	private void setYBounds(double lower, double upper) {
		yAxis.setLowerBound(lower);
		yAxis.setUpperBound(upper);
	}
	
}
