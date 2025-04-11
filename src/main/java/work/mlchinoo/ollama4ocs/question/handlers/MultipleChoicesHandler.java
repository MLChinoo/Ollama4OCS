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

public final class MultipleChoicesHandler extends QuestionHandler {
    public MultipleChoicesHandler(Question question) {
        super(question,
                "多选题",
                "回答要求:请分别直接回答多选题的多个最符合题意的选项本身，包含选项序号与选项内容。\n回答示例:[\"A.第一个正确选项\",\"C.第二个正确选项\",...,\"Z.第n个正确选项\"](此题的答案最少有两个选项)\n",
                new FormatStructure(new HashMap<>() {{
                    put("answers-with-only-option-label-and-text-without-any-explanation", new HashMap<String, Object>() {
                        {
                            put("type", "array");
                            put("items", new HashMap<String, Object>() {{
                                put("type", "string");
                            }});
                        }
                    });
                }}));
    }

    @Override
    protected String handle(OllamaResult result) {
        Map<String, List<String>> ollamaRsp = gson.fromJson(StringEscapeUtils.unescapeJava(result.getResponse()), new TypeToken<HashMap<String, Object>>() {}.getType());
        List<String> response = ollamaRsp.get("answers-with-only-option-label-and-text-without-any-explanation");
        return gson.toJson(response);
    }
}
