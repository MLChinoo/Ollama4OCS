package work.mlchinoo.ollama4ocs.javabeans;

import lombok.Getter;

public class QuestionInfo {
    @Getter private final String type;
    @Getter private final String questionPrompt;
    @Getter private final FormatStructure format;

    public QuestionInfo(String type, String questionPrompt, FormatStructure format) {
        this.type = type;
        this.questionPrompt = questionPrompt;
        this.format = format;
    }
}
