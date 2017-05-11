package foundry.views;

import spark.ModelAndView;
import spark.Route;
import spark.TemplateViewRoute;

import java.util.HashMap;

public class LoginView {
    
    public static TemplateViewRoute handleLoginGet = (req, res) -> {
        if (req.session().attribute("loggedIn")!=null) res.redirect("/");
        return new ModelAndView(new HashMap<>(), "templates/login.vtl");
    };
    
    public static Route handleLoginPost = (req, res) -> {
        System.out.println("loginpost");
        req.session().attribute("loggedIn", true);
        res.redirect("/");
        return "";
    };
    
    public static Route handleLogoutPost = (req, res) -> {
        req.session().attribute("loggedIn", false);
        res.redirect("/");
        return "";
    };
    
}
