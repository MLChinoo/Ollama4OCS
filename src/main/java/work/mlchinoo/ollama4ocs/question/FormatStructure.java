package work.mlchinoo.ollama4ocs.question;

import lombok.Getter;

import java.util.*;

public class FormatStructure {
    private String type = "object";
    private Map<String, Object> properties = new HashMap<>();
    private List<String> required = new ArrayList<>();
    @Getter private Map<String, Object> format = new HashMap<>();
    public FormatStructure(Map<String, Object> properties) {
        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            String name = entry.getKey();
            if (entry.getValue() instanceof Map) {
                this.properties.put(name, entry.getValue());
            } else {continue;}
            this.required.add(name);
        }
        this.format.put("type", this.type);
        this.format.put("properties", this.properties);
        this.format.put("required", this.required);
    }
}
