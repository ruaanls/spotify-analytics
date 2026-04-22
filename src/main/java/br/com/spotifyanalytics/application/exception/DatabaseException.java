package br.com.spotifyanalytics.application.exception;

public class DatabaseException extends RuntimeException {
    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException() {
        super("Erro de inconsistência de dados, tente novamente em alguns minutos");
    }
}
