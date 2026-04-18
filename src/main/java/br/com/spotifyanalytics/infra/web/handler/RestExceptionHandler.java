package br.com.spotifyanalytics.infra.web.handler;

import br.com.spotifyanalytics.application.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

public class RestExceptionHandler extends ResponseEntityExceptionHandler
{
    @ExceptionHandler(DatabaseException.class)
    private ResponseEntity<ErrorException> registerFailedHandler(DatabaseException exception)
    {
        ErrorException Exception = new ErrorException(HttpStatus.SERVICE_UNAVAILABLE,exception.getMessage());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(Exception);
    }

    @ExceptionHandler(TokenRedisNotFound.class)
    private ResponseEntity<ErrorException> tokenRedisNotFoundHandler(TokenRedisNotFound exception)
    {
        ErrorException Exception = new ErrorException(HttpStatus.SERVICE_UNAVAILABLE,exception.getMessage());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(Exception);
    }

    @ExceptionHandler(SpotifyAuthException.class)
    private ResponseEntity<ErrorException> spotifyAuthHandler(SpotifyAuthException exception)
    {
        ErrorException Exception = new ErrorException(HttpStatus.SERVICE_UNAVAILABLE,exception.getMessage());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(Exception);
    }

    @ExceptionHandler(SpotifyApiException.class)
    private ResponseEntity<ErrorException> spotifyApiHandler(SpotifyApiException exception)
    {
        ErrorException Exception = new ErrorException(HttpStatus.SERVICE_UNAVAILABLE,exception.getMessage());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(Exception);
    }

    @ExceptionHandler(Exception.class)
    private ResponseEntity<ErrorException> genericExceptionHandler(Exception exception)
    {
        ErrorException error = new ErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro inesperado. Tente novamente mais tarde.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }


}
