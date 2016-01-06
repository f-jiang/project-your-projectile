package bangersquad.projectile;
	
import bangersquad.projectile.util.calculator.Calculator;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class MainApp extends Application {
	
	public static final String MAIN_SCREEN = "main";
	public static final String INSTRUCTIONS_SCREEN = "instructions";
	public static final String SETTINGS_SCREEN = "settings";
	public static final String CREDITS_SCREEN = "credits";
	public static final String GAMEPLAY_SCREEN = "gameplay";

	public static final String MAIN_SCREEN_FXML = "view/screens/MainScreen.fxml";
	public static final String INSTRUCTIONS_SCREEN_FXML = "view/screens/InstructionsScreen.fxml";
	public static final String SETTINGS_SCREEN_FXML = "view/screens/SettingsScreen.fxml";
	public static final String CREDITS_SCREEN_FXML = "view/screens/CreditsScreen.fxml";
	public static final String GAMEPLAY_SCREEN_FXML = "view/screens/GameplayScreen.fxml";	
	
	@Override
	public void start(Stage primaryStage) {		
		ScreenManager root = new ScreenManager(); 
		root.loadScreen(MAIN_SCREEN, MAIN_SCREEN_FXML);
		root.loadScreen(INSTRUCTIONS_SCREEN, INSTRUCTIONS_SCREEN_FXML);
		root.loadScreen(SETTINGS_SCREEN, SETTINGS_SCREEN_FXML);
		root.loadScreen(CREDITS_SCREEN, CREDITS_SCREEN_FXML);
		root.loadScreen(GAMEPLAY_SCREEN, GAMEPLAY_SCREEN_FXML);
//		root.setScreen(MAIN_SCREEN);
		root.setScreen(GAMEPLAY_SCREEN);
		
		Scene scene = new Scene(root, 500, 300);
		
		primaryStage.setTitle("Project your Projectile!");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
//		Calculator.test();
	}
	
}
