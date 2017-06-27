package foundry.model;

import foundry.judge.JudgeStatus;
import foundry.views.WebsocketHandler;

public class Submission {
    
    private int attempt;
    private String problem;
    private String teamName;
    private String language;
    private JudgeStatus status;
    private long timestamp;
    
    public Submission(String teamName, String problem, int attempt, String language) {
        this.attempt = attempt;
        this.problem = problem;
        this.teamName = teamName;
        this.language = language;
        timestamp = System.currentTimeMillis();
        status = JudgeStatus.WAITING;
        updateWebsocket();
    }
    
    public String getFileName() {
        return getFileName(teamName, problem, attempt, language);
    }
    
    public static String getFileName(String teamName, String problem, int attempt, String language) {
        return String.format("uploads/%s-%s-%d.%s", teamName, problem, attempt, language);
    }
    
    public void updateWebsocket() {
        WebsocketHandler.updateSubmission(SentinelModel.getTeam(teamName), this);
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
    public JudgeStatus getStatus() {
        return status;
    }
    public void setStatus(JudgeStatus status) {
        this.status = status;
    }
    public long getTimestamp() {
        return timestamp;
    }
}
