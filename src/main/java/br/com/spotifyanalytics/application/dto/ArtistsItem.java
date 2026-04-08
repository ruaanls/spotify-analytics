package br.com.spotifyanalytics.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ArtistsItem
{
    private String id;
    private String name;
    private Integer popularity;
}
