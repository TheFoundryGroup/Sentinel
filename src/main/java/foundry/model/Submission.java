package foundry.model;

import foundry.judge.JudgeStatus;
import foundry.views.WebsocketHandler;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

public class Submission {
    
    private int attempt;
    private String problem;
    private String teamName;
    private String language;
    private JudgeStatus status;
    private long timestamp;
    private String file;
    private String error;
    
    public Submission(String teamName, String problem, int attempt, String language, String file) {
        this.attempt = attempt;
        this.problem = problem;
        this.teamName = teamName;
        this.language = language;
        this.file = file;
        this.error = "";
        timestamp = System.currentTimeMillis();
        status = JudgeStatus.WAITING;
        updateWebsocket();
    }
    
    public String getFolderName() {
        return getFolderName(teamName, problem, attempt);
    }
    
    public static String getFolderName(String teamName, String problem, int attempt) {
        return String.format("uploads/%s-%s-%d", teamName, problem, attempt);
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
        WebsocketHandler.updateSubmission(SentinelModel.getTeam(teamName), this);
    }
    public long getTimestamp() {
        return timestamp;
    }
    public String getFileName() {
        return file;
    }
    public void setFileName(String file) {
        this.file = file;
    }
    public String getError() {
        return error;
    }
    public void setError(String error) {
        this.error = error;
    }
}
