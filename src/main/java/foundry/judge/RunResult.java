package foundry.judge;

import java.util.List;

public class RunResult {
    
    private boolean successful;
    private String error;
    private List<String> output;
    
    public RunResult(boolean successful, String error, List<String> output) {
        this.successful = successful;
        this.error = error;
        this.output = output;
    }
    
    public boolean isSuccessful() {
        return successful;
    }
    public String getError() {
        return error;
    }
    public List<String> getOutput() {
        return output;
    }
}
