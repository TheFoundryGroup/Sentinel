package foundry.views;

import spark.ModelAndView;
import spark.TemplateViewRoute;

import java.util.HashMap;

public class HomeView {
    
    public static TemplateViewRoute handleHomeGet = (req, res) -> {
        HashMap<String, Object> model = new HashMap<>();
        if (req.session().attribute("loggedIn")!=null) {
            model.put("loggedIn", req.session().attribute("loggedIn"));
            model.put("judge", req.session().attribute("judge"));
        }
        return new ModelAndView(model, "templates/home.vtl");
    };
    
}
