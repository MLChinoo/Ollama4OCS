package work.mlchinoo.ollama4ocs;

import lombok.Getter;

public class Config {
    @Getter private static final String host = "http://localhost:11434";
    @Getter private static final String model = "deepseek-r1:7b";
}
