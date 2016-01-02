package bangersquad.projectile;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;

import bangersquad.projectile.model.MathFunction;
import bangersquad.projectile.model.MathFunctionType;

import java.util.Scanner;

public class MainApp extends Application {
	
	public static final String MAIN_SCREEN = "main";
	public static final String INSTRUCTIONS_SCREEN = "instructions";
	public static final String SETTINGS_SCREEN = "settings";
	public static final String CREDITS_SCREEN = "credits";
	public static final String GAMEPLAY_SCREEN = "gameplay";

	public static final String MAIN_SCREEN_FXML = "view/MainScreen.fxml";
	public static final String INSTRUCTIONS_SCREEN_FXML = "view/InstructionsScreen.fxml";
	public static final String SETTINGS_SCREEN_FXML = "view/SettingsScreen.fxml";
	public static final String CREDITS_SCREEN_FXML = "view/CreditsScreen.fxml";
	public static final String GAMEPLAY_SCREEN_FXML = "view/GameplayScreen.fxml";	
	
	@Override
	public void start(Stage primaryStage) {		
		ScreenManager root = new ScreenManager(); 
		root.loadScreen(MAIN_SCREEN, MAIN_SCREEN_FXML);
		root.loadScreen(INSTRUCTIONS_SCREEN, INSTRUCTIONS_SCREEN_FXML);
		root.setScreen(MAIN_SCREEN);
		
		Scene scene = new Scene(root, 500, 300);
		
		primaryStage.setTitle("Project your Projectile!");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
//		launch(args);
		Scanner s = new Scanner(System.in);
		MathFunction m;
		
		while (true) {
			m = new MathFunction(MathFunctionType.QUADRATIC_STANDARD_FORM);
			System.out.println(m.getEquation());
			System.out.println(m.getPartialEquation());
			s.nextLine();
		}
	}
	
}
