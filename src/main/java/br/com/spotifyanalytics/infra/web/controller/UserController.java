package br.com.spotifyanalytics.infra.web.controller;

import br.com.spotifyanalytics.domain.repository.UserRepoServiceImpl;
import br.com.spotifyanalytics.infra.persistence.entity.UsuariosJpa;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController
{
    private final UserRepoServiceImpl userRepoService;

    @GetMapping()
    public ResponseEntity<UsuariosJpa> getUser(@AuthenticationPrincipal String username)
    {
        UsuariosJpa usuariosJpa = userRepoService.findBySpotifyId(username);
        return new ResponseEntity<>(usuariosJpa, HttpStatus.OK);
    }

    @DeleteMapping()
    public ResponseEntity deleteUser(@AuthenticationPrincipal String username)
    {
        userRepoService.deleteUser(username);
        return ResponseEntity.ok().build();
    }
}
