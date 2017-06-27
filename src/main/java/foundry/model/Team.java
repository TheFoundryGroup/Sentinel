package foundry.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static foundry.Utilities.generateWebsocketAuth;

public class Team {
    
    private String password;
    private HashMap<String, List<Submission>> submissions;
    private transient long websocketAuth;
    
    public Team(String password) {
        this.password = password;
        submissions = new HashMap<>();
    }
    
    public String getPassword() {
        return password;
    }
    
    public List<Submission> getSubmissions(String problem) {
        if (submissions==null) submissions = new HashMap<>();
        if (!submissions.containsKey(problem)) submissions.put(problem, new ArrayList<>());
        return submissions.get(problem);
    }
    
    public List<Submission> getSubmissions() {
        return submissions.values().stream().reduce(new ArrayList<>(), (a, b) -> {a.addAll(b); return a;});
    }
    
    public void addSubmission(String problem, Submission submission) {
        getSubmissions(problem).add(submission);
    }
    
    public long getWebsocketAuth() {
        if (websocketAuth==0) websocketAuth = generateWebsocketAuth();
        return websocketAuth;
    }
    
}
