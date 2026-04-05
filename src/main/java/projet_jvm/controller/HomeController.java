package projet_jvm.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import projet_jvm.Main;

import java.io.IOException;

public class HomeController {

    public Label lblStatus;

    @FXML
    public void initialize() {
        if (Main.connectedUser != null) {
            lblStatus.setText("Connecté en tant que : " +
                    Main.connectedUser.getPseudo() +
                    " (" + Main.connectedUser.getProfile() + ")");
        }
    }

    @FXML
    void ouvrirConnexion(ActionEvent event) throws IOException {
        chargerPageAuth(event, "Connexion");
    }

    @FXML
    void ouvrirInscription(ActionEvent event) throws IOException {
        chargerPageAuth(event, "Inscription");
    }

    private void chargerPageAuth(ActionEvent event, String mode) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/projet_jvm/AuthView.fxml"));
        Parent root = loader.load();

        AuthController controller = loader.getController();
        controller.setMode(mode);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void ouvrirRecherche(ActionEvent event) throws IOException {
            Parent root = FXMLLoader.load(getClass().getResource("/projet_jvm/SearchView.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
    }

    @FXML
    void handleDeconnexion(ActionEvent event) {
        try {
            Main.connectedUser = Main.userManager.login("");

            Parent root = FXMLLoader.load(getClass().getResource("/projet_jvm/HomeView.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur lors de la déconnexion");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public void ouvrirTester(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/projet_jvm/TesterDashboard.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void ouvrirRechercheTest(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/projet_jvm/ConsultTestView.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void handleCompte(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/projet_jvm/ProfileView.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void ouvrirJoueur(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/projet_jvm/UserSearchView.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}