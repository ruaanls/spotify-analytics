package br.com.spotifyanalytics.application.service;

import br.com.spotifyanalytics.application.dto.SpotifyUser;
import br.com.spotifyanalytics.application.dto.TokenResponse;

public interface SpotifyServiceImpl
{
    String getLoginUrl();
    TokenResponse getToken(String code);
    SpotifyUser getUser(String accessToken);
}
