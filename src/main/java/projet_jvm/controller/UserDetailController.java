package projet_jvm.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import projet_jvm.Main;
import projet_jvm.models.users.Player;
import projet_jvm.models.users.User;

import java.io.IOException;
import java.util.stream.Collectors;

public class UserDetailController {
    @FXML
    private Label lblUserTitle, lblType, lblTotalTime, lblStats, lblEvaluationKarma;
    @FXML private VBox vboxSpecialInfo;
    @FXML private HBox adminActions;
    @FXML private ListView<String> listGames;
    @FXML private Button btnBlock;

    private User targetUser;

    public void setUser(User user) {
        this.targetUser = user;
        User viewer = Main.connectedUser;

        lblUserTitle.setText("Profil de " + user.getPseudo());
        lblType.setText(user.getProfile().toString());

        if (user instanceof Player p) {
            lblTotalTime.setText(p.getTotalPlayTime() + " h");
            lblStats.setText(p.getTotalEvaluationsCount() + " évaluations postées");

            boolean isPrivileged = (viewer.getProfile() == User.UserProfile.ADMIN || viewer.getProfile() == User.UserProfile.TESTER);
            vboxSpecialInfo.setVisible(isPrivileged);
            vboxSpecialInfo.setManaged(isPrivileged);

            if (isPrivileged) {
                lblEvaluationKarma.setText("Karma : 👍 " + p.getEvaluationsPositiveCount() + " | 👎 " + p.getEvaluationsNegativeCount());

                listGames.getItems().setAll(p.getGamesSortedByTime().stream()
                        .map(e -> e.getKey() + " (" + e.getValue() + "h)")
                        .collect(Collectors.toList()));
            }

            boolean isAdmin = (viewer.getProfile() == User.UserProfile.ADMIN);
            adminActions.setVisible(isAdmin);
            adminActions.setManaged(isAdmin);

            if (isAdmin) {
                btnBlock.setText(p.isBlocked() ? "Débloquer" : "Bloquer");
            }
        }
    }

    @FXML
    void handlePromote() {
        if (targetUser.getProfile() == User.UserProfile.PLAYER) targetUser.setProfile(User.UserProfile.TESTER);
        else if (targetUser.getProfile() == User.UserProfile.TESTER) targetUser.setProfile(User.UserProfile.ADMIN);
        setUser(targetUser);
    }

    @FXML
    void handleToggleBlock() {
        if (targetUser instanceof Player p) {
            p.setBlocked(!p.isBlocked());
            setUser(targetUser);
        }
    }

    @FXML
    void handleRetour(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/projet_jvm/UserSearchView.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    @FXML
    public void handleDelete(ActionEvent event) throws IOException {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Supprimer définitivement votre compte ?");
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    Main.userManager.unregisterPlayer(Main.connectedUser.getPseudo());
                    Main.connectedUser = Main.userManager.login("");

                    System.out.println("Compte supprimé.");

                    Parent root = FXMLLoader.load(getClass().getResource("/projet_jvm/UserSearchView.fxml"));
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
}
