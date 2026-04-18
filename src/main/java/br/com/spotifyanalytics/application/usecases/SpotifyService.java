package br.com.spotifyanalytics.application.usecases;

import br.com.spotifyanalytics.application.dto.*;
import br.com.spotifyanalytics.application.exception.SpotifyApiException;
import br.com.spotifyanalytics.application.service.RedisServiceImpl;
import br.com.spotifyanalytics.application.service.SpotifyServiceImpl;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.PlayHistory;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.data.personalization.simplified.GetUsersTopArtistsRequest;
import se.michaelthelin.spotify.requests.data.personalization.simplified.GetUsersTopTracksRequest;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SpotifyService implements SpotifyServiceImpl
{
    private final SpotifyApi spotifyApi;
    private final RedisServiceImpl redisService;

    public SpotifyService(SpotifyApi spotifyApi, RedisServiceImpl redisService) {
        this.spotifyApi = spotifyApi;
        this.redisService = redisService;
    }

    @Override
    public EstatisticasFreeDTO calculaEstatisticasFree(String username) {
        String accessToken = redisService.getTokenRedis(username,"accessToken");
        spotifyApi.setAccessToken(accessToken);
        Track[] topTracks = obterTopTracks("short_term", 20);

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
        String accessToken = redisService.getTokenRedis(username, "accessToken");
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


    private Track[] obterTopTracks(String timeRange, int limit) {
        try {
            GetUsersTopTracksRequest request = spotifyApi.getUsersTopTracks()
                    .limit(limit)
                    .time_range(timeRange)
                    .build();
            return request.execute().getItems();
        } catch (Exception e) {
            throw new SpotifyApiException();
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
            throw new SpotifyApiException();
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
            throw new SpotifyApiException();
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
