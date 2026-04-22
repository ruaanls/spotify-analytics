package br.com.spotifyanalytics.application.exception;

public class SpotifyAuthException extends RuntimeException {
    public SpotifyAuthException(String message) {
        super(message);
    }
    public SpotifyAuthException()
    {
        super("Ocorreu um erro ao realizar a autenticação com a sua conta do spotify, tente novamente mais tarde");
    }
}
