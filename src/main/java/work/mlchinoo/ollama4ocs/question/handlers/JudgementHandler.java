package work.mlchinoo.ollama4ocs.question.handlers;

import com.google.gson.reflect.TypeToken;
import io.github.ollama4j.models.response.OllamaResult;
import org.apache.commons.text.StringEscapeUtils;
import work.mlchinoo.ollama4ocs.question.FormatStructure;
import work.mlchinoo.ollama4ocs.question.Question;
import work.mlchinoo.ollama4ocs.question.QuestionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class JudgementHandler extends QuestionHandler {
    public JudgementHandler(Question question) {
        super(question,
                "判断题",
                "回答要求:请直接回答选项本身，包含选项序号与选项内容。\n回答示例:\"A.对\"\n(重要提醒：此题的答案有且仅有一个选项！！)\n",
                new FormatStructure(new HashMap<>() {{
                    put("answer-with-only-option-label-and-text-without-any-explanation", new HashMap<String, Object>() {
                        {
                            put("type", "string");
                        }
                    });
                }}));
    }

    @Override
    protected String handle(OllamaResult result) {
        Map<String, String> ollamaRsp = gson.fromJson(StringEscapeUtils.unescapeJava(result.getResponse()), new TypeToken<HashMap<String, String>>() {}.getType());
        String response = ollamaRsp.get("answer-with-only-option-label-and-text-without-any-explanation").strip();
        return gson.toJson(List.of(response));
    }
}
