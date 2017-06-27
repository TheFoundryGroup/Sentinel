package foundry;

import foundry.model.SentinelModel;
import foundry.views.HomeView;
import foundry.views.LoginView;
import foundry.views.WebsocketHandler;
import foundry.views.UploadView;
import spark.template.velocity.VelocityTemplateEngine;

import static spark.Spark.*;

public class Application {
    
    public static void main(String[] args) {
        port(80);
        webSocket("/websocket", WebsocketHandler.class);
        staticFileLocation("public");
        before((req, res) -> {
            if (req.session().attribute("loggedIn")==null && (!req.pathInfo().equals("/login") && !req.pathInfo().equals("/login/"))) {
                res.redirect("login", 303);
            }
        });
        get("/", HomeView.handleHomeGet, new VelocityTemplateEngine());
        get("/login", LoginView.handleLoginGet, new VelocityTemplateEngine());
        post("/login", LoginView.handleLoginPost);
        post("/logout", LoginView.handleLogoutPost);
        post("/upload", UploadView.handleUploadPost);
        SentinelModel.save();
    }
    
}
