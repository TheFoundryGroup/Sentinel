package foundry.model;

public class Settings {

    private boolean leaderboard = false;
    
    public Settings(boolean leaderboard) {
        this.leaderboard = leaderboard;
    }
    
    public Settings() {
        this(true);
    }

    public boolean leaderboardEnabled() {
        return leaderboard;
    }

    public void setLeaderboardEnabled(boolean leaderboard) {
        this.leaderboard = leaderboard;
    }
    
}
