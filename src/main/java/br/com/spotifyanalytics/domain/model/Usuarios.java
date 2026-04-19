package br.com.spotifyanalytics.domain.model;

import br.com.spotifyanalytics.infra.persistence.entity.AssinaturasJpa;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Usuarios
{
    private Long id;

    private String spotifyId;

    private String nome;  // mapeado para coluna "name"

    private String email;

    private Role tipo;

    private LocalDateTime criadoEm;

    //private EstatisticasUsuarioJPA estatisticas;

    private List<AssinaturasJpa> assinaturas = new ArrayList<>();

}
