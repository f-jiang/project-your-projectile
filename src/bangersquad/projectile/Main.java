package bangersquad.projectile;
	
import bangersquad.projectile.controller.ScreenManager;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	
	public static final String MAIN_SCREEN = "main";
	public static final String INSTRUCTIONS_SCREEN = "instructions";
	public static final String SETTINGS_SCREEN = "settings";
	public static final String CREDITS_SCREEN = "credits";
	public static final String GAMEPLAY_SCREEN = "gameplay";

	public static final String MAIN_SCREEN_FXML = "main.fxml";
	public static final String INSTRUCTIONS_SCREEN_FXML = "instructions.fxml";
	public static final String SETTINGS_SCREEN_FXML = "settings.fxml";
	public static final String CREDITS_SCREEN_FXML = "credits.fxml";
	public static final String GAMEPLAY_SCREEN_FXML = "gameplay.fxml";	
	
	@Override
	public void start(Stage primaryStage) {		
		ScreenManager root = new ScreenManager(); 
		
		Scene scene = new Scene(root, 500, 300);
		
		primaryStage.setTitle("Project your Projectile!");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
