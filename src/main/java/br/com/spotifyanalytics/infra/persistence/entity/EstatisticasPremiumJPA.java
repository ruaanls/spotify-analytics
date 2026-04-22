package br.com.spotifyanalytics.infra.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "estatisticas_premium")
public class EstatisticasPremiumJPA
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private UsuariosJpa usuario;

    @Column(name = "faixa_mais_popular", length = 150)
    private String faixaMaisPopular;

    @Column(name = "periodo_dia_mais_ativo", length = 50)
    private String periodoDiaMaisAtivo;

    @Column(name = "top5_artistas", length = 500)
    private String top5Artistas; // salva como "Artista1,Artista2,Artista3..."

    @Column(name = "registrado_em")
    private LocalDateTime registradoEm;

    @PrePersist
    protected void onCreate() {
        registradoEm = LocalDateTime.now();
    }
}
