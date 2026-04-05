package projet_jvm.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import projet_jvm.Main;
import projet_jvm.models.VideoGame;
import projet_jvm.models.GamePlatform;
import projet_jvm.models.Evaluation;
import projet_jvm.models.users.Player;
import projet_jvm.models.users.User;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class GameDetailController {
    public TextField fieldHours;
    public TableColumn colActions;
    @FXML private Label labelTitle, labelInfo;
    @FXML private ListView<GamePlatform> listPlatforms;
    @FXML private TableView<Evaluation> tableEvaluations;

    @FXML private TableColumn<Evaluation, String> colPlayer;
    @FXML private TableColumn<Evaluation, Double> colRating;
    @FXML private TableColumn<Evaluation, String> colComment;
    @FXML private Label labelTotalTokens, labelMyTokens;
    @FXML private Slider sliderScore;
    @FXML private Label labelScoreValue;
    @FXML private TextArea areaComment;

    private VideoGame currentGame;

    @FXML
    public void initialize() {
        colPlayer.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getAuthor().getPseudo()));

        colRating.setCellValueFactory(new PropertyValueFactory<>("globalScore"));

        colComment.setCellValueFactory(new PropertyValueFactory<>("text"));

        listPlatforms.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                updateEvaluationsTable(newVal);
            }
        });

        sliderScore.valueProperty().addListener((obs, oldVal, newVal) -> {
            labelScoreValue.setText(newVal.intValue() + "/10");
        });

        configurerColonneActions();
    }

    /**
     * Reçoit le jeu depuis la page de recherche
     */
    public void setGame(VideoGame game) {
        this.currentGame = game;
        labelTitle.setText(game.getName());
        labelInfo.setText(game.getGenre() + " | " + game.getPublisher());

        listPlatforms.getItems().setAll(game.getAllPlatforms());

        if (!listPlatforms.getItems().isEmpty()) {
            listPlatforms.getSelectionModel().selectFirst();
        }
        GamePlatform selected = listPlatforms.getItems().getFirst();
        rafraichirInfosTokens(selected);
    }

    /**
     * Met à jour la table en utilisant ta méthode de tri personnalisée
     */
    private void updateEvaluationsTable(GamePlatform platform) {
        List<Evaluation> evals = platform.getEvaluationsSorted();

        List<Evaluation> visibleEvals = evals.stream()
                .filter(e -> !e.isCensored())
                .collect(Collectors.toList());

        tableEvaluations.getItems().setAll(visibleEvals);
    }

    @FXML
    private void handleDeclareTime(ActionEvent event) {
        GamePlatform selectedPlatform = listPlatforms.getSelectionModel().getSelectedItem();

        if (!(Main.connectedUser instanceof Player)) {
            afficherAlerte("Erreur", "Vous devez être connecté en tant que joueur pour déclarer du temps de jeu.");
            return;
        }

        if (selectedPlatform == null) {
            afficherAlerte("Sélection manquante", "Veuillez sélectionner une plateforme dans la liste.");
            return;
        }

        try {
            int hours = Integer.parseInt(fieldHours.getText());
            Player player = (Player) Main.connectedUser;

            String key = currentGame.getGameKey(selectedPlatform.getPlatform());

            player.addGamePlayTime(key, hours);

            fieldHours.clear();


        } catch (IllegalArgumentException e) {
            afficherAlerte("Format invalide", "Veuillez entrer un nombre d'heures valide (entier).");
        }
    }

    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void rafraichirInfosTokens(GamePlatform platform) {
        labelTotalTokens.setText("Jetons totaux sur ce support : " + platform.getTotalTokensBidded());

        if (Main.connectedUser instanceof Player player) {
            labelMyTokens.setText("(Il vous reste " + player.getTokens() + " jetons)");
        } else {
            labelMyTokens.setText("");
        }
    }

    @FXML
    private void handleSendEvaluation(ActionEvent event) {
        try {
            if (!(Main.connectedUser instanceof Player player)) {
                throw new IllegalStateException("Seuls les joueurs connectés peuvent évaluer.");
            }

            GamePlatform selected = listPlatforms.getSelectionModel().getSelectedItem();
            if (selected == null) {
                throw new IllegalArgumentException("Sélectionnez une plateforme d'abord.");
            }

            String key = currentGame.getGameKey(selected.getPlatform());
            if (!player.getGamePlayTime().containsKey(key)) {
                throw new IllegalStateException("Vous devez déclarer posséder le jeu sur " + selected.getPlatform() + " avant de l'évaluer.");
            }

            Evaluation newEval = new Evaluation(
                    areaComment.getText(),
                    "1.0",
                    sliderScore.getValue(),
                    player
            );

            selected.addEvaluation(newEval);

            updateEvaluationsTable(selected);
            player.addTotalEvaluationsCount(1);
            areaComment.clear();
            System.out.println("Évaluation ajoutée !");

        } catch (IllegalStateException | IllegalArgumentException e) {
            afficherAlerte("Impossible d'évaluer", e.getMessage());
        }
    }

    @FXML
    private void handlePlaceToken(ActionEvent event) {
        try {
            if (!(Main.connectedUser instanceof Player player)) {
                throw new IllegalStateException("Seuls les joueurs peuvent voter.");
            }

            GamePlatform selected = listPlatforms.getSelectionModel().getSelectedItem();
            if (selected == null) {
                throw new IllegalArgumentException("Sélectionnez une plateforme pour voter.");
            }

            if (player.getTokens() == 0) {
                throw new IllegalStateException("Vous n'avez plus de jetons disponibles !");
            }

            if (!selected.getTests().isEmpty()){
                throw new IllegalStateException("ce jeu possède déjà un teste !");
            }

            player.addTokens(-1);

            selected.addTokenBid(player.getPseudo(), 1);

            rafraichirInfosTokens(selected);
            System.out.println("Jeton placé par " + player.getPseudo() + " sur " + selected.getPlatform());

        } catch (IllegalStateException | IllegalArgumentException e) {
            afficherAlerte("Vote impossible", e.getMessage());
        }
    }

    @FXML
    private void handleUnplaceToken(ActionEvent event) {
        try {
            if (!(Main.connectedUser instanceof Player player)) {
                throw new IllegalStateException("Seuls les joueurs peuvent voter.");
            }

            GamePlatform selected = listPlatforms.getSelectionModel().getSelectedItem();
            if (selected == null) {
                throw new IllegalArgumentException("Sélectionnez une plateforme pour voter.");
            }

            if (!selected.getTests().isEmpty()){
                throw new IllegalStateException("ce jeu possède déjà un teste !");
            }

            if (!selected.getTokensBidding().containsKey(player.getPseudo()) || selected.getTokensBidding().get(player.getPseudo()) < 1){
                throw new IllegalStateException("tu n'as pas de jeton sur ce jeu !");
            }

            player.addTokens(1);

            selected.addTokenBid(player.getPseudo(), -1);

            rafraichirInfosTokens(selected);

        } catch (IllegalStateException | IllegalArgumentException e) {
            afficherAlerte("Vote impossible", e.getMessage());
        }
    }

    private void configurerColonneActions() {
        colActions.setCellFactory(new Callback<TableColumn<Evaluation, Void>, TableCell<Evaluation, Void>>() {
            @Override
            public TableCell<Evaluation, Void> call(TableColumn<Evaluation, Void> param) {
                return new TableCell<Evaluation, Void>() {
                    private final Button btnLike = new Button("👍");
                    private final Label lblLikes = new Label("0");

                    private final Button btnDislike = new Button("👎");
                    private final Label lblDislikes = new Label("0");

                    private final Button btnCensure = new Button("🚫");

                    private final HBox container = new HBox(10,
                            new HBox(3, btnLike, lblLikes),
                            new HBox(3, btnDislike, lblDislikes),
                            btnCensure
                    );

                    {
                        container.setAlignment(Pos.CENTER_LEFT);

                        btnLike.setOnAction(event -> handleVote(1));
                        btnDislike.setOnAction(event -> handleVote(-1));

                        btnCensure.setOnAction(event -> {
                            Evaluation eval = getTableView().getItems().get(getIndex());
                            eval.setCensored(true);
                            updateEvaluationsTable(listPlatforms.getSelectionModel().getSelectedItem());
                        });
                    }

                    private void handleVote(int action) {
                        Evaluation eval = getTableView().getItems().get(getIndex());
                        if (!(Main.connectedUser instanceof Player player)) return;

                        String userId = player.getPseudo();
                        Integer currentVote = eval.getUsefulnessRatings().get(userId);

                        int finalVote = (currentVote != null && currentVote == action) ? 0 : action;
                        eval.rateUsefulness(userId, finalVote);

                        getTableView().refresh();
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            Evaluation eval = getTableView().getItems().get(getIndex());

                            lblLikes.setText(String.valueOf(eval.getPositiveUsefulness()));
                            lblDislikes.setText(String.valueOf(eval.getNegativeUsefulness()));

                            btnLike.setStyle("");
                            btnDislike.setStyle("");

                            if (Main.connectedUser instanceof Player player) {
                                Integer vote = eval.getUsefulnessRatings().get(player.getPseudo());
                                if (vote != null) {
                                    if (vote == 1) {
                                        btnLike.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                                    } else if (vote == -1) {
                                        btnDislike.setStyle("-fx-background-color: #F44336; -fx-text-fill: white;");
                                    }
                                }
                            }

                            boolean isAdmin = Main.connectedUser.getProfile() == User.UserProfile.ADMIN;
                            btnCensure.setVisible(isAdmin);
                            btnCensure.setManaged(isAdmin);

                            setGraphic(container);
                        }
                    }
                };
            }
        });
    }

    @FXML
    void handleRetour(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/projet_jvm/SearchView.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
    }
}