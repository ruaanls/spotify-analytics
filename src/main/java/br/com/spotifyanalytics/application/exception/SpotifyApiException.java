package br.com.spotifyanalytics.application.exception;

import org.springframework.http.HttpStatus;

import java.util.Optional;

public class SpotifyApiException extends RuntimeException {
  private final HttpStatus status;

  // Usado pelo handler — sem status específico
  public SpotifyApiException() {
    super("Houve um erro na captura dos seus dados do spotify, tente novamente mais tarde");
    this.status = null;
  }

  // Usado quando precisa de um status diferente (ex: 403)
  public SpotifyApiException(String message, HttpStatus status) {
    super(message);
    this.status = status;
  }

  public Optional<HttpStatus> getStatus() {
    return Optional.ofNullable(status);
  }

}
