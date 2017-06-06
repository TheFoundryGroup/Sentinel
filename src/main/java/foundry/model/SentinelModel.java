package foundry.model;

import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.io.*;
import java.util.stream.Collectors;

public class SentinelModel {
    
    private static HashMap<String, Team> teams;
    private static HashMap<String, Judge> judges;
    private static Settings settings;
    
    private static List<Problem> problems;
    
    static {
        teams = new HashMap<>();
        judges = new HashMap<>();
        settings = new Settings();
        problems = new ArrayList<>();
        parseTeams();
        parseJudges();
        parseProblems();
    }

    public static Settings getSettings() {
        return settings;
    }
    
    public static boolean hasTeam(String name) {
        return teams.containsKey(name);
    }
    
    public static Team getTeam(String name) {
        return teams.get(name);
    }
    
    public static Collection<Team> teams() {
        return teams.values();
    }
    
    public static boolean hasJudge(String name) {
        return judges.containsKey(name);
    }
    
    public static Judge getJudge(String name) {
        return judges.get(name);
    }
    
    public static Collection<Judge> judges() {
        return judges.values();
    }
    
    public static List<Problem> getProblems() {
        return problems;
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
    
    private static void parseProblems() {
        File dir = new File("config/problems");
        for (String s : Arrays.stream(dir.listFiles()).map(File::getName).filter(n -> n.endsWith(".in")).map(s -> s.substring(0, s.length()-3)).collect(Collectors.toList())) {
            try {
                Path in = Paths.get("config/problems/"+s + ".in");
                Path out = Paths.get("config/problems/"+s + ".out");
                String desc = Files.exists(Paths.get("config/problems/"+s+".info")) ? readFile("config/problems/"+s+".info") : "";
                Problem p = new Problem(Files.readAllLines(out), readFile(in), s, desc);
                problems.add(p);
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }
    
    private static String readFile(Path path) {
        try {
            String ans = Files.readAllLines(path).stream().reduce("", (a, b) -> a+"\n"+b);
            if (ans.length()>1) ans = ans.substring(1);
            return ans;
        } catch (IOException e) {
            return "";
        }
    }
    private static String readFile(String path) {
        return readFile(Paths.get(path));
    }
    
    //deny access to constructor
    private SentinelModel() {}
    
}
