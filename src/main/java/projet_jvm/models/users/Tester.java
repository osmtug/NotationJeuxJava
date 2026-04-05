package projet_jvm.models.users;

/**
 * Représente un testeur sur la plateforme.
 * Peut créer des tests structurés pour les jeux.
 */
public class Tester extends Player {
    private static final long serialVersionUID = 1L;
    private int testNumber = 0;

    public Tester(String pseudo) {
        super(pseudo);
        this.profile = UserProfile.TESTER;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public void incrementTest(){
        testNumber++;
        if (testNumber % 5 == 0){
            this.tokens++;
        }
    }
}
