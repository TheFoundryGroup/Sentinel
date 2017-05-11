package foundry.views;

import foundry.model.AuthHelper;
import foundry.model.AuthResult;
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Route;
import spark.TemplateViewRoute;

import java.util.HashMap;
import java.util.Map;

public class LoginView {
    
    public static TemplateViewRoute handleLoginGet = (req, res) -> {
        if (req.session().attribute("loggedIn")!=null) res.redirect("/");
        HashMap<String, Object> model = new HashMap<>();
        model.put("failed", req.queryParams("failed"));
        return new ModelAndView(model, "templates/login.vtl");
    };
    
    public static Route handleLoginPost = (req, res) -> {
        QueryParamsMap params = req.queryMap();
        String redirectEmptyField = "/login?failed=empty";
        String redirectFailedName = "/login?failed=name";
        String redirectFailedPassword = "/login?failed=password";
        String redirectSuccess = "/";
        if (params.hasKey("name") && params.hasKey("password")) {
            String name = params.value("name");
            AuthResult auth = AuthHelper.login(name, params.value("password"));
            if (auth==AuthResult.JUDGE) {
                req.session().attribute("loggedIn", name);
                req.session().attribute("judge", true);
                res.redirect(redirectSuccess);
            } else if (auth==AuthResult.TEAM) {
                req.session().attribute("loggedIn", name);
                req.session().attribute("judge", false);
                res.redirect(redirectSuccess);
            } else
                res.redirect(auth==AuthResult.FAILED_PASSWORD ? redirectFailedPassword : redirectFailedName);
        } else res.redirect(redirectEmptyField);
        return "Login Page";
    };
    
    public static Route handleLogoutPost = (req, res) -> {
        req.session().invalidate();
        res.redirect("/");
        return "";
    };
    
}
