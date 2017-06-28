package foundry.judge;

public class CompileResult {
    
    private boolean successful;
    private String output;
    
    public CompileResult(boolean successful, String output) {
        this.successful = successful;
        this.output = output;
    }
    
    public boolean isSuccessful() {
        return successful;
    }
    public String getOutput() {
        return output;
    }
}
