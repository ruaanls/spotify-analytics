package br.com.spotifyanalytics.infra.persistence.entity;

import br.com.spotifyanalytics.domain.model.NomePlano;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

//@Getter
//@Setter
//@Entity
//@Table(name = "planos")
public class PlanosJPA {

//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "plano_seq")
//    @SequenceGenerator(name = "plano_seq", sequenceName = "planos_id_seq", allocationSize = 1)
//    private Long id;
//
//    @Enumerated(EnumType.STRING)
//    @Column(name = "nome", length = 50)
//    private NomePlano nome;
//
//    @Column(precision = 10, scale = 2)
//    private BigDecimal preco;
//
//    @OneToMany(mappedBy = "plano")
//    private List<AssinaturasJpa> assinaturas = new ArrayList<>();
//
//    public PlanosJPA() {}
//

}
