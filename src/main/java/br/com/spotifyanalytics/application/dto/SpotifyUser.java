package br.com.spotifyanalytics.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SpotifyUser
{
    private String id;
    private String display_name;
    private String email;
}
