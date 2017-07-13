package foundry;

import foundry.judge.AutoJudge;
import foundry.model.SentinelModel;
import foundry.model.Settings;
import foundry.views.*;
import spark.template.velocity.VelocityTemplateEngine;

import static spark.Spark.*;

public class Application {
    
    public static void main(String[] args) {
        port(80);
        webSocket("/websocket", WebsocketHandler.class);
        staticFileLocation("public");
        
        //do nothing in case of NogLoggedIn
        exception(NotLoggedInException.class, (e, req, res) -> {});
        
        before((req, res) -> {
            if (req.session().attribute("loggedIn")==null && (!req.pathInfo().equals("/login") && !req.pathInfo().equals("/login/"))) {
                res.redirect("login", 303);
            }
        });
        get("/", HomeView.handleHomeGet, new VelocityTemplateEngine());
        get("/login", LoginView.handleLoginGet, new VelocityTemplateEngine());
        get("/settings", SettingsView.handleSettingsGet, new VelocityTemplateEngine());
        post("/settings", SettingsView.handleSettingsPost);
        post("/login", LoginView.handleLoginPost);
        post("/logout", LoginView.handleLogoutPost);
        get("/logout", LoginView.handleLogoutPost);
        post("/upload", UploadView.handleUploadPost);
        post("/clarification", ClarificationView.handleClarificationPost);
        post("/clarification-response", ClarificationView.handleClarificationResponsePost);
        SentinelModel.save();
        
        Thread autoJudge = new Thread(new AutoJudge());
        autoJudge.run();
    }
    
}
