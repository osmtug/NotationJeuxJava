package projet_jvm.models.users;

/**
 * Représente un invité (utilisateur anonyme).
 * Peut seulement consulter les évaluations.
 */
public class Guest extends User {

    public Guest() {
        super("Invité", UserProfile.GUEST);
    }

    @Override
    public String toString() {
        return "Invité [lecture seule]";
    }
}
