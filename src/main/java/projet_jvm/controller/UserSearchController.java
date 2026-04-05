package projet_jvm.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import projet_jvm.Main;
import projet_jvm.models.users.Player;
import projet_jvm.models.users.User;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class UserSearchController {

    @FXML private TextField searchField;
    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, String> colPseudo;
    @FXML private TableColumn<User, String> colType;
    @FXML private TableColumn<User, String> colStatus;
    @FXML private Button btnViewProfile;

    private ObservableList<User> observableUsers = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colPseudo.setCellValueFactory(new PropertyValueFactory<>("pseudo"));
        colType.setCellValueFactory(new PropertyValueFactory<>("profile"));

        colStatus.setCellValueFactory(cellData -> {
            User u = cellData.getValue();
            String status = "Actif";
            if (u instanceof Player p && p.isBlocked()) {
                status = "Bloqué 🚫";
            }
            return new javafx.beans.property.SimpleStringProperty(status);
        });

        observableUsers.addAll(Main.userManager.getAllUsers());
        userTable.setItems(observableUsers);

        userTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            btnViewProfile.setDisable(newSelection == null);
        });
    }

    @FXML
    void handleSearch() {
        String query = searchField.getText().toLowerCase();

        List<User> filtered = Main.userManager.getAllUsers().stream()
                .filter(u -> u.getPseudo().toLowerCase().contains(query))
                .collect(Collectors.toList());

        observableUsers.setAll(filtered);
    }

    @FXML
    void handleViewProfile(ActionEvent event) throws IOException {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();

        if (selectedUser != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/projet_jvm/UserDetailView.fxml"));
            Parent root = loader.load();

            UserDetailController controller = loader.getController();
            controller.setUser(selectedUser);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        }
    }

    @FXML
    void handleRetour(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/projet_jvm/HomeView.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
    }
}