package foundry.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
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
    
    private static Gson gson;
    
    static {
        gson = new GsonBuilder().setPrettyPrinting().create();
        teams = new HashMap<>();
        judges = new HashMap<>();
        if (Files.exists(Paths.get("data/config.json"))) {
            settings = gson.fromJson(readFile("data/config.json"), Settings.class);
        } else {
            settings = new Settings();
        }
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
        Type collectionType = new TypeToken<HashMap<String, Team>>(){}.getType();
        teams = gson.fromJson(readFile("data/teams.json"), collectionType);
    }
    
    private static void parseJudges() {
        Type collectionType = new TypeToken<HashMap<String, Judge>>(){}.getType();
        judges = gson.fromJson(readFile("data/judges.json"), collectionType);
    }
    
    private static void parseProblems() {
        File dir = new File("data/problems");
        for (String s : Arrays.stream(dir.listFiles()).map(File::getName).filter(n -> n.endsWith(".in")).map(s -> s.substring(0, s.length()-3)).collect(Collectors.toList())) {
            try {
                Path in = Paths.get("data/problems/"+s + ".in");
                Path out = Paths.get("data/problems/"+s + ".out");
                String desc = Files.exists(Paths.get("data/problems/"+s+".info")) ? readFile("data/problems/"+s+".info") : "";
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
    
    private static void writeFile(String path, String contents) {
        try {
            if (Files.notExists(Paths.get(path))) Files.createFile(Paths.get(path));
            FileWriter f = new FileWriter(path);
            f.write(contents);
            f.flush();
            f.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
    
    public static void save() {
        saveTeams();
        saveJudges();
        saveSettings();
    }
    
    public static void saveTeams() {
        String serialized = gson.toJson(teams);
        writeFile("data/teams.json", serialized);
    }
    public static void saveJudges() {
        String serialized = gson.toJson(judges);
        writeFile("data/judges.json", serialized);
    }
    public static void saveSettings() {
        String serialized = gson.toJson(settings);
        writeFile("data/config.json", serialized);
    }
    
    public static void runAsync(Runnable r) {
        new Thread(r).run();
    }
    
    //deny access to constructor
    private SentinelModel() {}
    
}
