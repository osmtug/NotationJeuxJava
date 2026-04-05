package projet_jvm.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import projet_jvm.models.users.Guest;
import projet_jvm.models.users.Player;
import projet_jvm.models.users.User;

import java.io.IOException;

import static projet_jvm.Main.connectedUser;
import static projet_jvm.Main.userManager;

public class AuthController {

    @FXML private Label lblTitre;
    @FXML private TextField pseudoField;
    @FXML private Button btnValider;

    public void setMode(String mode) {
        lblTitre.setText(mode);
        btnValider.setText(mode);
    }

    @FXML
    void handleValider(ActionEvent event) {
        String pseudo = pseudoField.getText();
        String mode = lblTitre.getText();

        try {
            if (mode.equals("Connexion")){
                User user = pseudo.isEmpty() ?
                        new Guest() :
                        userManager.login(pseudo);

                connectedUser = user;
            } else {
                Player player = userManager.registerPlayer(pseudo);
                System.out.println("[OK] Compte cree pour " + pseudo);
                System.out.println("  Vous avez recu 3 jetons pour voter les tests!");
                connectedUser = userManager.login(pseudo);
            }
            handleRetour(event);
        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Une erreur est survenue lors de : " + mode);
            alert.setContentText(e.getMessage());

            alert.showAndWait();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Une erreur est survenue lors de : " + mode);
            alert.setContentText(e.getMessage());
        }
    }

    @FXML
    void handleRetour(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/projet_jvm/HomeView.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}