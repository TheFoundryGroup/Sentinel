package foundry.model;

import java.util.List;

public class Problem {
    
    private List<String> expected;
    private String input;
    private String name;
    private String description;
    
    public Problem(List<String> expected, String input, String name, String description) {
        this.expected = expected;
        this.input = input;
        this.name = name;
        this.description = description;
    }
    
    public List<String> getExpected() {
        return expected;
    }
    
    public void setExpected(List<String> expected) {
        this.expected = expected;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getInput() {
        return input;
    }
    
    public void setInput(String input) {
        this.input = input;
    }
}
