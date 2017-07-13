package foundry;

import foundry.model.SentinelModel;
import spark.Request;

import java.util.HashMap;
import java.util.Random;

public class Utilities {
    
    public static HashMap<String, Object> generateModel(Request req) {
        
        //halt request if the user isn't logged in. messy, but there's not a better way.
        if (req.session().attribute("loggedIn")==null) throw new NotLoggedInException();
        
        HashMap<String, Object> model = new HashMap<>();

        String loggedIn = req.session().attribute("loggedIn");
        
        boolean judge = req.session().attribute("judge");
        
        model.put("counter", 0);
        
        model.put("loggedIn", loggedIn);
        model.put("judge", judge);
        model.put("problems", SentinelModel.getProblems());
        
        if (!(boolean)model.get("judge")) {
            model.put("websocketAuth", SentinelModel.getTeam(loggedIn).getWebsocketAuth());
        } else {
            model.put("websocketAuth", SentinelModel.getJudge(loggedIn).getWebsocketAuth());
        }
        
        return model;
    }
    
    public static long generateWebsocketAuth() {
        return new Random().nextLong();
    }
    
}

class NotLoggedInException extends RuntimeException {

}