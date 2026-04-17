package br.com.spotifyanalytics.domain.model;

import br.com.spotifyanalytics.infra.persistence.entity.UsuariosJpa;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EstatisticasUsuarios
{
    private UsuariosJpa usuario;

    private String generoTop;

    private String humor;

    private Double energiaMedia;

    private Double valenciaMedia;

    private Double pontuacaoDiversidade;

    private LocalDateTime atualizadoEm;

}
