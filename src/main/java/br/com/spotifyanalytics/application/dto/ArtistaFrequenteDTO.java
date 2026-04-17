package br.com.spotifyanalytics.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ArtistaFrequenteDTO
{
    private String nome;
    private int quantidadeMusicas;
}
