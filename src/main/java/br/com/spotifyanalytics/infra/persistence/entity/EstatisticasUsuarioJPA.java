package br.com.spotifyanalytics.infra.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.catalina.User;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "estatisticas_usuario")
public class EstatisticasUsuarioJPA {

    @Id
    @OneToOne
    @JoinColumn(name = "usuario_id")
    private UsuariosJpa usuario;

    @Column(name = "genero_top", length = 100)
    private String generoTop;

    @Column(length = 50)
    private String humor;

    @Column(name = "energia_media")
    private Double energiaMedia;

    @Column(name = "valencia_media")
    private Double valenciaMedia;

    @Column(name = "pontuacao_diversidade")
    private Double pontuacaoDiversidade;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    @PreUpdate
    @PrePersist
    protected void onUpdate() {
        atualizadoEm = LocalDateTime.now();
    }

}
