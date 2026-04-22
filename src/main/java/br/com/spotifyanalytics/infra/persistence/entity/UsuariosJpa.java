package br.com.spotifyanalytics.infra.persistence.entity;

import br.com.spotifyanalytics.domain.model.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "usuarios")
public class UsuariosJpa implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "spotify_id", unique = true, length = 100)
    private String spotifyId;

    @Column(name = "nome")
    private String nome;

    @Column(unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private Role tipo;

    @Column(name = "criado_em", updatable = false)
    private LocalDateTime criadoEm;

    // substituiu @OneToOne por @OneToMany para suportar histórico

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<EstatisticasFreeJpa> estatisticasFree;


    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<EstatisticasPremiumJPA> estatisticasPremium;

    @PrePersist
    protected void onCreate() {
        criadoEm = LocalDateTime.now();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.tipo == Role.ADMIN) {
            return List.of(
                    new SimpleGrantedAuthority("ROLE_ADMIN"),
                    new SimpleGrantedAuthority("ROLE_PREMIUM"),
                    new SimpleGrantedAuthority("ROLE_FREE")
            );
        }
        if (this.tipo == Role.PREMIUM) {
            return List.of(
                    new SimpleGrantedAuthority("ROLE_PREMIUM"),
                    new SimpleGrantedAuthority("ROLE_FREE")
            );
        }

        return List.of(new SimpleGrantedAuthority("ROLE_FREE"));
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return spotifyId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setEstatisticasFree(EstatisticasFreeJpa estatisticasFree) {
        this.estatisticasFree.add(estatisticasFree);
    }

    public void setEstatisticasPremium(EstatisticasPremiumJPA estatisticasPremium) {
        this.estatisticasPremium.add(estatisticasPremium);
    }
}
