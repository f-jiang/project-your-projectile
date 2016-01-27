/**
 * 
 */
package bangersquad.projectile.view.screens;

import bangersquad.projectile.MainApp;
import bangersquad.projectile.ScreenManager;
import bangersquad.projectile.util.SeriesUtil;
import bangersquad.projectile.view.ScreenController;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

/**
 * @author feilan
 *
 */
public class MainScreenController implements ScreenController {

	private ScreenManager screenManager;

	public void setScreenManager(ScreenManager manager) {
		screenManager = manager;
	}
	
	@FXML
	private void goToInstructions() {
		screenManager.setScreen(MainApp.INSTRUCTIONS_SCREEN);
	}

	@FXML
	private void goToGameplay() {
		screenManager.setScreen(MainApp.GAMEPLAY_SCREEN);
	}
	
	@FXML
	private void goToSettings() {
		screenManager.setScreen(MainApp.SETTINGS_SCREEN);
	}
	
	@FXML
	private void goToCredits() {
		screenManager.setScreen(MainApp.CREDITS_SCREEN);
	}

	private void initialize() {
		
	}

	@Override
	public void onScreenSet() {
		// TODO Auto-generated method stub
		
	}
	
}
