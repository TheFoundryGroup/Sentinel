package foundry.languages;

import foundry.judge.CompileResult;
import foundry.model.Submission;

import java.io.IOException;

public interface Language {
    
    String getName();
    String getSourceExtension();
    boolean isCompiled();
    CompileResult compile(Submission s) throws IOException;
    
}
