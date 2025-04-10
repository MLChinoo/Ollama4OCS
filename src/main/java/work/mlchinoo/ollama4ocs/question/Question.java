package work.mlchinoo.ollama4ocs.question;

import lombok.Getter;
import work.mlchinoo.ollama4ocs.question.handlers.*;

public class Question {
    @Getter private String type;
    @Getter private String question;
    @Getter private String options;
    @Getter private QuestionHandler handler;
    public Question(String type, String question, String options) {
        this.type = type;
        this.question = question;
        this.options = (options == null) ? "" : options.replaceAll("\n\n", "").strip();
        switch (type) {
            case "single" -> this.handler = new SingleChoiceHandler(this);
            case "multiple" -> this.handler = new MultipleChoicesHandler(this);
            case "judgement" -> this.handler = new JudgementHandler(this);
            case "fill-blank" -> this.handler = new FillBlankHandler(this);
            case "completion" -> this.handler = new CompletionHandler(this);
            default -> throw new IllegalArgumentException("Unsupported question type: " + type);
        }
    }
}
