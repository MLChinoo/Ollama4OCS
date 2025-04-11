package work.mlchinoo.ollama4ocs;

import com.google.gson.Gson;
import io.github.ollama4j.OllamaAPI;
import io.javalin.Javalin;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import work.mlchinoo.ollama4ocs.gson.OCSResponse;
import work.mlchinoo.ollama4ocs.question.Question;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final Gson gson = new Gson();
    private static final OllamaAPI api = new OllamaAPI(Config.getHost());

    public static void handle(Context ctx) {
        String _type = Objects.requireNonNull(ctx.queryParam("type"));
        String _question = Objects.requireNonNull(ctx.queryParam("question"));
        String _options = ctx.queryParam("options");

        Question question = new Question(_type, _question, _options);
        logger.debug("{} Request: {}", ctx, question.getQuestion());

//        ctx.future(() -> CompletableFuture.supplyAsync(() -> {
//            try {
//                String answer = question.getHandler().generate(api);
//                logger.debug("{} Response: {}", ctx, answer);
//                return OCSResponse.builder()
//                        .GPTStatus(200)
//                        .title(question.getQuestion())
//                        .answer(answer)
//                        .build();
//            } catch (Exception e) {
//                logger.error("{} failed: {}", ctx, e.getMessage());
//                return OCSResponse.builder()
//                        .GPTStatus(400)
//                        .title(question.getQuestion())
//                        .answer(e.getMessage())
//                        .build();
//            }
//        }).thenApply(gson::toJson));

        OCSResponse response;
        try {
            String answer = question.getHandler().generate(api);
            logger.debug("{} Response: {}", ctx, answer);
            response =  OCSResponse.builder()
                    .GPTStatus(200)
                    .title(question.getQuestion())
                    .answer(answer)
                    .build();
        } catch (Exception e) {
            logger.error("{} failed: {}", ctx, e.getMessage());
            response =  OCSResponse.builder()
                    .GPTStatus(400)
                    .title(question.getQuestion())
                    .answer(e.getMessage())
                    .build();
        }
        ctx.result(gson.toJson(response));
    }

    public static void main(String[] args) {
        logger.info("Pinging Ollama API on %s ...".formatted(Config.getHost()));
        // api.setVerbose(true);
        api.setRequestTimeoutSeconds(60);
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
