package br.com.spotifyanalytics.infra.web.controller;


import br.com.spotifyanalytics.application.dto.JwtResponseDTO;
import br.com.spotifyanalytics.application.dto.UsernameAuthDTO;
import br.com.spotifyanalytics.application.service.AuthServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;

@RestController
@RequestMapping("/auth")
@Setter
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

        boolean authProperties = true;
        authService.spotifyAuth(code);
    }

    @GetMapping("/token")
    public ResponseEntity<JwtResponseDTO> callbackSpotify(@RequestParam String username) {
        JwtResponseDTO jwtResponse = authService.captureToken(username);
        return ResponseEntity.ok(jwtResponse);
    }
}
