package projet_jvm.controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import projet_jvm.Main;
import projet_jvm.managers.GameManager;
import projet_jvm.models.*;
import projet_jvm.models.users.Player;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class TesterDashboardController {
    @FXML private TableView<GameManager.Mission> tableMissions;
    @FXML private TableColumn<GameManager.Mission, String> colGame, colPlatform;
    @FXML private TableColumn<GameManager.Mission, Integer> colTokens, colMyHours;
    @FXML private TableColumn<GameManager.Mission, Void> colAction;

    @FXML
    public void initialize() {
        colGame.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getGameName()));
        colPlatform.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPlatformName()));
        colTokens.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getTokens()).asObject());

        colMyHours.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    GameManager.Mission mission = getTableRow().getItem();
                    if (Main.connectedUser instanceof Player p) {
                        String key = mission.getGame().getGameKey(mission.getPlatformName());
                        setText(p.getPlayTime(key) + " h");
                    } else {
                        setText("0 h");
                    }
                }
            }
        });

        configurerBoutonTest();
        chargerMissions();
    }

    private void chargerMissions() {
        if (Main.gameManager == null) return;

        List<GameManager.Mission> missions = Main.gameManager.getAllMissions().stream()
                .sorted((m1, m2) -> Integer.compare(m2.getTokens(), m1.getTokens()))
                .collect(Collectors.toList());

        tableMissions.getItems().setAll(missions);
    }

    private void configurerBoutonTest() {
        colAction.setCellFactory(new Callback<>() {
            @Override
            public TableCell<GameManager.Mission, Void> call(TableColumn<GameManager.Mission, Void> param) {
                return new TableCell<>() {
                    private final Button btnRediger = new Button("Rédiger le Test");
                    {
                        btnRediger.setOnAction(event -> {
                            GameManager.Mission mission = getTableRow().getItem();
                            if (mission != null) ouvrirEditeurTest(mission);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                            setGraphic(null);
                        } else {
                            GameManager.Mission mission = getTableRow().getItem();

                            if (Main.connectedUser instanceof Player p) {
                                String key = mission.getGame().getGameKey(mission.getPlatformName());
                                boolean assezDeTemps = p.hasPlayedEnough(key, 5);

                                btnRediger.setDisable(!assezDeTemps);
                                if (!assezDeTemps) {
                                    btnRediger.setTooltip(new Tooltip("5h requises (Actuel: " + p.getPlayTime(key) + "h)"));
                                } else {
                                    btnRediger.setTooltip(null);
                                }
                                setGraphic(btnRediger);
                            } else {
                                setGraphic(null);
                            }
                        }
                    }
                };
            }
        });
    }

    private void ouvrirEditeurTest(GameManager.Mission mission) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/projet_jvm/TestEditorView.fxml"));
            Parent root = loader.load();

            TestEditorController controller = loader.getController();
            controller.setMission(mission);

            Stage stage = new Stage();
            stage.setTitle("Rédiger un test - " + mission.getGameName());
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            chargerMissions();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRetour(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/projet_jvm/HomeView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}