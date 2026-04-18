package br.com.spotifyanalytics.application.exception;

public class TokenRedisNotFound extends RuntimeException {
    public TokenRedisNotFound(String message) {
        super(message);
    }
    public TokenRedisNotFound()
    {
        super("Nenhum token de acesso foi encontrado com esse username, tente novamente ou realize o login");
    }

}
