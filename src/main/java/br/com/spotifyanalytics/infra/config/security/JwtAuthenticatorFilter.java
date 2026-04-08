package br.com.spotifyanalytics.infra.config.security;

import br.com.spotifyanalytics.application.service.TokenServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@NoArgsConstructor
public class JwtAuthenticatorFilter extends OncePerRequestFilter
{

    @Autowired
    private TokenServiceImpl tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
    {
        String token = recoverToken(request);

        if(token != null)
        {
            try{
                String userId = tokenService.validateToken(token);
                String role = tokenService.getRole(token);
                SimpleGrantedAuthority authority =
                        new SimpleGrantedAuthority("ROLE_" + role);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userId,null, List.of(authority));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            catch (Exception e)
            {
                throw new ServletException("Token Inválido");
            }
        }
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null) {
            return null;
        }

        // formato esperado: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6...
        return authHeader.replace("Bearer ", "");
    }
}
