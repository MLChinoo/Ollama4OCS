package work.mlchinoo.ollama4ocs;

import com.google.gson.Gson;
import io.github.ollama4j.OllamaAPI;
import io.github.ollama4j.exceptions.OllamaBaseException;
import io.github.ollama4j.models.response.OllamaResult;
import io.github.ollama4j.utils.OptionsBuilder;
import io.javalin.Javalin;
import io.javalin.http.Context;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import work.mlchinoo.ollama4ocs.javabeans.OCSResponse;
import work.mlchinoo.ollama4ocs.javabeans.QuestionInfo;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final Gson gson = new Gson();
    private static final OllamaAPI api = new OllamaAPI(Config.getHost());

    public static void handle(Context ctx) {
        String question = ctx.queryParam("question");
        String type = ctx.queryParam("type");
        String options = ctx.queryParam("options");

        QuestionInfo questionInfo = Config.getTypeSheet().getOrDefault(type, Config.getTypeSheet().get("single"));
        StringBuilder promptBuilder = new StringBuilder();
        promptBuilder.append("这道题的类型是:[%s]\n题目为:%s".formatted(questionInfo.getType(), question));
        if (List.of("单选题", "多选题", "判断题").contains(questionInfo.getType())) {
            promptBuilder.append("\n选项为:\n%s".formatted(options.replaceAll("\n\n", "")));
        }
        promptBuilder.append(questionInfo.getQuestionPrompt());

        //CompletableFuture.supplyAsync(() -> {
        String result1 = null;
        try {
            OllamaResult result = api.generate(Config.getModel(), promptBuilder.toString(), false, new OptionsBuilder()
                    .setCustomOption("format", gson.toJson(questionInfo.getFormat().getFormat()))
                    .build());
            String response = StringEscapeUtils.unescapeJava(result.getResponse())
                    // DeepSeek-R1 等推理模型会在开头输出<think>思考段，需要去除
                    .replaceFirst("<think>[\\s\\S]*?</think>", "")
                    .replaceAll("\\n", "").strip();
            //return response;
            result1 = response;
        } catch (OllamaBaseException | InterruptedException | IOException e) {
            logger.error(e.getMessage());
        }
        //return null;
        //}).thenAccept(result1 -> {
        logger.info(result1);
        ctx.result(gson.toJson(OCSResponse.builder()
                .GPTStatus(200)
                .title(question)
                .answer(result1)
                .build()));
    //});

    }

    public static void main(String[] args) {
        logger.info("Pinging Ollama API on %s ...".formatted(Config.getHost()));
        // api.setVerbose(true);
        api.setRequestTimeoutSeconds(120);
        logger.info("Is Ollama server running: %b".formatted(api.ping()));
        logger.info("Starting Ollama4OCS...");
        try {
            var app = Javalin.create(/*config*/)
                    .get("/api/qa", Main::handle)
                    .start(7070);
            logger.info("Successfully started on port %d".formatted(app.port()));
        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.error("Failed to start Ollama4OCS, omg");
        }
    }
}
