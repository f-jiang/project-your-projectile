/**
 * 
 */
package bangersquad.projectile.view.screens;

import bangersquad.projectile.MainApp;
import bangersquad.projectile.ScreenManager;
import bangersquad.projectile.view.ScreenController;
import javafx.fxml.FXML;

/**
 * @author feilan
 *
 */
public class InstructionsScreenController implements ScreenController {

	private ScreenManager screenManager;
	
	public void setScreenManager(ScreenManager manager) {
		screenManager = manager;
	}
	
	@FXML
	private void goToMain() {
		screenManager.setScreen(MainApp.MAIN_SCREEN);
	}

	@Override
	public void onScreenSet() {	}
	
}
