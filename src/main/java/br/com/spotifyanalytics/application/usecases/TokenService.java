package br.com.spotifyanalytics.application.usecases;

import br.com.spotifyanalytics.application.dto.SpotifyUser;
import br.com.spotifyanalytics.application.service.TokenServiceImpl;
import br.com.spotifyanalytics.domain.model.Usuarios;
import br.com.spotifyanalytics.infra.persistence.entity.UsuariosJpa;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenService implements TokenServiceImpl
{

    private final String SECRET = "my-secret-key";
    private final long Expiration = 35000;

    @Override
    public String generateToken(UsuariosJpa user) {
        return JWT.create()
                .withSubject(user.getId().toString())
                .withClaim("role", user.getTipo().name())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis()+Expiration))
                .sign(Algorithm.HMAC256(SECRET));
    }

    @Override
    public String validateToken(String token) {
        return JWT.require(Algorithm.HMAC256(SECRET))
                .build()
                .verify(token)
                .getSubject();
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
