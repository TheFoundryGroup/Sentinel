package foundry.model;

public class Submission {
    
    private int attempt;
    private String problem;
    String teamName;
    String language;
    
    public Submission(String teamName, String problem, int attempt, String language) {
        this.attempt = attempt;
        this.problem = problem;
        this.teamName = teamName;
        this.language = language;
    }
    
    public String getFileName() {
        return getFileName(teamName, problem, attempt, language);
    }
    
    public static String getFileName(String teamName, String problem, int attempt, String language) {
        return String.format("uploads/%s-%s-%d.%s", teamName, problem, attempt, language);
    }
    
    public int getAttempt() {
        return attempt;
    }
    public void setAttempt(int attempt) {
        this.attempt = attempt;
    }
    public String getProblem() {
        return problem;
    }
    public void setProblem(String problem) {
        this.problem = problem;
    }
    public String getTeamName() {
        return teamName;
    }
    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
    public String getLanguage() {
        return language;
    }
    public void setLanguage(String language) {
        this.language = language;
    }
    
}
