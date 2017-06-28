package foundry.languages;

import foundry.judge.CompileResult;
import foundry.judge.RunResult;
import foundry.model.Submission;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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
    
    @Override
    public RunResult run(Submission s) throws IOException {
        Path wd = Paths.get(s.getFolderName());
        Process run = Runtime.getRuntime().exec("java "+s.getFileName().substring(0, s.getFileName().length()-5), null, wd.toAbsolutePath().toFile());
        InputStream err = run.getErrorStream();
        InputStream out = run.getInputStream();
        try {
            run.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    
        List<String> output = new ArrayList<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(out));
        String line = null;
        while ((line = br.readLine()) != null) output.add(line);
        
        if (run.exitValue()!=0) {
            StringBuilder error = new StringBuilder();
            BufferedReader in = new BufferedReader(new InputStreamReader(err));
            while ((line = in.readLine()) != null) {
                error.append(line).append("\n");
            }
            return new RunResult(false, error.toString(), output);
        }
        
        return new RunResult(true, "", output);
    }
}
