package br.com.spotifyanalytics.infra.web.controller;


import br.com.spotifyanalytics.application.dto.EstatisticasFreeDTO;
import br.com.spotifyanalytics.application.dto.EstatisticasPremiumDTO;
import br.com.spotifyanalytics.application.service.SpotifyServiceImpl;
import com.auth0.jwt.JWT;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.context.SecurityContextHolder;



@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
public class AnalyticsController
{
    private final SpotifyServiceImpl spotifyService;

    @GetMapping("/free")
    public ResponseEntity<EstatisticasFreeDTO> getEstatisticasFree(@AuthenticationPrincipal String username)
    {

        EstatisticasFreeDTO estatisticas = spotifyService.calculaEstatisticasFree(username);
        return new ResponseEntity<>(estatisticas, HttpStatus.OK);
    }

    @GetMapping("/premium")
    @PreAuthorize("hasAnyRole('PREMIUM', 'ADMIN')")
    public ResponseEntity<EstatisticasPremiumDTO> getEstatisticasPremium(@AuthenticationPrincipal String username)
    {
        EstatisticasPremiumDTO estatisticasPremium = spotifyService.calculaEstatisticasPagas(username);
        return new ResponseEntity<>(estatisticasPremium,HttpStatus.OK);
    }

}
