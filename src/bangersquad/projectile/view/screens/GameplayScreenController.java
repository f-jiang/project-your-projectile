/**
 * 
 */
package bangersquad.projectile.view.screens;

import java.util.ArrayList;
import java.util.Arrays;

import bangersquad.projectile.MainApp;
import bangersquad.projectile.ScreenManager;
import bangersquad.projectile.model.MathFunction;
import bangersquad.projectile.util.SeriesUtil;
import bangersquad.projectile.util.RandomNumberUtil;
import bangersquad.projectile.view.ScreenController;
import bangersquad.projectile.view.fillintheblanks.FillInTheBlanks;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;
import javafx.scene.chart.NumberAxis;

/**
 * @author feilan
 *
 */
public class GameplayScreenController implements ScreenController {

	private enum TargetOrientation { VERTICAL, HORIZONTAL }
	
	private ScreenManager screenManager;
	private XYChart.Series<Double, Double> target;
	private Point2D bullseye;
	
	private LineChart<Double, Double> chart;
	private NumberAxis xAxis;
	private NumberAxis yAxis;
	
	private MathFunction currentFunction;
	
	private double targetSize = RandomNumberUtil.randInt(-10, 10);
	
	@FXML
	private BorderPane borderPane;
	
	@FXML
	private FillInTheBlanks userInput;
	
	@FXML
	private ProgressBar progressBar;

	@FXML
	private Button launchBtn;
	
	public void setScreenManager(ScreenManager manager) {
		screenManager = manager;
	}
	
	public void onScreenSet() {
		qStart();
	}
	
	@FXML
	private void goToMain() {
		screenManager.setScreen(MainApp.MAIN_SCREEN);
	}	
	
	@SuppressWarnings("unchecked")
	@FXML
	private void initialize() {
		progressBar.setProgress(0);
		
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
			String updatedText;
			
			if (e.getCharacter().matches("\\s")) {
				updatedText = source.getText();
			} else {
				updatedText = source.getText().concat(e.getCharacter());
			}
			
			System.out.println(updatedText);
			
			if (source.getText().length() >= 3 || !updatedText.matches("-*\\d+|-")) {
				e.consume();
				launchBtn.setDisable(true);
			} else if (updatedText.matches("-*\\d+")) {
				boolean isReady = true;
				boolean skipped = false;
				
				for (String text : userInput.getInputTexts()) {
					if (text.equals(source.getText()) && !skipped) {
						skipped = true;
						continue;
					} else if (!text.matches("-*\\d+")){
						isReady = false;
						break;
					}
				}
				
				launchBtn.setDisable(!isReady);
			}
		});		
	}

	private void qStart(){
		boolean startState = false;
		boolean endState = false;
		int startHold = 0;
		int endHold = 0;
		double targetMidX;
		double targetMidY;
		
		clearChart();
		launchBtn.setDisable(true);
		
		ArrayList<MathFunction.Type> enabledTypes = new ArrayList<>(MathFunction.Type.getEnabledValues());
		int rand = RandomNumberUtil.randInt(0, enabledTypes.size() - 1);
		
		currentFunction = new MathFunction(enabledTypes.get(rand), -10, 10);
		
		userInput.update(currentFunction.getPartialEquation(true), "_");
		userInput.setPrompts(currentFunction.getBlankVariables());
		
		double y;
		for (int x = -9; x <= 9; x++){
			y = currentFunction.getValue(x);
			if (y >= -10 && y <= 10 && startState == false){
				startState = true;
				startHold = x;
			}
			if (y >= -10 && y <= 10 && endState == false){
				endState = true;
				endHold = x;
			}
		}
		
		targetMidX = RandomNumberUtil.randInt(startHold, endHold);
		targetMidY = currentFunction.getValue(targetMidX);
		targetSize = RandomNumberUtil.randInt(1, 3);
		positionTarget(targetMidX, targetMidY, targetSize, TargetOrientation.VERTICAL);
		
		System.out.println(targetMidX);
		System.out.println(targetMidY);
		System.out.println(targetSize);
		
	}
	
	@FXML
	private void launch() {
		MathFunction userFunction = new MathFunction(userInput.getText());
		double functionY = userFunction.getValue(bullseye.getX());;		
		
		
		KeyFrame graph = new KeyFrame(Duration.ZERO, (ActionEvent e) -> { 
			plotFunction(userFunction, -10.0, 10.0, 0.1);
			launchBtn.setDisable(true);
		});
		
		KeyFrame check = new KeyFrame(Duration.millis(5000.0), (ActionEvent e) -> {  
			Point2D funcPoint = new Point2D(bullseye.getX(), functionY);
			double dist = funcPoint.distance(bullseye);
			
			if (dist <= targetSize) {
				double maxScore = 0.1;
				double scoreIncrease = maxScore * (1 - (dist / targetSize));
				
				progressBar.setProgress(progressBar.getProgress() + scoreIncrease);
			}

			if (progressBar.getProgress() == 1.0){
				goToMain();
			} else {
				qStart();
			}
		});
		
		Timeline line = new Timeline(graph, check);
		line.play();
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
	
	private void positionTarget(Double x, Double y, Double radius, TargetOrientation orientation) {
		ObservableList<XYChart.Series<Double, Double>> items = chart.getData();
		
		if (items.contains(target)) {
			items.remove(target);
		}
		
		target = new XYChart.Series<Double, Double>();
		target.setName("Target");
		
		switch (orientation) {
		case VERTICAL:
			target.getData().add(new XYChart.Data<>(x, y - radius));
			target.getData().add(new XYChart.Data<>(x, y + radius));
			break;
		case HORIZONTAL:
			target.getData().add(new XYChart.Data<>(x - radius, y));
			target.getData().add(new XYChart.Data<>(x + radius, y));
			break;
		default:
			target.getData().add(new XYChart.Data<>(x, y));
			break;
		}
		
		bullseye = new Point2D(x, y);
		
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