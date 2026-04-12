package br.com.spotifyanalytics.infra.web.controller;


import br.com.spotifyanalytics.application.dto.JwtResponseDTO;
import br.com.spotifyanalytics.application.service.AuthServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController
{
    private final AuthServiceImpl authService;

    @GetMapping("/redirect")
    public ResponseEntity<String> loginSpotifyRedirect ()
    {
        return new ResponseEntity<>(authService.spotifyAuthRedirect(),HttpStatus.PERMANENT_REDIRECT);
    }

    @GetMapping("/callback")
    public void handleCallback(@RequestParam String code,
                               HttpSession session,
                               HttpServletResponse response) throws IOException {

        boolean authProperties = false;

        if (authProperties) {
            JwtResponseDTO jwt = authService.spotifyAuth(code);

            ResponseCookie cookie = ResponseCookie.from("app_jwt", jwt.getToken())
                    .httpOnly(true)
                    .secure(false)
                    .sameSite("Lax")
                    .path("/")
                    .maxAge(24 * 60 * 60)
                    .build();
            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

            response.sendRedirect("http://127.0.0.1:8080");

        } else {
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.println("""
            <!DOCTYPE html>
            <html>
            <head><title>Autorização Concluída</title></head>
            <body>
                <h2>Login via Spotify realizado com sucesso!</h2>
                <p>Use esse code para se conectar: %s</p>
                <p><small>Para testes, utilize o endpoint <code>POST /api/auth/spotify/token</code> com o código obtido.</small></p>
            </body>
            </html>
            """.formatted(code));
        }
    }

    @GetMapping("/token")
    public ResponseEntity<JwtResponseDTO> callbackSpotify(@RequestParam("code") String code) {
        JwtResponseDTO jwtResponse = authService.spotifyAuth(code);
        return ResponseEntity.ok(jwtResponse);
    }
}
