package br.com.spotifyanalytics.infra.config.security;

import br.com.spotifyanalytics.application.exception.AuthInvalid;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAutheEntryPoint implements AuthenticationEntryPoint
{

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        AuthInvalid error;

        Exception storedJwtException = (Exception) request.getAttribute("exception");

        if (storedJwtException instanceof TokenExpiredException) {
            // Token JWT expirado
            error = new AuthInvalid(HttpStatus.UNAUTHORIZED, "Token JWT expirado, por favor realize o login novamente!");
            writeErrorResponse(response, error);

        } else if (storedJwtException instanceof JWTVerificationException) {
            // Token JWT inválido
            error = new AuthInvalid(HttpStatus.UNAUTHORIZED, "Token JWT inválido, por favor realize o login novamente!");
            writeErrorResponse(response, error);

        } else if (authException instanceof BadCredentialsException) {
            // Login e senha incorretos
            error = new AuthInvalid(HttpStatus.UNAUTHORIZED, "Login ou senha Incorretos, por favor tente novamente");
            writeErrorResponse(response, error);

        } else {
            // Verifica se é um caso de token JWT ausente
            String authHeader = request.getHeader("Authorization");
            String requestURI = request.getRequestURI();

            // Só trata como token ausente se for uma requisição para endpoint protegido
            // e não tiver o header Authorization
            if (isProtectedEndpoint(requestURI) && (authHeader == null || !authHeader.startsWith("Bearer "))) {

                error = new AuthInvalid(HttpStatus.SERVICE_UNAVAILABLE, storedJwtException.getCause().toString());
                writeErrorResponse(response, error);
            }
        }
    }

    private void writeErrorResponse(HttpServletResponse response, AuthInvalid error) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(error);
        response.getWriter().write(jsonResponse);
    }

    private boolean isProtectedEndpoint(String requestURI) {
        // Define quais endpoints são protegidos e precisam de JWT
        return !requestURI.startsWith("/product/create") &&
                !requestURI.startsWith("/product/*");

    }
}
