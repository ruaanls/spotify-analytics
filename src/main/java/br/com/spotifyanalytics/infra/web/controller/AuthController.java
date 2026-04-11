package br.com.spotifyanalytics.infra.web.controller;


import br.com.spotifyanalytics.application.dto.JwtResponseDTO;
import br.com.spotifyanalytics.application.service.AuthServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

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
    public ResponseEntity<JwtResponseDTO> callbackSpotify(@RequestParam("code") String code) {
        JwtResponseDTO jwtResponse = authService.spotifyAuth(code);
        return ResponseEntity.ok(jwtResponse);
    }
}
