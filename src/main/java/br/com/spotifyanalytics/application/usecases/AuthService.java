package br.com.spotifyanalytics.application.usecases;

import br.com.spotifyanalytics.application.dto.JwtResponseDTO;
import br.com.spotifyanalytics.application.dto.SpotifyUser;
import br.com.spotifyanalytics.application.dto.TokenResponse;
import br.com.spotifyanalytics.application.exception.SpotifyApiException;
import br.com.spotifyanalytics.application.exception.SpotifyAuthException;
import br.com.spotifyanalytics.application.service.AuthServiceImpl;
import br.com.spotifyanalytics.application.service.RedisServiceImpl;
import br.com.spotifyanalytics.application.service.TokenServiceImpl;
import br.com.spotifyanalytics.domain.repository.UserRepoServiceImpl;
import br.com.spotifyanalytics.infra.persistence.entity.UsuariosJpa;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Base64;

@Service
public class AuthService implements AuthServiceImpl
{

    private final TokenServiceImpl tokenService;
    private final UserRepoServiceImpl userRepoService;
    private final RedisServiceImpl redisService;
    private final WebClient spotifyApiWebClient;
    private final WebClient spotifyAuthWebClient;


    @Value("${spotify.api.v1.client-id}")
    private String clientId;

    @Value("${spotify.api.v1.secret}")
    private String clientSecret;

    @Value("${spotify.api.v1.redirecturi}")
    private String redirectUri;

    public AuthService(
            @Qualifier("spotifyApiWebClient") WebClient spotifyApiWebClient,
            @Qualifier("spotifyAuthWebClient") WebClient spotifyAuthWebClient,
            TokenServiceImpl tokenService, UserRepoServiceImpl userRepoService, RedisServiceImpl redisService) {
        this.tokenService = tokenService;
        this.userRepoService = userRepoService;
        this.redisService = redisService;
        this.spotifyApiWebClient = spotifyApiWebClient;
        this.spotifyAuthWebClient = spotifyAuthWebClient;
    }

    @Override
    public void spotifyAuth(String code) {
        TokenResponse tokenResponse = getToken(code);
        SpotifyUser spotifyUser = getUser(tokenResponse.getAccess_token());
        redisService.saveTokenRedis(spotifyUser.getId(),tokenResponse.getAccess_token(),"accessToken");
    }

    @Override
    public JwtResponseDTO captureToken(String username) {
        String accessToken = redisService.getTokenRedis(username, "accessToken");
        SpotifyUser spotifyUser = getUser(accessToken);
        UsuariosJpa usuariosJpa = userRepoService.findOrCreate(spotifyUser);
        JwtResponseDTO jwtResponseDTO = new JwtResponseDTO();
        jwtResponseDTO.setToken(tokenService.generateToken(usuariosJpa));
        return jwtResponseDTO;
    }

    @Override
    public String getLoginUrl() {
        return "https://accounts.spotify.com/authorize" + "?client_id=" + clientId + "&response_type=code" +
                "&redirect_uri=" + redirectUri + "&scope=user-top-read user-read-email user-read-recently-played";
    }

    @Override
    public TokenResponse getToken(String code) {
        String basicAuth = Base64.getEncoder()
                .encodeToString((clientId + ":" + clientSecret).getBytes());
        try
        {
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
        catch (Exception e)
        {
            throw new SpotifyAuthException();
        }

    }

    @Override
    public SpotifyUser getUser(String accessToken) {
        try{
            return spotifyApiWebClient.get()
                    .uri("/v1/me")
                    .header("Authorization", "Bearer " + accessToken)
                    .retrieve()
                    .bodyToMono(SpotifyUser.class)
                    .block();
        }
        catch (Exception e)
        {
            throw new SpotifyApiException("Esse username não pertence a conta que você realizou login. Tente novamente com o username em que o login foi feito no navegador", HttpStatus.FORBIDDEN);
        }

    }
}
