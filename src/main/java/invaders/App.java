package invaders;
import invaders.configFactory.EasyConfigFactory;
import invaders.configFactory.HardConfigFactory;
import invaders.configFactory.MediumConfigFactory;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import invaders.engine.GameEngine;
import invaders.engine.GameWindow;
import invaders.configFactory.Config;
import javafx.scene.control.Alert.AlertType;
import invaders.Singleton.DifficultyLevelManager;


public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Difficulty Selection");
        alert.setHeaderText(null);
        alert.setContentText("Please select a difficulty:");

        ButtonType easyButton = new ButtonType("Easy");
        ButtonType mediumButton = new ButtonType("Medium");
        ButtonType hardButton = new ButtonType("Hard");

        alert.getButtonTypes().setAll(easyButton, mediumButton, hardButton);

        alert.showAndWait().ifPresent(result -> {
            DifficultyLevelManager difficultyManager = DifficultyLevelManager.getInstance();
            if (result == easyButton) {
                difficultyManager.setDifficulty(new EasyConfigFactory().createConfig());
            } else if (result == mediumButton) {
                difficultyManager.setDifficulty(new MediumConfigFactory().createConfig());
            } else if (result == hardButton) {
                difficultyManager.setDifficulty(new HardConfigFactory().createConfig());
            }
        });

        DifficultyLevelManager difficultyManager = DifficultyLevelManager.getInstance();
        Config selectedConfig = difficultyManager.getDifficulty();

        GameEngine model = new GameEngine(selectedConfig);
        GameWindow window = new GameWindow(model);
        window.run();

        primaryStage.setTitle("Space Invaders");
        primaryStage.setScene(window.getScene());
        primaryStage.show();

        window.run();

    }
}
