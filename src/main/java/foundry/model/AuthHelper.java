package foundry.model;

public class AuthHelper {
    
    public static AuthResult login(String name, String password) {
        if (SentinelModel.hasTeam(name) && SentinelModel.getTeam(name).getPassword().equals(password)) return AuthResult.TEAM;
        if (SentinelModel.hasJudge(name) && SentinelModel.getJudge(name).getPassword().equals(password)) return AuthResult.JUDGE;
        if (SentinelModel.hasTeam(name) || SentinelModel.hasJudge(name)) return AuthResult.FAILED_PASSWORD;
        return AuthResult.FAILED_NAME;
    }
    
}
