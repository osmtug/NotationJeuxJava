package projet_jvm.models.users;

import java.io.Serializable;
import java.util.*;

/**
 * Représente un utilisateur de la plateforme.
 * Classe abstraite de base pour les différents profils.
 */
public abstract class User implements Serializable {
    private static final long serialVersionUID = 1L;

    protected String pseudo;
    protected UserProfile profile;

    protected boolean blocked;

    public User(String pseudo, UserProfile profile) {
        this.pseudo = pseudo;
        this.profile = profile;

        this.blocked = false;
    }

    /**
     * Énumération des profils utilisateur avec leurs privilèges.
     * Hiérarchie: GUEST < PLAYER < TESTER < ADMIN
     */
    public enum UserProfile {
        GUEST(0),
        PLAYER(1),
        TESTER(2),
        ADMIN(3);

        private final int level;

        UserProfile(int level) {
            this.level = level;
        }

        public int getLevel() {
            return level;
        }

        public boolean canAccessAs(UserProfile required) {
            return this.level >= required.level;
        }
    }

    public String getPseudo() {
        return pseudo;
    }

    public UserProfile getProfile() {
        return profile;
    }

    public void setProfile(UserProfile profile) {
        this.profile = profile;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(pseudo, user.pseudo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pseudo);
    }

    @Override
    public String toString() {
        return pseudo + " [" + profile + "]";
    }
}
