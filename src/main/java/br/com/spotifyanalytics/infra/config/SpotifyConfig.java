package br.com.spotifyanalytics.infra.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import se.michaelthelin.spotify.SpotifyApi;

@Configuration
public class SpotifyConfig
{
    @Value("4501e1f06a704af1a78fec28e752d898")
    private String clientId;
    @Value("8f01c6631d6b4a5091e0973e3fe6a950")
    private String clientSecret;

    @Bean
    public SpotifyApi spotifyApi()
    {
        return new SpotifyApi.Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .build();
    }

}
