package foundry.views;

import foundry.model.*;
import spark.QueryParamsMap;
import spark.Route;

public class ClarificationView {
    
    public static Route handleClarificationPost = (req, res) -> {
        QueryParamsMap params = req.queryMap();
        String from = req.session().attribute("loggedIn");
        Team team = SentinelModel.getTeam(from);
        
        String ps = params.value("problem");
        String message = params.value("message");
        System.out.println(ps);
        if (ps==null || ps.equals("") || message==null || message.equals("") || SentinelModel.getProblem(params.value("problem"))==null) {
            System.out.println("malformed request");
            return "";
        }
        
        Problem problem = SentinelModel.getProblem(params.value("problem"));
        
        Clarification c = new Clarification(from, ps, message);
        SentinelModel.addClarification(c);
        SentinelModel.saveClarifications();
        
        return "";
    };
    
}
