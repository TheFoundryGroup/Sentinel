package foundry.views;

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
        String language = req.queryMap().value("language");
        Path location = Files.createFile(Paths.get(String.format("uploads/%s-%s-%d.%s", teamName, problem, subNum, language)));
        try (InputStream in = req.raw().getPart("file").getInputStream()) {
            Files.copy(in, location, StandardCopyOption.REPLACE_EXISTING);
        }
        team.addSubmission(problem, new Submission());
        res.redirect("");
        return "";
    };
    
}
