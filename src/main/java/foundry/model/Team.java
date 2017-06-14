package foundry.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Team {

    private String name;
    private String password;
    private HashMap<String, List<Submission>> submissions;
    
    public Team(String name, String password) {
        this.name = name;
        this.password = password;
        submissions = new HashMap<>();
    }
    
    public String getName() {
        return name;
    }
    
    public String getPassword() {
        return password;
    }
    
    public List<Submission> getSubmissions(String problem) {
        if (!submissions.containsKey(problem)) submissions.put(problem, new ArrayList<>());
        return submissions.get(problem);
    }
    
    public void addSubmission(String problem, Submission submission) {
        if (!submissions.containsKey(problem)) submissions.put(problem, new ArrayList<>());
        submissions.get(problem).add(submission);
    }
    
}
