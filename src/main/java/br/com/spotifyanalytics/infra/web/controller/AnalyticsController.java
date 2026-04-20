package br.com.spotifyanalytics.infra.web.controller;

import br.com.spotifyanalytics.application.dto.EstatisticasFreeDTO;
import br.com.spotifyanalytics.application.dto.EstatisticasPremiumDTO;
import br.com.spotifyanalytics.application.mapper.EstatisticasMapper;
import br.com.spotifyanalytics.application.service.SpotifyServiceImpl;
import br.com.spotifyanalytics.domain.repository.UserRepoServiceImpl;
import br.com.spotifyanalytics.infra.persistence.entity.EstatisticasFreeJpa;
import br.com.spotifyanalytics.infra.persistence.entity.EstatisticasPremiumJPA;
import br.com.spotifyanalytics.infra.persistence.entity.UsuariosJpa;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
public class AnalyticsController
{
    private final SpotifyServiceImpl spotifyService;
    private final UserRepoServiceImpl userRepoService;
    private final EstatisticasMapper estatisticasMapper;

    @GetMapping("/free")
    public ResponseEntity<EstatisticasFreeDTO> getEstatisticasFree(@AuthenticationPrincipal String username)
    {
        EstatisticasFreeDTO estatisticas = spotifyService.calculaEstatisticasFree(username);
        UsuariosJpa usuariosJpa = userRepoService.findBySpotifyId(username);
        EstatisticasFreeJpa estatisticasFreeJpa = estatisticasMapper.freeDtoToFreeJpa(usuariosJpa,estatisticas);
        userRepoService.saveEstatisticasFree(estatisticasFreeJpa);
        return new ResponseEntity<>(estatisticas, HttpStatus.OK);
    }

    @GetMapping("/premium")
    @PreAuthorize("hasAnyRole('PREMIUM', 'ADMIN')")
    public ResponseEntity<EstatisticasPremiumDTO> getEstatisticasPremium(@AuthenticationPrincipal String username)
    {
        UsuariosJpa usuariosJpa = userRepoService.findBySpotifyId(username);
        EstatisticasPremiumDTO estatisticasPremium = spotifyService.calculaEstatisticasPagas(username);
        EstatisticasPremiumJPA estatisticasPremiumJPA = estatisticasMapper.premiumDtoToPremiumJpa(usuariosJpa,estatisticasPremium);
        userRepoService.saveEstatisticasPremium(estatisticasPremiumJPA);
        return new ResponseEntity<>(estatisticasPremium,HttpStatus.OK);
    }

}
