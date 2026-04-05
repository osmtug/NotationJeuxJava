package projet_jvm.models;

import projet_jvm.models.users.Tester;

import java.io.Serializable;
import java.util.*;

/**
 * Représente un test structuré d'un jeu sur une plateforme.
 * Créé par un testeur.
 */
public class Test implements Serializable {
    private static final long serialVersionUID = 1L;

    private Date date;
    private String text;
    private String buildVersion;
    private Tester author;

    private Map<String, Double> categoryScores;
    private List<String> strengths;
    private List<String> weaknesses;
    private String testConditions;
    private List<String> similarGames;
    private Map<String, Double> genreSpecificScores;

    public Test(String text, String buildVersion, Tester author) {
        this.date = new Date();
        this.text = text;
        this.buildVersion = buildVersion;
        this.author = author;
        this.categoryScores = new HashMap<>();
        this.strengths = new ArrayList<>();
        this.weaknesses = new ArrayList<>();
        this.testConditions = "";
        this.similarGames = new ArrayList<>();
        this.genreSpecificScores = new HashMap<>();
    }

    /**
     * Ajoute une note pour une catégorie testée.
     */
    public void addCategoryScore(String category, double score) {
        if (score < 0 || score > 10) throw new IllegalArgumentException("Score entre 0 et 10");
        categoryScores.put(category, score);
    }

    /**
     * Ajoute un point fort.
     */
    public void addStrength(String strength) {
        strengths.add(strength);
    }

    /**
     * Ajoute un point faible.
     */
    public void addWeakness(String weakness) {
        weaknesses.add(weakness);
    }

    /**
     * Ajoute une note spécifique au genre.
     */
    public void addGenreSpecificScore(String category, double score) {
        if (score < 0 || score > 10) throw new IllegalArgumentException("Score entre 0 et 10");
        genreSpecificScores.put(category, score);
    }

    /**
     * Calcule la note moyenne de tous les tests.
     */
    public double getAverageScore() {
        if (categoryScores.isEmpty()) return 0;
        return categoryScores.values().stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0);
    }

    public Date getDate() {
        return date;
    }

    public String getText() {
        return text;
    }

    public String getBuildVersion() {
        return buildVersion;
    }

    public Tester getAuthor() {
        return author;
    }

    public Map<String, Double> getCategoryScores() {
        return new HashMap<>(categoryScores);
    }

    public List<String> getStrengths() {
        return new ArrayList<>(strengths);
    }

    public List<String> getWeaknesses() {
        return new ArrayList<>(weaknesses);
    }

    public String getTestConditions() {
        return testConditions;
    }

    public void setTestConditions(String testConditions) {
        this.testConditions = testConditions;
    }

    public List<String> getSimilarGames() {
        return new ArrayList<>(similarGames);
    }

    public void addSimilarGame(String gameName) {
        similarGames.add(gameName);
    }

    public Map<String, Double> getGenreSpecificScores() {
        return new HashMap<>(genreSpecificScores);
    }

    @Override
    public String toString() {
        return "Test par " + author.getPseudo() + " (v" + buildVersion + ") - Score moyen: " + String.format("%.1f", getAverageScore());
    }
}

