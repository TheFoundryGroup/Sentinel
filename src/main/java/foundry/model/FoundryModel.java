package foundry.model;

import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FoundryModel {
    
    private static HashMap<String, Team> teams;
    private static HashMap<String, Judge> judges;
    
    static {
        teams = new HashMap<>();
        judges = new HashMap<>();
        parseTeams();
        parseJudges();
    }
    
    public static Team getTeam(String name) {
        return teams.get(name);
    }
    
    public static Collection<Team> teams() {
        return teams.values();
    }
    
    public static Judge getJudge(String name) {
        return judges.get(name);
    }
    
    public static Collection<Judge> judges() {
        return judges.values();
    }
    
    private static String readFile(String filename) {
        try {
            return Files.readAllLines(Paths.get(filename)).stream().reduce("", (a, b) -> a+"\n"+b);
        } catch (IOException e) {
            return "";
        }
    }
    
    private static void parseTeams() {
        JSONObject obj = new JSONObject(readFile("config/teams.json"));
        for (String name : obj.keySet()) {
            JSONObject team = obj.getJSONObject(name);
            teams.put(name, new Team(name, team.getString("password")));
        }
    }
    
    private static void parseJudges() {
        JSONObject obj = new JSONObject(readFile("config/judges.json"));
        for (String name : obj.keySet()) {
            JSONObject judge = obj.getJSONObject(name);
            judges.put(name, new Judge(name, judge.getString("password")));
        }
    }
    
    //deny access to constructor
    private FoundryModel() {}
    
}
