package work.mlchinoo.ollama4ocs.javabeans;

import lombok.Getter;

import java.util.*;

public class FormatStructure {
    @Getter private final String type = "array";
    @Getter private final Map<String, Object> properties = new HashMap<>();
    @Getter private final List<String> required = new ArrayList<>();
    @Getter private final Map<String, Object> format = new HashMap<>();
    public FormatStructure(Map<String, Object> customProperties) {
        // nameTypeEntries接受多个name-type键值对，如age-integer, available-boolean
        for (Map.Entry<String, Object> entry : customProperties.entrySet()) {
            String name = entry.getKey();
            if (entry.getValue().getClass().equals(String.class)) {
                properties.put(name, new HashMap<String, Object>() {{
                    put("type", entry.getValue());
                }});
            } else if (entry.getValue() instanceof Map) {
                properties.put(name, entry.getValue());
            } else {continue;}
            required.add(name);
        }
        format.put("type", type);
        format.put("properties", properties);
        format.put("required", required);
    }
}
