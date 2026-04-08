package br.com.spotifyanalytics.infra.persistence.repository;

import br.com.spotifyanalytics.infra.persistence.entity.UsuariosJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<UsuariosJpa, Long>
{
    Optional<UsuariosJpa> findByEmail(String email);
    Optional<UsuariosJpa> findBySpotifyId(String id);
}
