package projet_jvm.managers;


import projet_jvm.models.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Gère la base de données des jeux vidéo et le chargement depuis CSV.
 */
public class GameManager {
    private Map<String, VideoGame> games;
    private static final String CSV_URL = "https://raw.githubusercontent.com/charlesbrantstec/VGSalesRatings/" +
            "28980b2078f851b30d449186a45cb5127d81ea60/VG/output_csv/vg_data.csv";

    public GameManager() {
        this.games = new HashMap<>();
    }

    /**
     * Charge les données des jeux depuis le CSV en ligne.
     */
    public void loadGamesFromCSV() {
        System.out.println("Chargement des données des jeux depuis le serveur...");

        try {
            URL url = new URL(CSV_URL);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            int lineCount = 0;

            in.readLine();

            while ((line = in.readLine()) != null) {
                try {
                    parseCSVLine(line);
                    lineCount++;
                } catch (Exception e) {
                }
            }

            in.close();
            System.out.println("✓ " + lineCount + " jeux chargés avec succès (avec " + games.size() + " titres uniques)");

        } catch (IOException e) {
            System.err.println("Erreur lors du chargement du CSV: " + e.getMessage());
            loadSampleData();
        }
    }

    /**
     * Parse une ligne du CSV.
     */
    private void parseCSVLine(String line) {
        String[] parts = line.split(",");
        if (parts.length < 17) return;

        try {
            String name = parts[1].trim();
            String platform = parts[2].trim();
            int yearOfRelease = parseYear(parts[3].trim());
            String genre = parts[4].trim();
            String publisher = parts[5].trim();
            double globalSales = Double.parseDouble(parts[10].trim());

            int criticCount = parseIntSafely(parts[12].trim());
            double criticScore = parseDoubleSafely(parts[11].trim());

            int userCount = parseIntSafely(parts[14].trim());
            double userScore = parseDoubleSafely(parts[13].trim());

            String developer = parts[15].trim();
            String rating = parts[16].trim();

            if (!games.containsKey(name)) {
                games.put(name, new VideoGame(name, genre, publisher, rating));
            }

            VideoGame game = games.get(name);
            GamePlatform gamePlatform = new GamePlatform(platform, yearOfRelease, developer, globalSales,
                    criticCount, criticScore, userCount, userScore);
            game.addPlatform(gamePlatform);

        } catch (Exception e) {
        }
    }

    private int parseYear(String year) {
        try {
            return Integer.parseInt(year);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private int parseIntSafely(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private double parseDoubleSafely(String value) {
        try {
            return value.isEmpty() ? 0 : Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Charge des données d'exemple pour développement local.
     */
    private void loadSampleData() {
        System.out.println("Chargement des données d'exemple...");

        VideoGame game1 = new VideoGame("Virtua Tennis 4", "Sports", "Sega", "E");
        GamePlatform p1 = new GamePlatform("PC", 2011, "Sega", 0.04, 5, 66, 43, 7.3);
        game1.addPlatform(p1);
        games.put(game1.getName(), game1);

        VideoGame game2 = new VideoGame("Mario Kart 8", "Racing", "Nintendo", "E");
        GamePlatform p2 = new GamePlatform("Wii U", 2014, "Nintendo", 8.46, 80, 82, 2500, 8.1);
        game2.addPlatform(p2);
        games.put(game2.getName(), game2);

        System.out.println("✓ Données d'exemple chargées");
    }

    /**
     * Recherche un jeu par nom.
     */
    public List<VideoGame> searchByName(String query) {
        String lowerQuery = query.toLowerCase();
        return games.values().stream()
                .filter(g -> g.getName().toLowerCase().contains(lowerQuery))
                .collect(Collectors.toList());
    }

    /**
     * Recherche les jeux par genre.
     */
    public List<VideoGame> searchByGenre(String query) {
        String lowerQuery = query.toLowerCase();
        return games.values().stream()
                .filter(g -> g.getGenre().toLowerCase().contains(lowerQuery))
                .collect(Collectors.toList());
    }

    /**
     * Recherche les jeux par éditeur.
     */
    public List<VideoGame> searchByPublisher(String query) {
        String lowerQuery = query.toLowerCase();
        return games.values().stream()
                .filter(g -> g.getPublisher().toLowerCase().contains(lowerQuery))
                .collect(Collectors.toList());
    }

    public VideoGame getGameByName(String name) {
        return games.get(name);
    }

    public boolean gameExists(String name) {
        return games.containsKey(name);
    }

    public Collection<VideoGame> getAllGames() {
        return new HashSet<>(games.values());
    }

    public int getTotalGamesCount() {
        return games.size();
    }

    /**
     * Retourne la liste de toutes les plateformes de tous les jeux,
     * emballées dans un objet "Mission" pour faciliter l'affichage.
     */
    public List<Mission> getAllMissions() {
        List<Mission> missions = new ArrayList<>();
        for (VideoGame game : games.values()) {
            for (GamePlatform platform : game.getAllPlatforms()) {
                missions.add(new Mission(game, platform));
            }
        }
        return missions;
    }

    public static class Mission {
        private final VideoGame game;
        private final GamePlatform platform;

        public Mission(VideoGame game, GamePlatform platform) {
            this.game = game;
            this.platform = platform;
        }

        public VideoGame getGame() { return game; }
        public GamePlatform getPlatform() { return platform; }
        public String getGameName() { return game.getName(); }
        public String getPlatformName() { return platform.getPlatform(); }
        public int getTokens() { return platform.getTotalTokensBidded(); }
    }
}
