package br.com.spotifyanalytics.application.service;

import br.com.spotifyanalytics.domain.model.Usuarios;
import br.com.spotifyanalytics.infra.persistence.entity.UsuariosJpa;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Service;

@Service
public interface TokenServiceImpl
{
    public String generateToken(UsuariosJpa user);
    public String validateToken(String token);
    public String getRole(String token);
}
