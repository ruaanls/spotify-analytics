package br.com.spotifyanalytics.infra.persistence.repository;

import br.com.spotifyanalytics.infra.persistence.entity.EstatisticasFreeJpa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstatisticasFreeRepository extends JpaRepository<EstatisticasFreeJpa, Long> {
}
