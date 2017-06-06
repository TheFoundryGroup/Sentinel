package foundry.model;

import org.json.JSONObject;

import java.io.*;

public class Settings {

    private boolean leaderboard = false;

    private String file;

    private static String DEFAULT_FILE_NAME = "settings.json";

    //Tries to load settings from the file. If the file is not present
    //or incorrectly formatted, returns default values and saves to the file.
    public Settings(String filename) {
        this.file = filename;
        File file = new File(filename);
        try {
            JSONObject json = new JSONObject(file);
            leaderboard = json.getBoolean("leaderboard");
        } catch (Exception ignored) {}
    }
    
    public Settings() {
        this(DEFAULT_FILE_NAME);
    }

    public void save(String filename) {
        this.file = filename;
        save();
    }

    public void save() {
        if (file==null) file = DEFAULT_FILE_NAME;
        JSONObject json = new JSONObject();
        json.put("leaderboard", leaderboard);
        StringWriter writer = new StringWriter();
        json.write(writer);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream s = new ObjectOutputStream(fos);
            s.writeUTF(writer.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean leaderboardEnabled() {
        return leaderboard;
    }

    public void setLeaderboardEnabled(boolean leaderboard) {
        this.leaderboard = leaderboard;
    }
    
    @Override
    public String toString() {
        StringBuilder ans = new StringBuilder();
        ans.append("{");
        ans.append("leaderboard=");
        ans.append(leaderboard);
        ans.append("}");
        return ans.toString();
    }
}
