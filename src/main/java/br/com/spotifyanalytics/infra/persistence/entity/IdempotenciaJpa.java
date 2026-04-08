package br.com.spotifyanalytics.infra.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

//@Getter
//@Setter
//@Entity
//@Table(name = "chaves_idempotencia")
public class IdempotenciaJpa {

//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chave_idempotencia_seq")
//    @SequenceGenerator(name = "chave_idempotencia_seq", sequenceName = "chaves_idempotencia_id_seq", allocationSize = 1)
//    private Long id;
//
//    @Column(name = "chave_idempotencia", unique = true, nullable = false, length = 255)
//    private String chaveIdempotencia;
//
//    @Column(name = "criado_em", updatable = false)
//    private LocalDateTime criadoEm;
//
//    @PrePersist
//    protected void onCreate() {
//        criadoEm = LocalDateTime.now();
//    }

}
