package br.com.spotifyanalytics.infra.config.security;

import br.com.spotifyanalytics.application.exception.ErrorException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAutheEntryPoint implements AuthenticationEntryPoint
{

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        ErrorException error;

        Exception storedJwtException = (Exception) request.getAttribute("exception");

        if (storedJwtException instanceof TokenExpiredException) {
            error = new ErrorException(HttpStatus.UNAUTHORIZED, "Token JWT expirado, por favor realize o login novamente!");
            writeErrorResponse(response, error);

        } else if (storedJwtException instanceof JWTVerificationException) {
            error = new ErrorException(HttpStatus.UNAUTHORIZED, "Token JWT inválido, por favor realize o login novamente!");
            writeErrorResponse(response, error);

        } else {
            String authHeader = request.getHeader("Authorization");
            String requestURI = request.getRequestURI();

            if (isProtectedEndpoint(requestURI) && (authHeader == null || !authHeader.startsWith("Bearer "))) {
                error = new ErrorException(HttpStatus.UNAUTHORIZED, "Token JWT ausente, por favor realize o login!");
                writeErrorResponse(response, error);
            } else {
                error = new ErrorException(HttpStatus.UNAUTHORIZED, "Não autorizado.");
                writeErrorResponse(response, error);
            }
        }
    }

    private void writeErrorResponse(HttpServletResponse response, ErrorException error) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(error);
        response.getWriter().write(jsonResponse);
    }

    private boolean isProtectedEndpoint(String requestURI) {
        return !requestURI.startsWith("/auth/");

    }
}
