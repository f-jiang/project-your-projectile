/**
 * 
 */
package bangersquad.projectile.view.screens;

import bangersquad.projectile.MainApp;
import bangersquad.projectile.ScreenManager;
import bangersquad.projectile.view.ControlledScreen;
import javafx.fxml.FXML;

/**
 * @author feilan
 *
 */
public class SettingsScreenController implements ControlledScreen {

	private ScreenManager screenManager;
	
	public void setScreenManager(ScreenManager manager) {
		screenManager = manager;
	}
	
	@FXML
	private void goToMain() {
		screenManager.setScreen(MainApp.MAIN_SCREEN);
	}
	
}
