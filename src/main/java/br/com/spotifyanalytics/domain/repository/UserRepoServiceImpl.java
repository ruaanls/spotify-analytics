package br.com.spotifyanalytics.domain.repository;

import br.com.spotifyanalytics.application.dto.SpotifyUser;
import br.com.spotifyanalytics.infra.persistence.entity.EstatisticasFreeJpa;
import br.com.spotifyanalytics.infra.persistence.entity.EstatisticasPremiumJPA;
import br.com.spotifyanalytics.infra.persistence.entity.UsuariosJpa;
import org.springframework.stereotype.Repository;

import java.util.Objects;

@Repository
public interface UserRepoServiceImpl
{

    UsuariosJpa findOrCreate(SpotifyUser spotifyUser);
    void deleteUser(String username);

    UsuariosJpa findBySpotifyId(String username);
    void saveEstatisticasFree(EstatisticasFreeJpa estatisticasFreeJpa);

    void saveEstatisticasPremium(EstatisticasPremiumJPA estatisticasPremiumJPA);
}
