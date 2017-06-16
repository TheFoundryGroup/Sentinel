package foundry.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Team {
    
    private String password;
    private HashMap<String, List<Submission>> submissions;
    
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
    
    public void addSubmission(String problem, Submission submission) {
        getSubmissions(problem).add(submission);
    }
    
}
