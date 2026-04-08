package br.com.spotifyanalytics.application.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
@Setter
public class AuthInvalid
{
    private HttpStatus status;
    private String message;
}
