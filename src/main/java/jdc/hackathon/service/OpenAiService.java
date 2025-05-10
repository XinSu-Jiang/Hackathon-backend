package jdc.hackathon.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OpenAiService {

    private final WebClient openAiWebClient;
    private final String model = "gpt-3.5-turbo"; // application.yml 과 일치시켜도 좋습니다

    public String summarizeAnalysis(List<Map<String, String>> messages) {
        Map<String, Object> body = Map.of(
                "model", model,
                "messages", messages,
                "temperature", 0.7
        );

        // Mono blocking 예시
        Map<?, ?> resp = openAiWebClient.post()
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        // 응답 구조: choices[0].message.content
        var choices = (List<Map<String, Object>>) resp.get("choices");
        if (choices != null && !choices.isEmpty()) {
            var message = (Map<String, Object>) choices.get(0).get("message");
            return (String) message.get("content");
        }
        return "분석 결과가 없습니다.";
    }
}