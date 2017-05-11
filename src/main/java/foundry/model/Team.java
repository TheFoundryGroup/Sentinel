package foundry.model;

public class Team {

    private String name;
    private String password;
    
    public Team(String name, String password) {
        this.name = name;
        this.password = password;
    }
    
    public String getName() {
        return name;
    }
    
    public String getPassword() {
        return password;
    }
    
}
