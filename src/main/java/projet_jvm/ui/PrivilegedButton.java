package projet_jvm.ui;
import javafx.scene.control.Button;
import projet_jvm.Main;
import projet_jvm.models.users.User.UserProfile;

public class PrivilegedButton extends Button {

    private UserProfile targetProfile = UserProfile.GUEST;
    private boolean exclusive = false;

    public PrivilegedButton() {
        this.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                checkVisibility();
            }
        });
    }

    public void setTargetProfile(String profileName) {
        try {
            this.targetProfile = UserProfile.valueOf(profileName.toUpperCase());
            checkVisibility();
        } catch (IllegalArgumentException e) {
            System.err.println("Profil invalide : " + profileName);
        }
    }

    public void setExclusive(boolean exclusive) {
        this.exclusive = exclusive;
        checkVisibility();
    }

    public String getTargetProfile() { return targetProfile.name(); }
    public boolean isExclusive() { return exclusive; }

    private void checkVisibility() {
        if (Main.connectedUser == null || Main.connectedUser.getProfile() == null) {
            this.setVisible(false);
            this.setManaged(false);
            return;
        }

        boolean authorized;
        if (exclusive) {
            authorized = (Main.connectedUser.getProfile() == targetProfile);
        } else {
            authorized = Main.connectedUser.getProfile().canAccessAs(targetProfile);
        }

        this.setVisible(authorized);
        this.setManaged(authorized);
    }
}