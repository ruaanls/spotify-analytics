package br.com.spotifyanalytics.application.usecases;

import br.com.spotifyanalytics.application.dto.EstatisticasFreeDTO;
import br.com.spotifyanalytics.application.dto.SpotifyUser;
import br.com.spotifyanalytics.application.dto.TokenResponse;
import br.com.spotifyanalytics.application.dto.TopArtistsResponse;
import br.com.spotifyanalytics.application.service.SpotifyServiceImpl;
import br.com.spotifyanalytics.infra.config.WebClientConfig;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.AudioFeatures;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.data.personalization.simplified.GetUsersTopTracksRequest;
import se.michaelthelin.spotify.requests.data.tracks.GetAudioFeaturesForSeveralTracksRequest;

import java.io.IOException;
import java.lang.reflect.Array;
import java.time.Duration;
import java.util.Arrays;
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

    private final SpotifyApi spotifyApi;
    private final RedisTemplate<String, Object> redisTemplate;
    private final Duration TOKEN_TTL = Duration.ofMinutes(58);

    public SpotifyService(
            @Qualifier("spotifyApiWebClient") WebClient spotifyApiWebClient,
            @Qualifier("spotifyAuthWebClient") WebClient spotifyAuthWebClient, SpotifyApi spotifyApi, RedisTemplate<String, Object> redisTemplate
    ) {
        this.spotifyApiWebClient = spotifyApiWebClient;
        this.spotifyAuthWebClient = spotifyAuthWebClient;
        this.spotifyApi = spotifyApi;
        this.redisTemplate = redisTemplate;
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

    @Override
    public EstatisticasFreeDTO calculaEstatisticasFree(String username) {
        String accessToken = getTokenRedis(username,"accessToken");
        spotifyApi.setAccessToken(accessToken);
        Track[] topTracks = obterTopTracks();
        String[] ids = extrairIds(topTracks);
        AudioFeatures[] audioFeatures = obterAudioFeature(ids);

        double energiaMedia = calcularMediaEnergia(audioFeatures);
        double valenciaMedia = calcularMediaValencia(audioFeatures);

        return new EstatisticasFreeDTO(energiaMedia,valenciaMedia);
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

    @Override
    public void saveTokenRedis(String id, String value, String type)
    {
        String key = buildKey(id, type);
        redisTemplate.opsForValue().set(key, value, TOKEN_TTL);
    }

    @Override
    public String getTokenRedis(String id, String type) {
        String key = buildKey(id, type);
        return (String) redisTemplate.opsForValue().get(key);
    }

    @Override
    public void deleteTokenRedis(String id, String type) {
        String key = buildKey(id, type);
        redisTemplate.delete(key);
    }


    private String buildKey(String id, String type)
    {
        return type+":" + id;
    }



    private Track[] obterTopTracks()
    {
        try{
            GetUsersTopTracksRequest request = spotifyApi.getUsersTopTracks()
                    .limit(10)
                    .time_range("medium_term")
                    .build();
            Paging<Track> paging = request.execute();
            return paging.getItems();
        }catch (Exception e) {
            throw new RuntimeException("Erro ao obter top tracks do Spotify", e);
        }
    }


    private AudioFeatures[] obterAudioFeature(String [] ids)
    {
        try
        {
            String idsParam = String.join(",", ids);
            GetAudioFeaturesForSeveralTracksRequest request = spotifyApi
                    .getAudioFeaturesForSeveralTracks(idsParam)
                    .build();
            return request.execute();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao obter audio features do Spotify", e);
        }
    }


    private String[] extrairIds(Track [] tracks)
    {
        return Arrays.stream(tracks)
                .map(Track::getId)
                .toArray(String[]::new);
    }

    private double calcularMediaEnergia(AudioFeatures[] features)
    {
        return Arrays.stream(features)
                .mapToDouble(AudioFeatures::getEnergy)
                .average()
                .orElse(0.0);
    }

    private double calcularMediaValencia(AudioFeatures[] features)
    {
        return Arrays.stream(features)
                .mapToDouble(AudioFeatures::getValence)
                .average()
                .orElse(0.0);
    }


}
