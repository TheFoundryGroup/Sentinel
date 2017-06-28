package foundry.views;

import foundry.languages.Language;
import foundry.model.Problem;
import foundry.model.SentinelModel;
import foundry.model.Submission;
import foundry.model.Team;
import spark.Route;

import javax.servlet.MultipartConfigElement;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class UploadView {
    
    public static Route handleUploadPost = (req, res) -> {
        req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
        String teamName = req.session().attribute("loggedIn");
        String problem = req.queryMap().value("problem");
        Team team = SentinelModel.getTeam(teamName);
        int subNum = team.getSubmissions(problem).size()+1;
        String languageName = req.queryMap().value("language");
        Language language = SentinelModel.getLanguage(languageName);
        
        if (problem==null || problem.equals("")) {
            System.out.println("malformed input");
            res.redirect("");
            return "";
        }
        
        Path folder = Files.createDirectory(Paths.get(Submission.getFolderName(teamName, problem, subNum)));
        
        Path other = Paths.get(req.queryMap().value("path"));
        
        if (other.getFileName().toString().equals("")) {
            System.out.println("malformed input");
            res.redirect("");
            return "";
        }
        
        Path location = folder.resolve(other.getFileName());
        try (InputStream in = req.raw().getPart("file").getInputStream()) {
            Files.copy(in, location, StandardCopyOption.REPLACE_EXISTING);
        }
        
        Path input = Paths.get("data/problems/"+problem+".in");
        if (Files.exists(input)) {
            Path link = folder.resolve(input.getFileName());
            Files.createLink(link, input);
        }
        
        team.addSubmission(problem, new Submission(teamName, problem, subNum, languageName, other.getFileName().toString()));
        res.redirect("");
        SentinelModel.runAsync(SentinelModel::saveTeams);
        return "";
    };
    
}
