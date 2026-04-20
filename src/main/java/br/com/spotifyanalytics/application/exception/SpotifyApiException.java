package br.com.spotifyanalytics.application.exception;

public class SpotifyApiException extends RuntimeException {
  public SpotifyApiException(String message) {
    super(message);
  }
  public SpotifyApiException()
  {
    super("Houve um erro na captura dos seus dados do spotify, tente novamente mais tarde");
  }
}
