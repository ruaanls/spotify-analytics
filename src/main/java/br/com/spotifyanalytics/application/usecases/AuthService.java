package br.com.spotifyanalytics.application.usecases;

import br.com.spotifyanalytics.application.dto.JwtResponseDTO;
import br.com.spotifyanalytics.application.dto.SpotifyUser;
import br.com.spotifyanalytics.application.dto.TokenResponse;
import br.com.spotifyanalytics.application.service.AuthServiceImpl;
import br.com.spotifyanalytics.application.service.SpotifyServiceImpl;
import br.com.spotifyanalytics.application.service.TokenServiceImpl;
import br.com.spotifyanalytics.domain.repository.UserRepoServiceImpl;
import br.com.spotifyanalytics.infra.persistence.entity.UsuariosJpa;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements AuthServiceImpl
{
    private final SpotifyServiceImpl spotifyService;
    private final TokenServiceImpl tokenService;
    private final UserRepoServiceImpl userRepoService;

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
    public JwtResponseDTO spotifyAuth(String code) {
        TokenResponse tokenResponse =  spotifyService.getToken(code);

        SpotifyUser spotifyUser =  spotifyService.getUser(tokenResponse.getAccess_token());
        UsuariosJpa usuariosJpa = userRepoService.findOrCreate(spotifyUser);
        JwtResponseDTO jwtResponseDTO = new JwtResponseDTO();
        jwtResponseDTO.setToken(tokenService.generateToken(usuariosJpa));
        return jwtResponseDTO;
    }
}
