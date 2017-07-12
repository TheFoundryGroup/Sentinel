package foundry.model;

public class Settings {

    @Setting(name = "Leaderboard", description = "Enables the leaderboard for contestants")
    public boolean leaderboard = false;
    
    public Settings(boolean leaderboard) {
        this.leaderboard = leaderboard;
    }
    
    public Settings() {
        this(true);
    }
    
}
