package projet_jvm.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import projet_jvm.models.*;
import java.util.List;

public class ConsultTestDetailController {
    @FXML private Label labelTitle, detailsTitle, detailsScore, detailsMeta, detailsText, labelPlaceholder;
    @FXML private ListView<GamePlatform> listPlatforms;
    @FXML private TableView<Test> tableTests;
    @FXML private TableColumn<Test, String> colAuthor, colScore;
    @FXML private VBox contentBox;
    @FXML private ListView<String> listStrengths, listWeaknesses;

    @FXML
    public void initialize() {
        colAuthor.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getAuthor().getPseudo()));
        colScore.setCellValueFactory(d -> new SimpleStringProperty(String.format("%.1f/10", d.getValue().getAverageScore())));

        listPlatforms.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV != null) {
                tableTests.getItems().setAll(newV.getTests());
                contentBox.setVisible(false);
                labelPlaceholder.setVisible(true);
            }
        });

        tableTests.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV != null) afficherDetailsTest(newV);
        });
    }

    public void setGame(VideoGame game) {
        labelTitle.setText("Tests pour : " + game.getName());
        listPlatforms.getItems().setAll(game.getAllPlatforms());
        if (!listPlatforms.getItems().isEmpty()) listPlatforms.getSelectionModel().selectFirst();
    }

    private void afficherDetailsTest(Test test) {
        labelPlaceholder.setVisible(false);
        contentBox.setVisible(true);

        detailsTitle.setText("Test de " + test.getAuthor().getPseudo());
        detailsScore.setText(String.format("%.1f/10", test.getAverageScore()));
        detailsMeta.setText("Version : " + test.getBuildVersion() + " | Date : " + test.getDate());
        detailsText.setText(test.getText());

        listStrengths.getItems().setAll(test.getStrengths());
        listWeaknesses.getItems().setAll(test.getWeaknesses());
    }

    @FXML
    void handleRetour(javafx.event.ActionEvent event) throws java.io.IOException {
        javafx.scene.Parent root = javafx.fxml.FXMLLoader.load(getClass().getResource("/projet_jvm/ConsultTestView.fxml"));
        javafx.stage.Stage stage = (javafx.stage.Stage)((javafx.scene.Node)event.getSource()).getScene().getWindow();
        stage.setScene(new javafx.scene.Scene(root));
    }
}