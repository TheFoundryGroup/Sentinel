package foundry.model;

public class AuthHelper {
    
    public static AuthResult login(String name, String password) {
        if (FoundryModel.getTeam(name).getPassword().equals(password)) return AuthResult.TEAM;
        if (FoundryModel.getJudge(name).getPassword().equals(password)) return AuthResult.JUDGE;
        return AuthResult.FAILED;
    }
    
}
