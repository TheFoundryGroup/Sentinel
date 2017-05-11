package foundry.model;

public class Judge {
    
    private String name;
    private String password;
    
    public Judge(String name, String password) {
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
