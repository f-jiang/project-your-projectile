/**
 * 
 */
package bangersquad.projectile.view.screens;

import bangersquad.projectile.MainApp;
import bangersquad.projectile.ScreenManager;
import bangersquad.projectile.model.MathFunction;
import bangersquad.projectile.view.ControlledScreen;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;

/**
 * @author feilan
 *
 */
public class GameplayScreenController implements ControlledScreen {

	private enum TargetOrientation { VERTICAL, HORIZONTAL }
	
	private ScreenManager screenManager;

	@FXML 
	private LineChart<Double, Double> lineChart;
	private LineChart.Series<Double, Double> target;
	
	public void setScreenManager(ScreenManager manager) {
		screenManager = manager;
	}
	
	@FXML
	private void goToMain() {
		screenManager.setScreen(MainApp.MAIN_SCREEN);
	}	
	
	private void plotFunction(MathFunction function, int startX, int endX) {
		
	}
	
	private void removeFunction(MathFunction function) {
		
	}
	
	private void positionTarget(int x, int y, int size, TargetOrientation orientation) {
		
	}
	
}
