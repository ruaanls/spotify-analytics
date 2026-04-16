package br.com.spotifyanalytics.application.usecases;

import br.com.spotifyanalytics.application.dto.JwtResponseDTO;
import br.com.spotifyanalytics.application.dto.SpotifyUser;
import br.com.spotifyanalytics.application.dto.TokenResponse;
import br.com.spotifyanalytics.application.service.AuthServiceImpl;
import br.com.spotifyanalytics.application.service.SpotifyServiceImpl;
import br.com.spotifyanalytics.application.service.TokenServiceImpl;
import br.com.spotifyanalytics.domain.repository.UserRepoServiceImpl;
import br.com.spotifyanalytics.infra.persistence.entity.UsuariosJpa;
import br.com.spotifyanalytics.infra.web.controller.AuthController;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements AuthServiceImpl
{
    private final SpotifyServiceImpl spotifyService;
    private final TokenServiceImpl tokenService;
    private final UserRepoServiceImpl userRepoService;
    private AuthController authController;

    public AuthService(SpotifyServiceImpl spotifyService, TokenServiceImpl tokenService, UserRepoServiceImpl userRepoService) {
        this.spotifyService = spotifyService;
        this.tokenService = tokenService;
        this.userRepoService = userRepoService;
    }


    @Override
    public String spotifyAuthRedirect() {
        return spotifyService.getLoginUrl();
    }

    @Override
    public void spotifyAuth(String code) {
        TokenResponse tokenResponse =  spotifyService.getToken(code);
        SpotifyUser spotifyUser =  spotifyService.getUser(tokenResponse.getAccess_token());
        UsuariosJpa usuariosJpa = userRepoService.findOrCreate(spotifyUser);
        spotifyService.saveTokenRedis(usuariosJpa.getSpotifyId(),tokenResponse.getAccess_token(),"accessToken");
    }

    @Override
    public JwtResponseDTO captureToken(String username) {
        String accessToken = spotifyService.getTokenRedis(username, "accessToken");
        SpotifyUser spotifyUser =  spotifyService.getUser(accessToken);
        UsuariosJpa usuariosJpa = userRepoService.findOrCreate(spotifyUser);
        JwtResponseDTO jwtResponseDTO = new JwtResponseDTO();
        jwtResponseDTO.setToken(tokenService.generateToken(usuariosJpa));
        return jwtResponseDTO;
    }
}
