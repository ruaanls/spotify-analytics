package br.com.spotifyanalytics.infra.web.controller;


import br.com.spotifyanalytics.application.dto.JwtResponseDTO;
import br.com.spotifyanalytics.application.service.AuthServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController
{
    private final AuthServiceImpl authService;

    @GetMapping("/auth/redirect")
    public ResponseEntity<String> loginSpotifyRedirect ()
    {
        return new ResponseEntity<>(authService.spotifyAuthRedirect(),HttpStatus.PERMANENT_REDIRECT);
    }

    @PostMapping("/auth/callback")
    public ResponseEntity<JwtResponseDTO> loginSpotify (@RequestBody String code)
    {
        return new ResponseEntity<>(authService.spotifyAuth(code), HttpStatus.OK);
    }
}
