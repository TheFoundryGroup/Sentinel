package foundry.views;

import foundry.model.*;
import spark.QueryParamsMap;
import spark.Route;

import static spark.Spark.halt;

public class ClarificationView {
    
    public static Route handleClarificationPost = (req, res) -> {
        QueryParamsMap params = req.queryMap();
        String from = req.session().attribute("loggedIn");
        Team team = SentinelModel.getTeam(from);
        
        String ps = params.value("problem");
        String message = params.value("message");
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
    
    public static Route handleClarificationResponsePost = (req, res) -> {
        QueryParamsMap params = req.queryMap();
        String from = req.session().attribute("loggedIn");
        if (!(boolean)req.session().attribute("judge")) throw halt(403);
        Judge judge = SentinelModel.getJudge(from);
        
        boolean global = !(params.value("global")==null) && params.value("global").equals("on");
    
        int id = Integer.parseInt(params.value("id"));
        String message = params.value("message");
        Clarification clar = SentinelModel.getClarification(id);
        if (message==null || message.equals("") || clar==null) {
            System.out.println("malformed request");
            return "";
        }
    
        clar.respond(message, global);
        
        SentinelModel.saveClarifications();
    
        return "";
    };
    
}
