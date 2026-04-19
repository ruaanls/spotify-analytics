package br.com.spotifyanalytics.infra.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "estatisticas_free")
public class EstatisticasFreeJpa
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private UsuariosJpa usuario;

    @Column(name = "artista_mais_ouvido", length = 100)
    private String artistaMaisOuvido;

    @Column(name = "album_mais_ouvido", length = 150)
    private String albumMaisOuvido;

    @Column(name = "faixa_mais_ouvida", length = 150)
    private String faixaMaisOuvida;

    @Column(name = "registrado_em")
    private LocalDateTime registradoEm;

    @PrePersist
    protected void onCreate() {
        registradoEm = LocalDateTime.now();
    }
}
