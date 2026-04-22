package br.com.spotifyanalytics.application.service;

import br.com.spotifyanalytics.application.dto.EstatisticasFreeDTO;
import br.com.spotifyanalytics.application.dto.EstatisticasPremiumDTO;

public interface SpotifyServiceImpl
{
    EstatisticasFreeDTO calculaEstatisticasFree(String username);
    EstatisticasPremiumDTO calculaEstatisticasPagas(String username);
}
