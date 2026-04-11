package br.com.spotifyanalytics.application.usecases;

import br.com.spotifyanalytics.application.dto.SpotifyUser;
import br.com.spotifyanalytics.application.dto.TokenResponse;
import br.com.spotifyanalytics.application.dto.TopArtistsResponse;
import br.com.spotifyanalytics.application.service.SpotifyServiceImpl;
import br.com.spotifyanalytics.infra.config.WebClientConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Base64;

@Service
public class SpotifyService implements SpotifyServiceImpl
{
    private final WebClient spotifyApiWebClient;
    private final WebClient spotifyAuthWebClient;
    @Value("4501e1f06a704af1a78fec28e752d898")
    private String clientId;
    @Value("8f01c6631d6b4a5091e0973e3fe6a950")
    private String clientSecret;
    @Value("http://127.0.0.1:8080/auth/callback")
    private String redirectUri;

    public SpotifyService(
            @Qualifier("spotifyApiWebClient") WebClient spotifyApiWebClient,
            @Qualifier("spotifyAuthWebClient") WebClient spotifyAuthWebClient
    ) {
        this.spotifyApiWebClient = spotifyApiWebClient;
        this.spotifyAuthWebClient = spotifyAuthWebClient;
    }


    @Override
    public String getLoginUrl() {
        return "https://accounts.spotify.com/authorize" +
                "?client_id=" + clientId +
                "&response_type=code" +
                "&redirect_uri=" + redirectUri +
                "&scope=user-top-read user-read-email";
    }

    @Override
    public TokenResponse getToken(String code) {
        String basicAuth = Base64.getEncoder()
                .encodeToString((clientId + ":" + clientSecret).getBytes());

        return spotifyAuthWebClient.post()
                .uri("/api/token")
                .header("Authorization", "Basic " + basicAuth)
                .bodyValue("grant_type=authorization_code" +
                        "&code=" + code +
                        "&redirect_uri=" + redirectUri)
                .retrieve()
                .bodyToMono(TokenResponse.class)
                .block();
    }

    @Override
    public SpotifyUser getUser(String accessToken) {
        return spotifyApiWebClient.get()
                .uri("/v1/me")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(SpotifyUser.class)
                .block();
    }

    public TopArtistsResponse getTopArtists(String accessToken) {

        return spotifyApiWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/me/top/artists")
                        .queryParam("limit", 10)
                        .build())
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(TopArtistsResponse.class)
                .block();
    }
}
