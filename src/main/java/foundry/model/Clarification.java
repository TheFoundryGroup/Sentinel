package foundry.model;

import foundry.views.WebsocketHandler;

public class Clarification {
    
    String from;
    boolean global;
    String problem;
    String message;
    String response;
    boolean responded;
    long timestamp;
    
    public Clarification(String from, String problem, String message) {
        this.from = from;
        this.message = message;
        this.problem = problem;
        this.global = false;
        this.response = null;
        this.responded = false;
        this.timestamp = System.currentTimeMillis();
        WebsocketHandler.updateClarification(this);
    }
    
    public void respond(String response, boolean global) {
        this.response = response;
        this.global = global;
        WebsocketHandler.updateClarification(this);
    }
    
    public boolean concerns(Team t) {
        return global || t==SentinelModel.getTeam(from); //reference equality check, should be OK
    }
    
    public boolean isGlobal() {
        return global;
    }
    public void setGlobal(boolean global) {
        this.global = global;
    }
    public String getResponse() {
        return response;
    }
    public void setResponse(String response) {
        this.response = response;
    }
    public boolean isResponded() {
        return responded;
    }
    public void setResponded(boolean responded) {
        this.responded = responded;
    }
    public String getFrom() {
        return from;
    }
    public String getProblem() {
        return problem;
    }
    public String getMessage() {
        return message;
    }
    public long getTimestamp() {
        return timestamp;
    }
}
