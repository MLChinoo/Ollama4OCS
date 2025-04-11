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

public final class CompletionHandler extends QuestionHandler {
    public CompletionHandler(Question question) {
        super(question,
                "简答题",
                "回答要求:请简要回答你对这个问题的看法与见解，要求逻辑通顺、言之有理，适当缩减语言。\n回答示例:\"对于这个问题，我的看法是.../我们应该做...\"\n",
                new FormatStructure(new HashMap<>() {{
                    put("answer-that-should-be-logical-reasonable-concise", new HashMap<String, Object>() {
                        {
                            put("type", "string");
                        }
                    });
                }}));
    }

    @Override
    protected String handle(OllamaResult result) {
        Map<String, String> ollamaRsp = gson.fromJson(StringEscapeUtils.unescapeJava(result.getResponse()), new TypeToken<HashMap<String, String>>() {}.getType());
        String response = ollamaRsp.get("answer-that-should-be-logical-reasonable-concise").strip();
        return gson.toJson(List.of(response));
    }
}
