package br.com.spotifyanalytics.application.usecases;

import br.com.spotifyanalytics.application.dto.*;
import br.com.spotifyanalytics.application.service.SpotifyServiceImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.ArtistSimplified;
import se.michaelthelin.spotify.model_objects.specification.PlayHistory;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.data.personalization.simplified.GetUsersTopArtistsRequest;
import se.michaelthelin.spotify.requests.data.personalization.simplified.GetUsersTopTracksRequest;
import java.time.Duration;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

import java.util.function.Function;
import java.util.stream.Collectors;

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
                "&scope=user-top-read user-read-email user-read-recently-played";
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
        Track[] topTracks = obterTopTracks("short_term", 20);
        if (topTracks == null || topTracks.length == 0) {
            throw new RuntimeException("Nenhuma faixa encontrada para o período.");
        }

        // 1. Artista mais ouvido (considerando o primeiro artista de cada faixa)
        String topArtist = Arrays.stream(topTracks)
                .map(track -> track.getArtists()[0].getName())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Desconhecido");

        // 2. Álbum mais ouvido
        String topAlbum = Arrays.stream(topTracks)
                .map(track -> track.getAlbum().getName() + " - " + track.getArtists()[0].getName())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Desconhecido");

        String topTrack = Arrays.stream(topTracks)
                .map(Track::getName)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Desconhecido");
        return new EstatisticasFreeDTO(topArtist, topAlbum, topTrack);
    }

    @Override
    public EstatisticasPremiumDTO calculaEstatisticasPagas(String username) {
        String accessToken = getTokenRedis(username, "accessToken");
        spotifyApi.setAccessToken(accessToken);

        Track[] topTracks = obterTopTracks("medium_term", 15);
        Artist[] topArtists = obterTopArtists("medium_term", 15);
        List<PlayHistory> historicoRecente = obterHistoricoRecente(15);

        List<String> top5Artistas = Arrays.stream(topArtists)
                .limit(5)
                .map(Artist::getName)
                .collect(Collectors.toList());

        String faixaMaisPopular = Arrays.stream(topTracks)
                .findFirst()
                .map(track -> track.getName() + " - " + track.getArtists()[0].getName())
                .orElse("Desconhecida");
        String periodoDiaMaisAtivo = calcularPeriodoDiaMaisAtivo(historicoRecente);

        // 🔄 MUDANÇA 3: DTO agora tem apenas três campos simples
        return new EstatisticasPremiumDTO(periodoDiaMaisAtivo, top5Artistas, faixaMaisPopular);
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





    private String buildKey(String id, String type)
    {
        return type+":" + id;
    }


    private Track[] obterTopTracks(String timeRange, int limit) {
        try {
            GetUsersTopTracksRequest request = spotifyApi.getUsersTopTracks()
                    .limit(limit)
                    .time_range(timeRange)
                    .build();
            return request.execute().getItems();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao obter top tracks", e);
        }
    }


    private Artist[] obterTopArtists(String timeRange, int limit) {
        try {
            GetUsersTopArtistsRequest request = spotifyApi.getUsersTopArtists()
                    .limit(limit)
                    .time_range(timeRange)
                    .build();
            return request.execute().getItems();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao obter top artists", e);
        }
    }

    private List<PlayHistory> obterHistoricoRecente(int limite) {
        try {
            return Arrays.asList(
                    spotifyApi.getCurrentUsersRecentlyPlayedTracks()
                            .limit(limite)
                            .build()
                            .execute()
                            .getItems()
            );
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar histórico recente", e);
        }
    }

    private String calcularPeriodoDiaMaisAtivo(List<PlayHistory> historico) {
        Map<String, Long> contagemPeriodos = historico.stream()
                .map(play -> {
                    LocalTime hora = play.getPlayedAt()
                            .toInstant()
                            .atZone(ZoneId.of("America/Sao_Paulo")) // fuso correto
                            .toLocalTime();

                    int h = hora.getHour();

                    if (h >= 6 && h < 12) {
                        return "Manhã (06h-12h)";
                    } else if (h >= 12 && h < 18) {
                        return "Tarde (12h-18h)";
                    } else if (h >= 18 && h < 23) {
                        return "Noite (18h-23h)";
                    } else {
                        return "Madrugada (23h-06h)";
                    }
                })
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        return contagemPeriodos.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Indefinido");
    }

}
