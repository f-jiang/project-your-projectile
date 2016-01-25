/**
 * 
 */
package bangersquad.projectile.view.screens;

import bangersquad.projectile.MainApp;
import bangersquad.projectile.ScreenManager;
import bangersquad.projectile.model.MathFunction;
import bangersquad.projectile.util.SeriesUtil;
import bangersquad.projectile.util.RandomNumberUtil;
import bangersquad.projectile.view.ControlledScreen;
import bangersquad.projectile.view.fillintheblanks.FillInTheBlanks;
import javafx.animation.AnimationTimer;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
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
	private Point2D bullseye;
	
	private LineChart<Double, Double> chart;
	private NumberAxis xAxis;
	private NumberAxis yAxis;
	
	private MathFunction currentFunction;
	private int i = 0; //test
	
	@FXML
	private BorderPane borderPane;
	
	@FXML
	private FillInTheBlanks userInput;
	
	@FXML
	private ProgressBar progressBar;
	
	public void setScreenManager(ScreenManager manager) {
		screenManager = manager;
	}
	
	@FXML
	private void goToMain() {
		screenManager.setScreen(MainApp.MAIN_SCREEN);
	}	
	
	@SuppressWarnings("unchecked")
	@FXML
	private void initialize() {
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
		
		userInput.setInputFilter((KeyEvent e) -> {
			TextField source = (TextField) e.getSource();
			
			if (source.getText().length() >= 4 || !e.getCharacter().matches("\\d")) {
				e.consume();
			} 
		});
	}

	@FXML
	private void test() {
		int startX = -10;
		int endX = 10;
		
		double targetX = RandomNumberUtil.randInt(-10, 10);
		double targetY = RandomNumberUtil.randInt(-10, 10);
		double targetSize = RandomNumberUtil.randInt(-10, 10);
		i = ++i % MathFunction.Type.values().length;
		
		positionTarget(targetX, targetY, targetSize, Math.random() > 0.5 ? TargetOrientation.HORIZONTAL : TargetOrientation.VERTICAL);

		if (currentFunction != null) {
			removeFunction(currentFunction);
		}
		currentFunction = new MathFunction(MathFunction.Type.values()[i], startX, endX);
		plotFunction(currentFunction, (double) startX, (double) endX, 0.1);
		
		userInput.update(currentFunction.getSplitPartialEquation(true), "_");
		userInput.setPrompts(currentFunction.getBlankVariables());
	}	
	
	private void plotFunction(MathFunction function, Double startX, Double endX, Double increment) {
		String equation = function.getEquation();
		ObservableList<XYChart.Data<Double, Double>> dataPoints = 
			SeriesUtil.getFunctionSeries(function, equation, startX, endX, increment).getData();
		XYChart.Series<Double, Double> plottedSeries = new XYChart.Series<>();
		AnimationTimer plotAnimation = new AnimationTimer() {			
			@Override
			public void handle(long now) {
				if (dataPoints.isEmpty()) {
					stop();
				} else {
					XYChart.Data<Double, Double> dataPoint = dataPoints.remove(0);
					plottedSeries.getData().add(dataPoint);
				}
			}
		};
		
		plottedSeries.setName(function.getEquation());
		chart.getData().add(plottedSeries);
		plotAnimation.start();
	}
	
	private void removeFunction(MathFunction function) {		
		for (XYChart.Series<Double, Double> item : chart.getData()) {
			if (item.getName().equals(function.getEquation())) {
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
			bullseye = new Point2D(x, y + size / 2);
			break;
		case HORIZONTAL:
			target.getData().add(new XYChart.Data<>(x + size, y));
			bullseye = new Point2D(x + size / 2, y);
			break;
		default:
			target.getData().add(new XYChart.Data<>(x, y));
			bullseye = new Point2D(x, y);
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
