package br.com.spotifyanalytics.domain.model;

import br.com.spotifyanalytics.infra.persistence.entity.PlanosJPA;
import br.com.spotifyanalytics.infra.persistence.entity.UsuariosJpa;
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
public class Assinaturas
{
    private Long id;

    private UsuariosJpa usuario;

    private PlanosJPA plano;

    private LocalDateTime dataInicio;

}
