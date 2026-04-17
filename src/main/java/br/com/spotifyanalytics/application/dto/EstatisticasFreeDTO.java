package br.com.spotifyanalytics.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class EstatisticasFreeDTO
{
    private String artistaMaisOuvido;
    private String albumMaisOuvido;
    private String musicaMaisOuvida;

}
