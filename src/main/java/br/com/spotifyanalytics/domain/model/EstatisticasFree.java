package br.com.spotifyanalytics.domain.model;

import br.com.spotifyanalytics.infra.persistence.entity.UsuariosJpa;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EstatisticasFree
{
    private Long id;

    private UsuariosJpa usuario;

    private String artistaMaisOuvido;

    private String albumMaisOuvido;

    private String faixaMaisOuvida;

    private LocalDateTime registradoEm;
}
