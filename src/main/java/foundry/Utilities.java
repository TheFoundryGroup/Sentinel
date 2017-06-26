package foundry;

import foundry.model.SentinelModel;
import spark.Request;

import java.util.HashMap;

public class Utilities {
    
    public static HashMap<String, Object> generateModel(Request req) {
        HashMap<String, Object> model = new HashMap<>();

        model.put("loggedIn", req.session().attribute("loggedIn"));
        model.put("judge", req.session().attribute("judge"));
        model.put("problems", SentinelModel.getProblems());

        return model;
    }
    
}
