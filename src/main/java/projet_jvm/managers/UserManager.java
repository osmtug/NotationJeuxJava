package projet_jvm.managers;

import projet_jvm.models.*;
import projet_jvm.models.users.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Gère les utilisateurs et l'authentification de la plateforme.
 */
public class UserManager {
    private Map<String, User> users;
    private User currentUser;

    public UserManager() {
        this.users = new HashMap<>();
        this.currentUser = new Guest();
        Admin admin = new Admin("admin");
        users.put("admin", admin);
    }

    /**
     * Cherche l'utilisateur (invité par défaut).
     */
    public User login(String pseudo) {
        if (pseudo == null || pseudo.trim().isEmpty()) {
            currentUser = new Guest();
            return currentUser;
        }

        User user = users.get(pseudo);
        if (user != null) {
            if (user.isBlocked()) {
                throw new IllegalArgumentException("Cet utilisateur est bloqué!");
            }
            currentUser = user;
            return user;
        }

        throw new IllegalArgumentException("Utilisateur non trouvé: " + pseudo);
    }

    /**
     * Inscrit un nouveau joueur.
     */
    public Player registerPlayer(String pseudo) {
        if (pseudo == null || pseudo.trim().isEmpty()) {
            throw new IllegalArgumentException("Le pseudo ne peut pas être vide");
        }

        if (users.containsKey(pseudo)) {
            throw new IllegalArgumentException("Le pseudo est déjà utilisé!");
        }

        Player player = new Player(pseudo);
        users.put(pseudo, player);
        return player;
    }

    /**
     * Désinscrit un joueur.
     */
    public void unregisterPlayer(String pseudo) {
        if (!users.containsKey(pseudo)) {
            throw new IllegalArgumentException("Utilisateur non trouvé");
        }

        User user = users.get(pseudo);
        if (!(user instanceof Player)) {
            throw new IllegalArgumentException("Seuls les joueurs peuvent être désinscrit");
        }

        users.remove(pseudo);
    }

    /**
     * Change le statut d'un joueur.
     */
    public void promoteUser(String pseudo, User.UserProfile newProfile) {
        User user = users.get(pseudo);
        if (user == null) {
            throw new IllegalArgumentException("Utilisateur non trouvé");
        }

        if (newProfile == User.UserProfile.TESTER) {
            if (user instanceof Player player && !(user instanceof Tester)) {
                Tester tester = new Tester(pseudo);
                tester.setTokens(player.getTokens());
                users.put(pseudo, tester);
            }
        } else if (newProfile == User.UserProfile.ADMIN) {
            if (user instanceof Tester player && !(user instanceof Admin)) {
                Admin admin = new Admin(pseudo);
                admin.setTokens(player.getTokens());
                users.put(pseudo, admin);
            }
        }
    }

    /**
     * Récupère un utilisateur par pseudo.
     */
    public User getUser(String pseudo) {
        return users.get(pseudo);
    }

    /**
     * Retourne l'utilisateur connecté actuellement.
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Affiche les informations d'un utilisateur.
     */
    public void displayUserInfo(String pseudo) {
        User user = users.get(pseudo);
        if (user == null) {
            System.out.println("Utilisateur non trouvé");
            return;
        }

        System.out.println("\n=== Profil: " + pseudo + " ===");
        System.out.println("Statut: " + user.getProfile());
        System.out.println("Bloqué: " + (user.isBlocked() ? "OUI" : "NON"));

        if (user instanceof Player player) {
            System.out.println("Profil joueur:");
            System.out.println("Jetons: " + player.getTokens());
            System.out.println("  - Temps total joué: " + player.getTotalPlayTime() + "h");
            System.out.println("  - Jeux possédés: " + player.getGamePlayTime().size());
            System.out.println("  - Évaluations positives: " + player.getEvaluationsPositiveCount());
        }
    }

    /**
     * Bloque un utilisateur (admin seulement).
     */
    public void blockUser(String pseudo) {
        User user = users.get(pseudo);
        if (user != null) {
            user.setBlocked(true);
        }
    }

    /**
     * Débloque un utilisateur (admin seulement).
     */
    public void unblockUser(String pseudo) {
        User user = users.get(pseudo);
        if (user != null) {
            user.setBlocked(false);
        }
    }

    public boolean userExists(String pseudo) {
        return users.containsKey(pseudo);
    }

    public Collection<User> getAllUsers() {
        return new HashSet<>(users.values());
    }

    public void logout() {
        currentUser = new Guest();
    }
}
