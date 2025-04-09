package work.mlchinoo.ollama4ocs.javabeans;

import lombok.Builder;

@Builder public class OCSResponse {
    private final int GPTStatus;
    private final String title;
    private final String answer;

    public OCSResponse(int gptStatus, String title, String answer) {
        this.GPTStatus = gptStatus;
        this.title = title;
        this.answer = answer;
    }
}
