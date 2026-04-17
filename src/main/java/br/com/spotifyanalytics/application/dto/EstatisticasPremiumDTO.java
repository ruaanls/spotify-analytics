package br.com.spotifyanalytics.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class EstatisticasPremiumDTO
{
    private String periodoDiaMaisAtivo;
    private List<String> top5Artistas;
    private String faixaMaisPopular;
}
