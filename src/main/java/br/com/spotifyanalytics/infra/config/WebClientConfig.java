package br.com.spotifyanalytics.infra.config;

import io.netty.channel.ChannelOption;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;


import java.time.Duration;

@Configuration
public class WebClientConfig
{
    @Bean
    public WebClient ms2WebClient(@Value("${ms2.url}") String ms2Url) {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 2000)
                .responseTimeout(Duration.ofSeconds(3));

        return WebClient.builder()
                .baseUrl(ms2Url)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

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
