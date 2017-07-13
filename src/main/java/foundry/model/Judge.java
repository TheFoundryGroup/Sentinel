package foundry.model;

import static foundry.Utilities.generateWebsocketAuth;

public class Judge {

    private String password;
    private transient long websocketAuth;
    
    public Judge(String password) {
        this.password = password;
    }
    
    public String getPassword() {
        return password;
    }
    
    public long getWebsocketAuth() {
        if (websocketAuth==0) websocketAuth = generateWebsocketAuth();
        return websocketAuth;
    }
    
}
