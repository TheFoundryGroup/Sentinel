package foundry.model;

public class SettingWrapper {
    public String name;
    public String description;
    public Object value;
    
    public SettingWrapper(String name, String description, Object value) {
        this.name = name;
        this.description = description;
        this.value = value;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public Object getValue() {
        return value;
    }
}