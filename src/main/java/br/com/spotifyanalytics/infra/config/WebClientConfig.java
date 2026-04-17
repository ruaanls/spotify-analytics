package br.com.spotifyanalytics.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig
{
    @Bean
    public WebClient spotifyApiWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl("https://api.spotify.com")
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    @Bean
    public WebClient spotifyAuthWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl("https://accounts.spotify.com")
                .defaultHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
    }
}
