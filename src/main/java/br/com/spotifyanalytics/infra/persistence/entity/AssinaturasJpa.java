package br.com.spotifyanalytics.infra.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

//@Getter
//@Setter
//@Entity
//@Table(name = "assinaturas")
//@NoArgsConstructor
//@AllArgsConstructor
public class AssinaturasJpa {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "assinatura_seq")
//    @SequenceGenerator(name = "assinatura_seq", sequenceName = "assinaturas_id_seq", allocationSize = 1)
//    private Long id;
//
//    @Column(name = "usuario_id", nullable = false)
//    private Long usuarioId;  // referência lógica ao usuário (sem FK JPA)
//
//    @ManyToOne
//    @JoinColumn(name = "plano_id", nullable = false)
//    private PlanosJPA plano;
//
//
//    @Column(name = "data_inicio")
//    private LocalDateTime dataInicio;
//
//    @PrePersist
//    protected void onCreate() {
//        if (dataInicio == null) dataInicio = LocalDateTime.now();
//    }
//
//
}
