package foundry.judge;

import foundry.model.Problem;
import foundry.model.SentinelModel;
import foundry.model.Submission;
import foundry.model.Team;

public class JudgeProcess implements Runnable {
    
    private Problem problem;
    private Team team;
    private Submission submission;
    private JudgeStatus status;
    
    public JudgeProcess(Problem problem, Submission submission) {
        this.problem = problem;
        this.submission = submission;
        this.team = SentinelModel.getTeam(submission.getTeamName());
        this.status = JudgeStatus.WAITING;
    }
    
    @Override
    public void run() {
        //TODO: Compile and run program, set status as necessary
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
