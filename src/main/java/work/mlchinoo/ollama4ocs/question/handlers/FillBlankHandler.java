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

public final class FillBlankHandler extends QuestionHandler {
    public FillBlankHandler(Question question) {
        super(question,
                "填空题",
                "回答要求:请分别直接回答每个空缺处可以填入的最符合题意的内容，不需要填入上下题干与答案解释，确保你填入的内容能和上下文组成完整通顺的句子。\n回答示例:[\"第1个空缺处的内容\",\"第2个空缺处的内容\",...,\"第n个空缺处的内容\"]\n",
                new FormatStructure(new HashMap<>() {{
                    put("answers-in-each-blank-separately", new HashMap<String, Object>() {
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
        List<String> response = ollamaRsp.get("answers-in-each-blank-separately");
        return gson.toJson(response);
    }
}
