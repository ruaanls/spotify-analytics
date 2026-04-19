package br.com.spotifyanalytics.infra.persistence.repository;

import br.com.spotifyanalytics.infra.persistence.entity.EstatisticasPremiumJPA;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstatisticasPremiumRepository extends JpaRepository<EstatisticasPremiumJPA, Long> {
}
