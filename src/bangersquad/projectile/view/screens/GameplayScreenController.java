/**
 * 
 */
package bangersquad.projectile.view.screens;

import java.util.Arrays;

import bangersquad.projectile.MainApp;
import bangersquad.projectile.ScreenManager;
import bangersquad.projectile.model.MathFunction;
import bangersquad.projectile.model.MathFunctionType;
import bangersquad.projectile.util.RandomNumberUtil;
import bangersquad.projectile.util.calculator.Calculator;
import bangersquad.projectile.view.ControlledScreen;
import bangersquad.projectile.view.fillintheblanks.FillInTheBlanks;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

/**
 * @author feilan
 *
 */
public class GameplayScreenController implements ControlledScreen {

	private enum TargetOrientation { VERTICAL, HORIZONTAL }
	
	private ScreenManager screenManager;
	private XYChart.Series<Double, Double> target;
	private MathFunction currentFunction;	// test
	
	@FXML 
	private LineChart<Double, Double> lineChart;
	
	@FXML
	private FillInTheBlanks userInput;
	
	public void setScreenManager(ScreenManager manager) {
		screenManager = manager;
	}
	
	@FXML
	private void goToMain() {
		screenManager.setScreen(MainApp.MAIN_SCREEN);
	}	
	
	@FXML
	private void initialize() {	// TODO: add styling to the graph
		lineChart.getXAxis().setAutoRanging(true);
		lineChart.getYAxis().setAutoRanging(true);
		lineChart.setCreateSymbols(false);
	}
	
	@FXML
	private void draw() {	// temporary test function
		double targetX = RandomNumberUtil.getRandomInt(-10, 10);
		double targetY = RandomNumberUtil.getRandomInt(-10, 10);
		double targetSize = RandomNumberUtil.getRandomInt(-10, 10);
		
		positionTarget(targetX, targetY, targetSize, Math.random() > 0.5 ? TargetOrientation.HORIZONTAL : TargetOrientation.VERTICAL);

		if (currentFunction != null) {
			removeFunction(currentFunction);
		}
		currentFunction = new MathFunction(MathFunctionType.QUADRATIC_STANDARD_FORM);
		plotFunction(currentFunction, -10.0, 10.0);
		
		userInput.update(currentFunction.getSplitPartialEquation(false, true), "_");
		userInput.setPrompts(currentFunction.getMissingVariables());
	}
	
	private void plotFunction(MathFunction function, Double startX, Double endX) {	// TODO: add a left to right animation for this
		XYChart.Series<Double, Double> points = new XYChart.Series<>();
		String equation = function.getEquation(false);
		Double y;
		
		points.setName(equation);
		
		for (Double x = startX; x < endX; x += 0.1) {
//			System.out.println(Calculator.plugIn(equation, x));	// TODO: use bigdecimal instead for the calculator
			y = Double.valueOf(Calculator.eval(Calculator.plugIn(equation, x), false));
			System.out.println(y);
			points.getData().add(new XYChart.Data<>(x, y));
		}
		
		lineChart.getData().add(points);
	}
	
	private void removeFunction(MathFunction function) {		
		for (XYChart.Series<Double, Double> item : lineChart.getData()) {
			if (item.getName().equals(function.getEquation(false))) {
				lineChart.getData().remove(item);
				break;
			}
		}
	}
	
	private void clearGraph() {
		lineChart.getData().clear();
	}
	
	private void positionTarget(Double x, Double y, Double size, TargetOrientation orientation) {
		ObservableList<XYChart.Series<Double, Double>> items = lineChart.getData();
		
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
	
}
