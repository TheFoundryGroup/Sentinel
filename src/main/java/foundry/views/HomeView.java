package foundry.views;

import spark.ModelAndView;
import spark.TemplateViewRoute;

import java.util.HashMap;

import static foundry.Utilities.generateModel;

public class HomeView {
    
    public static TemplateViewRoute handleHomeGet = (req, res) -> {
        HashMap<String, Object> model = generateModel(req);
        model.put("home", true);

        return new ModelAndView(model, "templates/home.vtl");
    };
    
}
