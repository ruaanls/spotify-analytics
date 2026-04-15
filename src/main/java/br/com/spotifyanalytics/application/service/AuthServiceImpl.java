package br.com.spotifyanalytics.application.service;

import br.com.spotifyanalytics.application.dto.JwtResponseDTO;

public interface AuthServiceImpl
{
    String spotifyAuthRedirect();
    void spotifyAuth(String code);
    JwtResponseDTO captureToken(String username);

}
