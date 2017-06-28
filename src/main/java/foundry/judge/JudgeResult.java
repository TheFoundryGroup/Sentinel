package foundry.judge;

import java.util.List;

public class JudgeResult {
    
    public boolean correct;
    public List<Integer> diffs;
    
    public JudgeResult(boolean correct, List<Integer> diffs) {
        this.correct = correct;
        this.diffs = diffs;
    }
    
    public boolean isCorrect() {
        return correct;
    }
    public List<Integer> getDiffs() {
        return diffs;
    }
    
}
