package foundry.judge;

import foundry.languages.Language;
import foundry.model.Problem;
import foundry.model.SentinelModel;
import foundry.model.Submission;
import foundry.model.Team;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JudgeProcess {
    
    private Problem problem;
    private Team team;
    private Submission submission;
    private JudgeStatus status;
    private Language language;
    
    public JudgeProcess(Problem problem, Submission submission) {
        this.problem = problem;
        this.submission = submission;
        this.team = SentinelModel.getTeam(submission.getTeamName());
        this.status = JudgeStatus.WAITING;
        this.language = SentinelModel.getLanguage(submission.getLanguage());
        cleanWorkspace();
    }
    
    public CompileResult compile() throws IOException {
        CompileResult res = language.compile(submission);
        if (res.isSuccessful()) {
            setStatus(JudgeStatus.COMPILED);
        } else {
            submission.setError(res.getOutput());
            setStatus(JudgeStatus.COMPILE_ERROR);
        }
        return res;
    }
    
    private void cleanWorkspace() {
        /*
        //get workspace ready to go
        Path workspacePath = Paths.get("/uploads/workspace");
        if (!Files.exists(workspacePath))
            try {
                Files.createDirectory(workspacePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        File workspace = new File("/uploads/workspace");
        //clean up workspace
        if (workspace.listFiles().length!=0) {
            for (File f : workspace.listFiles()) f.delete();
        }*/
    }
    
    private void setStatus(JudgeStatus status) {
        this.status = status;
        submission.setStatus(status);
    }
    
    public Problem getProblem() {
        return problem;
    }
    public Team getTeam() {
        return team;
    }
    public Submission getSubmission() {
        return submission;
    }
    public JudgeStatus getStatus() {
        return status;
    }
}
