package work.mlchinoo.ollama4ocs;

import lombok.Getter;
import work.mlchinoo.ollama4ocs.javabeans.FormatStructure;
import work.mlchinoo.ollama4ocs.javabeans.QuestionInfo;

import java.util.HashMap;
import java.util.Map;

public class Config {
    @Getter
    private static final String host = "http://localhost:11434";
    @Getter
    private static final String model = "deepseek-r1:7b";
    @Getter
    private static final Map<String, QuestionInfo> typeSheet = Map.of(
            "single", new QuestionInfo(
                    "单选题", "\n回答要求:请返回一个包含多个元素的数组,第一个元素只能回答选项本身,第二个元素需填入回答理由\n例子:[\"A.这是示例选项\",\"因为这里是回答示例，假设这道题目选择A，所以对于这个示例给出答案A\"](此题的答案有且仅有一个选项)\n",
                    new FormatStructure(new HashMap<>() {{
                        put("answer", new HashMap<String, Object>() {
                            {
                                put("type", "string");
                                put("description", "答题选项，只返回选项本身，例如 'A.这是示例选项'");
                                put("pattern", "^[A-Z]\\..+");
                            }
                        });
                        put("reasons", new HashMap<String, Object>() {
                            {
                                put("type", "string");
                                put("description", "回答理由，说明选择该选项的原因");
                            }
                        });

                    }}))
            , "multiple", new QuestionInfo(
                    "多选题", "\n回答要求:请返回一个包含若干元素的数组,第一个元素需填入多选题的多个选项,并用#号隔离,后续若干元素则需填入选择对应选项的理由,,\n多选题例子:[\"选项1#选项2#...#选项n\",\"选择选项1的理由\",\"选择选项2的理由\",...,\"选择选项n的理由\"](最少为俩个选项,选项和选择理由都要在一个数组中,被选择的选项们必须单独放在数组的第一个元素中并以#号隔开,一次回答最多返回一个数组)\n",
                    new FormatStructure(new HashMap<>() {{
                        put("answer", new HashMap<String, Object>() {
                            {
                                put("type", "string");
                                put("description", "多选题的选项，多个选项使用#号隔开，至少包含两个选项");
                                put("pattern", "^[^#]+(#[^#]+)+$");
                            }
                        });
                        put("reasons", new HashMap<String, Object>() {
                            {
                                put("type", "array");
                                put("description", "各个选项对应的回答理由，顺序与选项顺序一致");
                                put("items", new HashMap<String, Object>() {
                                    {
                                        put("type", "string");
                                    }
                                });
                                put("minItems", 2);
                            }
                        });

                    }}))
            , "judgement", new QuestionInfo(
                    "判断题", "\n回答要求:请返回一个包含多个元素的数组,第一个元素只能回答选项本身,第二个元素需填入回答理由\n例子:[\"A.对\",\"因为1+2=3\"]\n",
                    new FormatStructure(new HashMap<>() {{
                        put("answer", new HashMap<String, Object>() {
                            {
                                put("type", "string");
                                put("description", "判断题选项，仅返回选项本身，例如 'A.对'");
                                put("pattern", "^[A-Z]\\..+");
                            }
                        });
                        put("reasons", new HashMap<String, Object>() {
                            {
                                put("type", "string");
                                put("description", "回答理由，例如 '因为1+2=3'");
                            }
                        });
                    }}))
            , "fill-blank", new QuestionInfo(
                    "填空题", "\n回答要求:请返回一个包含多个元素的数组,每一个元素都需填入空缺处的内容\n例子:[\"第1个空缺处的内容\",\"第2个空缺处的内容\",...,\"第n个空缺处的内容\"]\n",
                    new FormatStructure(new HashMap<>() {{
                        put("answer", new HashMap<String, Object>() {
                            {
                                put("type", "array");
                                put("description", "每个空缺处填入的内容，顺序与空缺处的顺序一致");
                                put("items", new HashMap<String, Object>() {
                                    {
                                        put("type", "string");
                                    }
                                });
                                put("minItems", 2);
                            }
                        });
                    }}))
            , "completion", new QuestionInfo(
                    "简答题", "\n回答要求:请返回一个包含单个元素的数组,元素中需填入这道简答题的答案\n例子:[\"简答题答案\"]\n",
                    new FormatStructure(new HashMap<>() {{
                        put("answer", new HashMap<String, Object>() {
                            {
                                put("type", "string");
                                put("description", "简答题的回答内容");
                            }
                        });
                    }}))
    );
}
