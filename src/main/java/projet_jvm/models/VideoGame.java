package projet_jvm.models;

import java.io.Serializable;
import java.util.*;

/**
 * Représente un jeu vidéo sur la plateforme.
 * Contient les données statiques et les données dynamiques.
 */
public class VideoGame implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private String genre;
    private String publisher;
    private String rating;
    private Map<String, GamePlatform> platforms;

    public VideoGame(String name, String genre, String publisher, String rating) {
        this.name = name;
        this.genre = genre;
        this.publisher = publisher;
        this.rating = rating;
        this.platforms = new HashMap<>();
    }

    /**
     * Ajoute ou met à jour une plateforme pour ce jeu.
     */
    public void addPlatform(GamePlatform platform) {
        platforms.put(platform.getPlatform(), platform);
    }

    public GamePlatform getPlatform(String platformName) {
        return platforms.get(platformName);
    }

    public boolean hasPlatform(String platformName) {
        return platforms.containsKey(platformName);
    }

    public Collection<GamePlatform> getAllPlatforms() {
        return new ArrayList<>(platforms.values());
    }

    public String getName() {
        return name;
    }

    public String getGenre() {
        return genre;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getRating() {
        return rating;
    }

    /**
     * Retourne une clé unique pour ce jeu sur une plateforme.
     */
    public String getGameKey(String platform) {
        return name + "_" + platform;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VideoGame videoGame = (VideoGame) o;
        return Objects.equals(name, videoGame.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name + " (" + genre + ") - " + publisher;
    }
}

