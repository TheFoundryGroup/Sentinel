package foundry.model;

public class AuthHelper {
    
    public static AuthResult login(String name, String password) {
        if (FoundryModel.hasTeam(name) && FoundryModel.getTeam(name).getPassword().equals(password)) return AuthResult.TEAM;
        if (FoundryModel.hasJudge(name) && FoundryModel.getJudge(name).getPassword().equals(password)) return AuthResult.JUDGE;
        if (FoundryModel.hasTeam(name) || FoundryModel.hasJudge(name)) return AuthResult.FAILED_PASSWORD;
        return AuthResult.FAILED_NAME;
    }
    
}
