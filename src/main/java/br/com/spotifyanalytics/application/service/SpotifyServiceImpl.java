package br.com.spotifyanalytics.application.service;

import br.com.spotifyanalytics.application.dto.EstatisticasFreeDTO;
import br.com.spotifyanalytics.application.dto.EstatisticasPremiumDTO;
import br.com.spotifyanalytics.application.dto.SpotifyUser;
import br.com.spotifyanalytics.application.dto.TokenResponse;

public interface SpotifyServiceImpl
{
    String getLoginUrl();
    TokenResponse getToken(String code);
    SpotifyUser getUser(String accessToken);
    EstatisticasFreeDTO calculaEstatisticasFree(String username);
    void saveTokenRedis(String id, String value, String type);
    String getTokenRedis(String id, String type);
    EstatisticasPremiumDTO calculaEstatisticasPagas(String username);
}
