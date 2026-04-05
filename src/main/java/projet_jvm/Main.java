package projet_jvm;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import projet_jvm.controller.HomeController;
import projet_jvm.managers.GameManager;
import projet_jvm.managers.UserManager;
import projet_jvm.models.users.User;

import java.io.IOException;

public class Main extends Application {
    public static final GameManager gameManager = new GameManager();
    public static final UserManager userManager = new UserManager();
    public static User connectedUser = userManager.login("");

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HomeController.class.getResource("/projet_jvm/HomeView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Notation");
        stage.setScene(scene);
        stage.show();
        gameManager.loadGamesFromCSV();
    }

    public static void main(String[] args) {
        launch();
    }
}