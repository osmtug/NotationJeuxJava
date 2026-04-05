package projet_jvm.models;

import projet_jvm.Main;
import projet_jvm.models.users.Player;
import projet_jvm.models.users.User;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import static projet_jvm.Main.userManager;

/**
 * Représente la version d'un jeu sur une plateforme spécifique.
 * Contient les données statiques + les évaluations et tests dynamiques.
 */
public class GamePlatform implements Serializable {
    private static final long serialVersionUID = 1L;

    private String platform;
    private int releaseYear;
    private String developer;
    private double globalSales;
    private int criticCount;
    private double criticScore;
    private int userCount;
    private double userScore;

    private List<Test> tests;
    private List<Evaluation> evaluations;
    private Map<String, Integer> tokensBidding;

    public GamePlatform(String platform, int releaseYear, String developer,
                        double globalSales, int criticCount, double criticScore,
                        int userCount, double userScore) {
        this.platform = platform;
        this.releaseYear = releaseYear;
        this.developer = developer;
        this.globalSales = globalSales;
        this.criticCount = criticCount;
        this.criticScore = criticScore;
        this.userCount = userCount;
        this.userScore = userScore;
        this.tests = new ArrayList<>();
        this.evaluations = new ArrayList<>();
        this.tokensBidding = new HashMap<>();
    }

    /**
     * Ajoute un test pour cette plateforme.
     * Il ne peut y avoir qu'un seul test par testeur.
     */
    public void addTest(Test test) {
        tests.removeIf(t -> t.getAuthor().getPseudo().equals(test.getAuthor().getPseudo()));
        tests.add(test);
    }

    public Test getTestByAuthor(String testerPseudo) {
        return tests.stream()
                .filter(t -> t.getAuthor().getPseudo().equals(testerPseudo))
                .findFirst()
                .orElse(null);
    }

    /**
     * Ajoute une évaluation.
     */
    public void addEvaluation(Evaluation evaluation) {
        evaluations.add(evaluation);
        updateUserScore();
    }

    /**
     * Retourne les évaluations triées par note (meilleures d'abord) puis par date (plus anciennes d'abord).
     */
    public List<Evaluation> getEvaluationsSorted() {
        return evaluations.stream()
                .sorted(Comparator.comparingDouble(Evaluation::getGlobalScore)
                        .reversed()
                        .thenComparingLong(e -> e.getDate().getTime()))
                .collect(Collectors.toList());
    }

    /**
     * Recalcule le score utilisateur moyen.
     */
    private void updateUserScore() {
        if (evaluations.isEmpty()) return;
        double sum = evaluations.stream()
                .mapToDouble(Evaluation::getGlobalScore)
                .sum();
        this.userScore = sum / evaluations.size();
        this.userCount = evaluations.size();
    }

    public void removeEvaluation(Evaluation evaluation) {
        evaluations.remove(evaluation);
        updateUserScore();
    }

    /**
     * Place des jetons de vote pour un jeu.
     */
    public void addTokenBid(String userId, int amount) {
        tokensBidding.put(userId, tokensBidding.getOrDefault(userId, 0) + amount);
    }

    /**
     * Récupère le nombre de jetons placés sur ce jeu.
     */
    public int getTotalTokensBidded() {
        return tokensBidding.values().stream().mapToInt(Integer::intValue).sum();
    }

    /**
     * Libère tous les jetons après publication d'un test.
     */
    public void releaseAllTokens() {
        tokensBidding.forEach((key, value) -> {
            User user = Main.userManager.getUser(key);
            Player p = (Player) user;
            p.addTokens(value);
        });
        tokensBidding.clear();
    }

    public String getPlatform() {
        return platform;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public String getDeveloper() {
        return developer;
    }

    public double getGlobalSales() {
        return globalSales;
    }

    public int getCriticCount() {
        return criticCount;
    }

    public double getCriticScore() {
        return criticScore;
    }

    public int getUserCount() {
        return userCount;
    }

    public double getUserScore() {
        return userScore;
    }

    public List<Test> getTests() {
        return new ArrayList<>(tests);
    }

    public void setTests(Test t) {
        tests.add(t);
    }

    public List<Evaluation> getEvaluations() {
        return new ArrayList<>(evaluations);
    }

    public Map<String, Integer> getTokensBidding() {
        return tokensBidding;
    }

    @Override
    public String toString() {
        return platform + " (" + releaseYear + ")";
    }
}
