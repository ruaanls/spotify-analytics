package br.com.spotifyanalytics.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter

public class EstatisticasFreeDTO
{
    private Double energiaMedia;
    private Double valenciaMedia;
    private LocalDateTime atualizadoEm;

    public EstatisticasFreeDTO(Double energiaMedia, Double valenciaMedia) {
        this.energiaMedia = energiaMedia;
        this.valenciaMedia = valenciaMedia;
        this.atualizadoEm = LocalDateTime.now();
    }

}
