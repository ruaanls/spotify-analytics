package br.com.spotifyanalytics.infra.web.controller;


import br.com.spotifyanalytics.application.dto.EstatisticasFreeDTO;
import br.com.spotifyanalytics.application.service.SpotifyServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
public class AnalyticsController
{
    private final SpotifyServiceImpl spotifyService;

    @GetMapping("/free")
    public ResponseEntity<EstatisticasFreeDTO> getEstatisticasFree(@RequestHeader("Authorization") String authHeader)
    {
        String accessToken = authHeader.replace("Bearer ","");
        EstatisticasFreeDTO estatisticas = spotifyService.calculaEstatisticasFree(accessToken);
        return new ResponseEntity<>(estatisticas, HttpStatus.OK);
    }
}
