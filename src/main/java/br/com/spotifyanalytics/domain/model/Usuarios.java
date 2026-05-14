package br.com.spotifyanalytics.domain.model;

import br.com.spotifyanalytics.infra.persistence.entity.EstatisticasFreeJpa;
import br.com.spotifyanalytics.infra.persistence.entity.EstatisticasPremiumJPA;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Usuarios
{
    private Long id;

    private String spotifyId;

    private String nome;

    private String email;

    @Enumerated(EnumType.STRING)
    private Role tipo;

    private LocalDateTime criadoEm;

    private List<EstatisticasFreeJpa> estatisticasFree;

    private List<EstatisticasPremiumJPA> estatisticasPremium;
}
