package br.com.spotifyanalytics.infra.persistence.adapter;

import br.com.spotifyanalytics.application.dto.SpotifyUser;
import br.com.spotifyanalytics.domain.model.Role;
import br.com.spotifyanalytics.domain.repository.UserRepoServiceImpl;
import br.com.spotifyanalytics.infra.persistence.entity.UsuariosJpa;
import br.com.spotifyanalytics.infra.persistence.repository.UserRepository;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepoService implements UserRepoServiceImpl {

    private final UserRepository userRepository;

    public UserRepoService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UsuariosJpa findOrCreate(SpotifyUser spotifyUser) {
        return userRepository.findBySpotifyId(spotifyUser.getId())
                .map(usuarioExistente ->
                {
                    if(!usuarioExistente.getEmail().equals(spotifyUser.getEmail()) || !usuarioExistente.getNome().equals(spotifyUser.getDisplay_name()))
                    {
                        usuarioExistente.setEmail(spotifyUser.getEmail());
                        usuarioExistente.setNome(spotifyUser.getDisplay_name());
                        return userRepository.save(usuarioExistente);
                    }
                    else
                    {
                        return usuarioExistente;
                    }
                })
                .orElseGet(() ->
                {
                    UsuariosJpa novoUsuario = new UsuariosJpa();
                    novoUsuario.setTipo(Role.FREE);
                    novoUsuario.setSpotifyId(spotifyUser.getId());
                    novoUsuario.setNome(spotifyUser.getDisplay_name());
                    novoUsuario.setEmail(spotifyUser.getEmail());
                    return userRepository.save(novoUsuario);
                });
    }
}
