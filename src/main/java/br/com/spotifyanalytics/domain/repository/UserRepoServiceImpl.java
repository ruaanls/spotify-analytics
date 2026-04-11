package br.com.spotifyanalytics.domain.repository;

import br.com.spotifyanalytics.application.dto.SpotifyUser;
import br.com.spotifyanalytics.infra.persistence.entity.UsuariosJpa;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepoServiceImpl
{

    UsuariosJpa findOrCreate(SpotifyUser spotifyUser);
}
