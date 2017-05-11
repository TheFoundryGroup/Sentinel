package foundry;

import foundry.model.FoundryModel;
import foundry.model.Judge;
import foundry.views.HomeView;
import foundry.views.LoginView;
import spark.template.velocity.VelocityTemplateEngine;

import static spark.Spark.*;

public class Application {
    
    public static void main(String[] args) {
        port(80);
        staticFileLocation("public");
        before((req, res) -> {
            if (req.session().attribute("loggedIn")==null && (!req.pathInfo().equals("/login") && !req.pathInfo().equals("/login/"))) {
                res.redirect("login");
            }
        });
        get("/", HomeView.handleHomeGet, new VelocityTemplateEngine());
        get("/login", LoginView.handleLoginGet, new VelocityTemplateEngine());
        post("/login", LoginView.handleLoginPost);
        post("/logout", LoginView.handleLogoutPost);
    }
    
}
