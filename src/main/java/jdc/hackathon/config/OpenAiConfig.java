package jdc.hackathon.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class OpenAiConfig {

    @Value("${openai.api-url}")
    private String apiUrl;

    @Bean
    public WebClient openAiWebClient(@Value("${openai.api-key}") String apiKey) {
        return WebClient.builder()
                .baseUrl(apiUrl)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .build();
    }
}