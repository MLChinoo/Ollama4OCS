package work.mlchinoo.ollama4ocs.question;

import com.google.gson.Gson;
import io.github.ollama4j.OllamaAPI;
import io.github.ollama4j.exceptions.OllamaBaseException;
import io.github.ollama4j.models.response.OllamaResult;
import lombok.Getter;
import work.mlchinoo.ollama4ocs.Config;
import work.mlchinoo.ollama4ocs.question.handlers.*;

import java.io.IOException;


public abstract class QuestionHandler {
    protected static final Gson gson = new Gson();
    @Getter protected Question question;
    protected String typeTranslated;
    protected String prompt;
    protected FormatStructure format;

    public static QuestionHandler getHandler(Question question) {
        return switch (question.getType()) {
            case "single" -> new SingleChoiceHandler(question);
            case "multiple" -> new MultipleChoicesHandler(question);
            case "judgement" -> new JudgementHandler(question);
            case "fill-blank" -> new FillBlankHandler(question);
            case "completion" -> new CompletionHandler(question);
            default -> throw new IllegalArgumentException("Unsupported question type: " + question.getType());
        };
    }

    public QuestionHandler(Question question, String typeTranslated, String prompt, FormatStructure format) {
        this.question = question;
        this.typeTranslated = typeTranslated;
        this.prompt = new StringBuilder()
                .append("你作为一名正在线上学习的天资聪颖、天赋异禀的学生，需要精准正确地按照要求回答下面的问题：\n")
                .append("这道题的类型是:[%s]\n题目为:%s\n".formatted(typeTranslated, question.getQuestion()))
                .append((question.getOptions().isEmpty())?
                        (""):
                        ("选项为:\n%s".formatted(question.getOptions())))
                .append(prompt).append("\n")
                .append("")
                .toString();
        this.format = format;
    }

    public String generate(OllamaAPI api) throws OllamaBaseException, IOException, InterruptedException {
        OllamaResult result = api.generate(Config.getModel(), this.prompt, this.format.getFormat());
        return handle(result);
    }

    protected abstract String handle(OllamaResult result);  // 返回结构化后转为 JSON 的答案
}
