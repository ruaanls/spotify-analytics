package br.com.spotifyanalytics.application.service;

import br.com.spotifyanalytics.application.dto.JwtResponseDTO;
import br.com.spotifyanalytics.application.dto.SpotifyUser;
import br.com.spotifyanalytics.application.dto.TokenResponse;
import org.springframework.stereotype.Service;

@Service
public interface AuthServiceImpl
{

    void spotifyAuth(String code);
    JwtResponseDTO captureToken(String username);
    String getLoginUrl();
    TokenResponse getToken(String code);
    SpotifyUser getUser(String accessToken);

}
