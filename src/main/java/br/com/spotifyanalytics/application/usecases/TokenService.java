package br.com.spotifyanalytics.application.usecases;

import br.com.spotifyanalytics.application.dto.SpotifyUser;
import br.com.spotifyanalytics.application.service.TokenServiceImpl;
import br.com.spotifyanalytics.domain.model.Usuarios;
import br.com.spotifyanalytics.infra.persistence.entity.UsuariosJpa;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenService implements TokenServiceImpl
{

    private final String SECRET = "my-secret-key";
    private final long Expiration = 3500000;

    @Override
    public String generateToken(UsuariosJpa user) {
        try
        {
            return JWT.create()
                    .withSubject(user.getSpotifyId())
                    .withClaim("role", user.getTipo().name())
                    .withIssuedAt(new Date())
                    .withExpiresAt(new Date(System.currentTimeMillis()+Expiration))
                    .sign(Algorithm.HMAC256(SECRET));
        }
        catch (JWTCreationException e)
        {
            throw new RuntimeException("Erro ao gerar token: ", e.getCause());
        }

    }

    @Override
    public String validateToken(String token) {
        try
        {
            return JWT.require(Algorithm.HMAC256(SECRET))
                    .build()
                    .verify(token)
                    .getSubject();
        }
        catch (TokenExpiredException e)
        {
            throw new TokenExpiredException("Token JWT Expirado por favor, realize um login novamente", e.getExpiredOn());
        }
        catch (JWTVerificationException e) {
            throw new JWTVerificationException("Token JWT inválido por favor, realize um login novamente ", e.getCause());
        }

    }

    @Override
    public String getRole(String token) {
        return JWT.require(Algorithm.HMAC256(SECRET))
                .build()
                .verify(token)
                .getClaim("role")
                .asString();
    }
}
