package projet_jvm.models.users;

import projet_jvm.models.Test;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Représente un joueur enregistré sur la plateforme.
 * Possède des jeux et peut évaluer/voter.
 */
public class Player extends User {
    private static final long serialVersionUID = 1L;

    private Map<String, Integer> gamePlayTime;
    private int evaluationsPositiveCount;
    private int evaluationsNegativeCount;
    private int totalEvaluationsCount;

    protected int tokens;

    public Player(String pseudo) {
        super(pseudo, UserProfile.PLAYER);

        this.tokens = 3;

        this.gamePlayTime = new HashMap<>();
        this.evaluationsPositiveCount = 0;
        this.evaluationsNegativeCount = 0;
        this.totalEvaluationsCount = 0;
    }

    /**
     * Ajoute un jeu à la liste des jeux du joueur.
     * @param gameKey clé unique du jeu (nom_plateforme)
     * @param hours heures de jeu
     */
    public void addGamePlayTime(String gameKey, int hours) {
        if (hours < 0) throw new IllegalArgumentException("Les heures ne peuvent pas être négatives");
        gamePlayTime.put(gameKey, hours);
    }

    /**
     * Augmente le temps de jeu pour un jeu donné.
     */
    public void increasePlayTime(String gameKey, int hours) {
        if (hours < 0) throw new IllegalArgumentException("Les heures doivent être positives");
        gamePlayTime.put(gameKey, gamePlayTime.getOrDefault(gameKey, 0) + hours);
    }

    public int getPlayTime(String gameKey) {
        return gamePlayTime.getOrDefault(gameKey, 0);
    }

    public Map<String, Integer> getGamePlayTime() {
        return new HashMap<>(gamePlayTime);
    }

    /**
     * Vérifie si le joueur a joué assez longtemps (minimum 5 heures pour pouvoir évaluer).
     */
    public boolean hasPlayedEnough(String gameKey, int minimumHours) {
        return gamePlayTime.getOrDefault(gameKey, 0) >= minimumHours;
    }

    public void incrementPositiveEvaluations() {
        evaluationsPositiveCount++;
        if (evaluationsPositiveCount % 10 == 0) {
            addTokens(1);
        }
    }

    public int getEvaluationsPositiveCount() {
        return evaluationsPositiveCount;
    }

    public void incrementNegativeEvaluations() {
        evaluationsNegativeCount++;
        if (evaluationsNegativeCount % 10 == 0) {
            addTokens(-1);
        }
    }

    public int getEvaluationsNegativeCount() {
        return evaluationsNegativeCount;
    }

    /**
     * Calcule le temps de jeu total toutes plateformes confondues
     */
    public int getTotalPlayTime() {
        return gamePlayTime.values().stream().mapToInt(Integer::intValue).sum();
    }

    public void addTokens(int amount) {
        this.tokens += amount;
    }

    public int getTokens() {
        return tokens;
    }

    public void setTokens(int tokens) {
        this.tokens = tokens;
    }

    public int getTotalEvaluationsCount() {
        return totalEvaluationsCount;
    }

    public void addTotalEvaluationsCount(int amount) {
        this.totalEvaluationsCount += amount;
    }

    @Override
    public String toString() {
        return super.toString() + " | Jeux: " + gamePlayTime.size() + " | Temps total: " + getTotalPlayTime() + "h";
    }

    /**
     * Retourne la liste des jeux triée par durée de jeu décroissante.
     * Requis pour le profil des Testeurs/Admins
     */
    public List<Map.Entry<String, Integer>> getGamesSortedByTime() {
        if (gamePlayTime == null) return new ArrayList<>();

        return gamePlayTime.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .collect(Collectors.toList());
    }
}
