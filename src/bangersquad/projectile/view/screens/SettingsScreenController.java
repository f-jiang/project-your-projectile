/**
 * 
 */
package bangersquad.projectile.view.screens;

import bangersquad.projectile.MainApp;
import bangersquad.projectile.ScreenManager;
import bangersquad.projectile.model.MathFunction;
import bangersquad.projectile.view.ControlledScreen;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import org.controlsfx.control.CheckListView;

/**
 * @author feilan
 *
 */
public class SettingsScreenController implements ControlledScreen {

	private ScreenManager screenManager;

	@FXML
	private VBox vbox;
	private CheckListView<MathFunction.Type> typeList;


	public void setScreenManager(ScreenManager manager) {
		screenManager = manager;
	}

	@FXML
	private void goToMain() {
		screenManager.setScreen(MainApp.MAIN_SCREEN);
	}

	@FXML
	private void initialize() {
		typeList = new CheckListView<>(FXCollections.observableArrayList(MathFunction.Type.values()));
		typeList.getCheckModel().getCheckedItems().addListener(new ListChangeListener<MathFunction.Type>() {
			public void onChanged(ListChangeListener.Change<? extends MathFunction.Type> c) {
				c.next();
				if (c.wasAdded()) {
					c.getAddedSubList().get(0).setEnabled(true);
				} else if (c.wasRemoved()) {
					c.getRemoved().get(0).setEnabled(false);
				}
			}
		});

		for (MathFunction.Type t : MathFunction.Type.values()) {
			if (t.isEnabled()) {
				typeList.getCheckModel().check(t);
			}
		}

		vbox.getChildren().add(1, typeList);
		VBox.setMargin(typeList, new Insets(10));
		VBox.setVgrow(typeList, Priority.ALWAYS);
	}

}
