package projet_jvm.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import projet_jvm.Main;
import projet_jvm.managers.GameManager;
import projet_jvm.models.*;
import projet_jvm.models.users.Tester;
import java.time.ZoneId;
import java.util.Date;

public class TestEditorController {
    @FXML private TextField fieldBuild, fieldConditions, fieldStrength, fieldWeakness;
    @FXML private TextArea areaContent;
    @FXML private Slider slideGameplay, slideGraphics, slideGenre;
    @FXML private Label valGameplay, valGraphics, valGenre, labelGenreSpecific;
    @FXML private ListView<String> listStrengths, listWeaknesses;
    @FXML private DatePicker datePicker;

    private GameManager.Mission currentMission;

    public void setMission(GameManager.Mission mission) {
        this.currentMission = mission;

        String genre = mission.getGame().getGenre();
        if (genre.equalsIgnoreCase("Strategy")) labelGenreSpecific.setText("Intérêt Hist.:");
        else if (genre.equalsIgnoreCase("Sports")) labelGenreSpecific.setText("Licences:");
        else labelGenreSpecific.setText("Originalité:");
    }

    @FXML
    public void initialize() {
        slideGameplay.valueProperty().addListener((o, old, n) -> valGameplay.setText(String.format("%.1f", n)));
        slideGraphics.valueProperty().addListener((o, old, n) -> valGraphics.setText(String.format("%.1f", n)));
        slideGenre.valueProperty().addListener((o, old, n) -> valGenre.setText(String.format("%.1f", n)));
        datePicker.setValue(java.time.LocalDate.now());
    }

    @FXML void addStrength() { if(!fieldStrength.getText().isEmpty()) listStrengths.getItems().add(fieldStrength.getText()); fieldStrength.clear(); }

    @FXML void addWeakness() { if(!fieldWeakness.getText().isEmpty()) listWeaknesses.getItems().add(fieldWeakness.getText()); fieldWeakness.clear(); }

    @FXML
    private void handlePublish(ActionEvent event) {
        try {
            Tester tester = (Tester) Main.connectedUser;
            Test newTest = new Test(areaContent.getText(), fieldBuild.getText(), tester);

            newTest.addCategoryScore("Gameplay", slideGameplay.getValue());
            newTest.addCategoryScore("Graphismes", slideGraphics.getValue());
            newTest.addGenreSpecificScore(labelGenreSpecific.getText(), slideGenre.getValue());

            newTest.setTestConditions(fieldConditions.getText());
            listStrengths.getItems().forEach(newTest::addStrength);
            listWeaknesses.getItems().forEach(newTest::addWeakness);

            currentMission.getPlatform().setTests(newTest);
            currentMission.getPlatform().releaseAllTokens();

            javafx.stage.Stage stage = (javafx.stage.Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            stage.close();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Erreur lors de la publication : " + e.getMessage()).show();
        }
    }
}
