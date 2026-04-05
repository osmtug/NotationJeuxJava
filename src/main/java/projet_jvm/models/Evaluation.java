package projet_jvm.models;

import projet_jvm.models.users.Player;

import java.io.Serializable;
import java.util.*;

/**
 * Représente une évaluation écrite par un joueur.
 */
public class Evaluation implements Serializable {
    private static final long serialVersionUID = 1L;

    private Date date;
    private String text;
    private String buildVersion;
    private double globalScore;
    private Player author;
    private boolean censored;

    private Map<String, Integer> usefulnessRatings;

    public Evaluation(String text, String buildVersion, double globalScore, Player author) {
        if (globalScore < 0 || globalScore > 10) {
            throw new IllegalArgumentException("Le score doit être entre 0 et 10");
        }
        this.date = new Date();
        this.text = text;
        this.buildVersion = buildVersion;
        this.globalScore = globalScore;
        this.author = author;
        this.censored = false;
        this.usefulnessRatings = new HashMap<>();
    }

    /**
     * Évalue l'utilité de cette évaluation.
     * @param userId ID de l'évaluateur
     * @param rating +1 (utile), 0 (neutre), -1 (pas utile)
     */
    public void rateUsefulness(String userId, int rating) {
        if (rating < -1 || rating > 1) {
            throw new IllegalArgumentException("Rating doit être -1, 0 ou +1");
        }
        if (!usefulnessRatings.containsKey(userId)){
            if (rating == 1){
                author.incrementPositiveEvaluations();
            } else if (rating == -1){
                author.incrementNegativeEvaluations();
            }
        }

        usefulnessRatings.put(userId, rating);
    }

    /**
     * Compte le nombre d'évaluations positives (+1).
     */
    public int getPositiveUsefulness() {
        return (int) usefulnessRatings.values().stream()
                .filter(r -> r == 1)
                .count();
    }

    /**
     * Compte le nombre d'évaluations négatives (-1).
     */
    public int getNegativeUsefulness() {
        return (int) usefulnessRatings.values().stream()
                .filter(r -> r == -1)
                .count();
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

    public double getGlobalScore() {
        return globalScore;
    }

    public Player getAuthor() {
        return author;
    }

    public boolean isCensored() {
        return censored;
    }

    public void setCensored(boolean censored) {
        this.censored = censored;
    }

    public Map<String, Integer> getUsefulnessRatings() {
        return new HashMap<>(usefulnessRatings);
    }

    @Override
    public String toString() {
        return "Evaluation par " + author.getPseudo() + " - Score: " + globalScore +
                "/10 | Utilite: " + getPositiveUsefulness() + " utile(s) / " + getNegativeUsefulness() + " pas utile(s)";
    }
}

