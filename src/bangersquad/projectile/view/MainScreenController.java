/**
 * 
 */
package bangersquad.projectile.view;

import bangersquad.projectile.MainApp;
import bangersquad.projectile.ScreenManager;
import javafx.fxml.FXML;

/**
 * @author feilan
 *
 */
public class MainScreenController implements ControlledScreen {

	private ScreenManager screenManager;
	
	public void setScreenManager(ScreenManager manager) {
		screenManager = manager;
	}
	
	@FXML
	private void goToInstructions() {
		screenManager.setScreen(MainApp.INSTRUCTIONS_SCREEN);
	}
	
}
