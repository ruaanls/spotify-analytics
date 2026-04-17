package br.com.spotifyanalytics.application.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TokenResponse
{
    private String access_token;
    private String refresh_token;
    private int expires_in;
}
