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
import projet_jvm.models.VideoGame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConsultTestController {

    @FXML
    private TextField searchField;
    @FXML private ComboBox<String> genreFilter;
    @FXML private TableView<VideoGame> gamesTable;
    @FXML private TableColumn<VideoGame, String> colName;
    @FXML private TableColumn<VideoGame, String> colGenre;
    @FXML private TableColumn<VideoGame, String> colPublisher;
    @FXML private TableColumn<VideoGame, String> colRating;
    @FXML private Button btnDetails;

    private ObservableList<VideoGame> observableGames = FXCollections.observableArrayList();

    @FXML private ComboBox<String> searchMode;

    @FXML
    public void initialize() {
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colGenre.setCellValueFactory(new PropertyValueFactory<>("genre"));
        colPublisher.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        colRating.setCellValueFactory(new PropertyValueFactory<>("rating"));

        searchMode.setItems(FXCollections.observableArrayList("Nom", "Genre", "Éditeur"));
        searchMode.setValue("Nom");

        observableGames.addAll(Main.gameManager.getAllGames());
        gamesTable.setItems(observableGames);

        gamesTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            btnDetails.setDisable(newSelection == null);
        });
    }

    @FXML
    void handleSearch() {
        String query = searchField.getText().toLowerCase();
        String mode = searchMode.getValue();
        List<VideoGame> results;

        if (query.isEmpty()) {
            results = new ArrayList<>(Main.gameManager.getAllGames());
        } else {
            switch (mode) {
                case "Genre":
                    results = Main.gameManager.searchByGenre(query);
                    break;
                case "Éditeur":
                    results = Main.gameManager.searchByPublisher(query);
                    break;
                case "Nom":
                default:
                    results = Main.gameManager.searchByName(query);
                    break;
            }
        }
        observableGames.setAll(results);
    }

    @FXML
    void handleVoirDetails(ActionEvent event) throws IOException {
        VideoGame selectedGame = gamesTable.getSelectionModel().getSelectedItem();
        if (selectedGame != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/projet_jvm/ConsultTestDetailView.fxml"));
            Parent root = loader.load();

            ConsultTestDetailController controller = loader.getController();
            controller.setGame(selectedGame);

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
