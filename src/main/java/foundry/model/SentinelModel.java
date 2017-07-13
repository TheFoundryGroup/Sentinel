package foundry.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import foundry.languages.Java;
import foundry.languages.Language;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.io.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SentinelModel {
    
    private static HashMap<String, Team> teams;
    private static HashMap<String, Judge> judges;
    private static Settings settings;
    
    private static List<Problem> problems;
    private static List<Clarification> clarifications;
    
    private static HashMap<String, Language> languages;
    
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
        clarifications = new LinkedList<>();
        languages = new HashMap<>();
        languages.put("Java", new Java());
        parseTeams();
        parseJudges();
        parseProblems();
        parseClarifications();
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
    
    public static List<Clarification> getClarifications() {
        return clarifications;
    }
    public static List<Clarification> getClarifications(Team t) {
        return clarifications.stream().filter(c -> c.concerns(t)).collect(Collectors.toList());
    }
    public static Clarification getClarification(int id) {
        for (Clarification c : clarifications) if (c.getId()==id) return c;
        return null;
    }
    
    public static void addClarification(Clarification c) {
        clarifications.add(c);
    }
    
    public static Problem getProblem(String problem) {
        for (Problem p : problems) if (p.getName().equals(problem)) return p;
        return null;
    }
    
    public static Language getLanguage(String language) {
        return languages.get(language);
    }
    
    private static void parseTeams() {
        Type collectionType = new TypeToken<HashMap<String, Team>>(){}.getType();
        teams = gson.fromJson(readFile("data/teams.json"), collectionType);
    }
    
    private static void parseJudges() {
        Type collectionType = new TypeToken<HashMap<String, Judge>>(){}.getType();
        judges = gson.fromJson(readFile("data/judges.json"), collectionType);
    }
    
    private static void parseClarifications() {
        Path file = Paths.get("data/clarifications.json");
        if (!Files.exists(file)) return;
        Type collectionType = new TypeToken<ArrayList<Clarification>>(){}.getType();
        clarifications = gson.fromJson(readFile("data/clarifications.json"), collectionType);
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
        saveClarifications();
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
    public static void saveClarifications() {
        String serialized = gson.toJson(clarifications);
        writeFile("data/clarifications.json", serialized);
    }
    
    public static void runAsync(Runnable r) {
        new Thread(r).run();
    }
    
    //deny access to constructor
    private SentinelModel() {}
    
}
