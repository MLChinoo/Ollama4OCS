package work.mlchinoo.ollama4ocs.question;

import lombok.Getter;

public class Question {
    @Getter private String type;
    @Getter private String question;
    @Getter private String options;
    @Getter private QuestionHandler handler;
    public Question(String type, String question, String options) {
        this.type = type;
        this.question = question;
        this.options = (options == null) ? "" : options.replaceAll("\n\n", "").strip();
        this.handler = QuestionHandler.getHandler(this);
    }
}
