package br.com.spotifyanalytics.infra.web.controller;

import br.com.spotifyanalytics.application.dto.JwtResponseDTO;
import br.com.spotifyanalytics.application.service.AuthServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return new ResponseEntity<>(authService.getLoginUrl(),HttpStatus.PERMANENT_REDIRECT);
    }

    @GetMapping("/callback")
    public ResponseEntity<String> handleCallback( @RequestParam(required = false) String code,
                                @RequestParam(required = false) String error){

        if (error != null || code == null) {
            return new ResponseEntity<>(
                    "Autorização negada pelo usuário. Por favor, autorize o acesso ao Spotify para continuar.",
                    HttpStatus.UNAUTHORIZED
            );
        }
        authService.spotifyAuth(code);
        return new ResponseEntity<>("Login realizado com sucesso!", HttpStatus.OK);
    }

    @GetMapping("/token")
    public ResponseEntity<JwtResponseDTO> callbackSpotify(@RequestParam String username) {
        JwtResponseDTO jwtResponse = authService.captureToken(username);
        return new ResponseEntity<>(jwtResponse,HttpStatus.OK);
    }
}
