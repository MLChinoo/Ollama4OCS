package work.mlchinoo.ollama4ocs.gson;

import lombok.Builder;
import lombok.Getter;

@Builder public class OCSResponse {
    @Getter private final int GPTStatus;
    @Getter private final String title;
    @Getter private final String answer;

    public OCSResponse(int gptStatus, String title, String answer) {
        this.GPTStatus = gptStatus;
        this.title = title;
        this.answer = answer;
    }
}
