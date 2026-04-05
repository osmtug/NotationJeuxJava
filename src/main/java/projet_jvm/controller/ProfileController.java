package projet_jvm.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import projet_jvm.Main;
import projet_jvm.models.users.Player;
import projet_jvm.models.users.User;

import java.io.IOException;
import java.util.stream.Collectors;

public class ProfileController {
    @FXML private Label lblPseudo, lblRank, lblTotalTime, lblTokens;
    @FXML private ListView<String> listOwnedGames;

    @FXML
    public void initialize() {
        User user = Main.connectedUser;
        lblPseudo.setText(user.getPseudo());
        lblRank.setText(user.getProfile().toString());

        if (user instanceof Player p) {
            lblTotalTime.setText(p.getTotalPlayTime() + " heures");
            lblTokens.setText(p.getTokens() + " jetons");

            if (user.getProfile() == User.UserProfile.ADMIN || user.getProfile() == User.UserProfile.TESTER) {
                listOwnedGames.getItems().setAll(
                        p.getGamesSortedByTime().stream()
                                .map(e -> e.getKey() + " : " + e.getValue() + "h")
                                .collect(Collectors.toList())
                );
            } else {
                listOwnedGames.getItems().setAll(
                        p.getGamePlayTime().keySet().stream().collect(Collectors.toList())
                );
            }
        }
    }

    @FXML
    void handleDeleteAccount(ActionEvent event) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Supprimer définitivement votre compte ?");
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                Main.userManager.unregisterPlayer(Main.connectedUser.getPseudo());
                Main.connectedUser = Main.userManager.login("");

                System.out.println("Compte supprimé.");
                Parent root = null;

                    root = FXMLLoader.load(getClass().getResource("/projet_jvm/HomeView.fxml"));
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                catch (IllegalArgumentException e) {
                    javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setHeaderText("Erreur lors de la suppression");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                }
            }
        });
    }

    public void handleRetour(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/projet_jvm/HomeView.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
