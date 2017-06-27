package foundry.languages;

import foundry.judge.CompileResult;
import foundry.model.Submission;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Java implements Language {
    
    @Override
    public String getName() {
        return "Java";
    }
    
    @Override
    public String getSourceExtension() {
        return ".java";
    }
    
    @Override
    public boolean isCompiled() {
        return true;
    }
    
    @Override
    public CompileResult compile(Submission s) throws IOException {
        Path sourcePath = Paths.get(s.getFolderName());
        Process compile = Runtime.getRuntime().exec("javac "+s.getFileName(), null, sourcePath.toAbsolutePath().toFile());
        InputStream err = compile.getErrorStream();
        try {
            compile.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (compile.exitValue()!=0) {
            //unsuccessful compilation
            StringBuilder output = new StringBuilder();
            BufferedReader in = new BufferedReader(new InputStreamReader(err));
            String line = null;
            while ((line = in.readLine()) != null) {
                output.append(line).append("\n");
            }
            return new CompileResult(false, output.toString());
        }
        //successfully compiled
        return new CompileResult(true, "");
    }
}
