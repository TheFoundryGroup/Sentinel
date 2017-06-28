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
import java.util.ArrayList;
import java.util.List;

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
    
    public RunResult run() throws IOException {
        RunResult res = language.run(submission);
        if (res.isSuccessful()) {
            setStatus(JudgeStatus.JUDGING);
        } else {
            submission.setError(res.getError());
            setStatus(JudgeStatus.RUNTIME_ERROR);
        }
        return res;
    }
    
    public JudgeResult judge(RunResult res) {
        List<String> output = res.getOutput();
        List<String> expected = problem.getExpected();
        List<Integer> diffs = new ArrayList<>();
        int i;
        for (i = 0; i<output.size() && i<expected.size(); i++) {
            if (!output.get(i).trim().equals(expected.get(i))) diffs.add(i);
        }
        for (;i<output.size(); i++) diffs.add(i);
        if (diffs.size()==0) {
            setStatus(JudgeStatus.CORRECT);
        } else {
            setStatus(JudgeStatus.INCORRECT);
        }
        return new JudgeResult(diffs.size()==0, diffs);
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
