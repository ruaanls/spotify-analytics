package br.com.spotifyanalytics.domain.model;

import br.com.spotifyanalytics.infra.persistence.entity.UsuariosJpa;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EstatisticasPremium
{

    private Long id;

    private UsuariosJpa usuario;

    private String faixaMaisPopular;

    private String periodoDiaMaisAtivo;

    private String top5Artistas;

    private LocalDateTime registradoEm;
}
