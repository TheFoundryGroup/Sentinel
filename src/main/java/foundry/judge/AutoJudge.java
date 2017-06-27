package foundry.judge;

import foundry.model.SentinelModel;
import foundry.model.Submission;
import foundry.model.Team;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class AutoJudge implements Runnable {
    
    private Queue<Submission> submissions;
    
    @Override
    public void run() {
        submissions = new LinkedList<>();
        while (true) {
            for (Team t : SentinelModel.teams()) {
                for (Submission s : t.getSubmissions()) {
                    if (s.getStatus()==JudgeStatus.WAITING && !submissions.contains(s)) submissions.offer(s);
                }
            }
            while (!submissions.isEmpty()) {
                Submission s = submissions.poll();
                System.out.println(s.getFolderName());
                JudgeProcess process = new JudgeProcess(SentinelModel.getProblem(s.getProblem()), s);
                try {
                    CompileResult res = process.compile();
                    SentinelModel.saveTeams();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
