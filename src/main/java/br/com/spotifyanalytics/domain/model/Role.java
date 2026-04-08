package br.com.spotifyanalytics.domain.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter

public enum Role {
    ADMIN,
    FREE,
    PREMIUM;

    private String tipo;
}