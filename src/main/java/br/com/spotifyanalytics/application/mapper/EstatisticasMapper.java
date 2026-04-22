package br.com.spotifyanalytics.application.mapper;

import br.com.spotifyanalytics.application.dto.EstatisticasFreeDTO;
import br.com.spotifyanalytics.application.dto.EstatisticasPremiumDTO;
import br.com.spotifyanalytics.infra.persistence.entity.EstatisticasFreeJpa;
import br.com.spotifyanalytics.infra.persistence.entity.EstatisticasPremiumJPA;
import br.com.spotifyanalytics.infra.persistence.entity.UsuariosJpa;
import org.springframework.stereotype.Component;

@Component
public class EstatisticasMapper
{
    public EstatisticasFreeJpa freeDtoToFreeJpa(UsuariosJpa usuariosJpa, EstatisticasFreeDTO estatisticas)
    {
        EstatisticasFreeJpa estatisticasFreeJpa = new EstatisticasFreeJpa();
        estatisticasFreeJpa.setUsuario(usuariosJpa); // seta o usuário aqui
        estatisticasFreeJpa.setAlbumMaisOuvido(estatisticas.getAlbumMaisOuvido());
        estatisticasFreeJpa.setFaixaMaisOuvida(estatisticas.getMusicaMaisOuvida());
        estatisticasFreeJpa.setArtistaMaisOuvido(estatisticas.getArtistaMaisOuvido());
        return estatisticasFreeJpa;
    }

    public EstatisticasPremiumJPA premiumDtoToPremiumJpa(UsuariosJpa usuariosJpa, EstatisticasPremiumDTO estatisticasPremium)
    {
        EstatisticasPremiumJPA estatisticasPremiumJPA = new EstatisticasPremiumJPA();
        estatisticasPremiumJPA.setUsuario(usuariosJpa);
        estatisticasPremiumJPA.setFaixaMaisPopular(estatisticasPremium.getFaixaMaisPopular());
        estatisticasPremiumJPA.setPeriodoDiaMaisAtivo(estatisticasPremium.getPeriodoDiaMaisAtivo());
        estatisticasPremiumJPA.setTop5Artistas(String.join(",", estatisticasPremium.getTop5Artistas()));
        return estatisticasPremiumJPA;
    }


}
