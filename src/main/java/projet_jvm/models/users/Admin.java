package projet_jvm.models.users;

import projet_jvm.models.Evaluation;

/**
 * Représente un administrateur sur la plateforme.
 * Peut modérer et bloquer les utilisateurs.
 */
public class Admin extends Tester {
    private static final long serialVersionUID = 1L;

    public Admin(String pseudo) {
        super(pseudo);
        this.profile = UserProfile.ADMIN;
    }

    /**
     * Bloque un utilisateur de la plateforme.
     */
    public void blockUser(User user) {
        user.setBlocked(true);
    }

    /**
     * Débloque un utilisateur.
     */
    public void unblockUser(User user) {
        user.setBlocked(false);
    }

    /**
     * Supprime une évaluation de la base (modération).
     */
    public void deleteEvaluation(Evaluation evaluation) {
        evaluation.setCensored(true);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
