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
import java.util.stream.Stream;

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
        spotifyApi.setAccessToken(redisService.getTokenRedis(username, "accessToken"));
        Track[] topTracks = obterTopTracks("short_term", 20);

        return new EstatisticasFreeDTO(
                calcularMaisFrequente(Arrays.stream(topTracks).map(t -> t.getArtists()[0].getName())),
                calcularMaisFrequente(Arrays.stream(topTracks).map(t -> t.getAlbum().getName() + " - " + t.getArtists()[0].getName())),
                calcularMaisFrequente(Arrays.stream(topTracks).map(Track::getName))
        );
    }

    @Override
    public EstatisticasPremiumDTO calculaEstatisticasPagas(String username) {
        spotifyApi.setAccessToken(redisService.getTokenRedis(username, "accessToken"));

        Track[] topTracks = obterTopTracks("medium_term", 15);
        Artist[] topArtists = obterTopArtists("medium_term", 15);

        return new EstatisticasPremiumDTO(
                calcularPeriodoDiaMaisAtivo(obterHistoricoRecente(15)),
                extrairTop5Artistas(topArtists),
                extrairFaixaMaisPopular(topTracks)
        );
    }

    private String calcularMaisFrequente(Stream<String> stream) {
        return stream
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Desconhecido");
    }

    private List<String> extrairTop5Artistas(Artist[] topArtists) {
        return Arrays.stream(topArtists)
                .limit(5)
                .map(Artist::getName)
                .collect(Collectors.toList());
    }

    private String extrairFaixaMaisPopular(Track[] topTracks) {
        return Arrays.stream(topTracks)
                .findFirst()
                .map(t -> t.getName() + " - " + t.getArtists()[0].getName())
                .orElse("Desconhecida");
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
