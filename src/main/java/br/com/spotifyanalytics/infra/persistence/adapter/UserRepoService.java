package br.com.spotifyanalytics.infra.persistence.adapter;

import br.com.spotifyanalytics.application.dto.SpotifyUser;
import br.com.spotifyanalytics.domain.model.Role;
import br.com.spotifyanalytics.domain.repository.UserRepoServiceImpl;
import br.com.spotifyanalytics.infra.persistence.entity.EstatisticasFreeJpa;
import br.com.spotifyanalytics.infra.persistence.entity.EstatisticasPremiumJPA;
import br.com.spotifyanalytics.infra.persistence.entity.UsuariosJpa;
import br.com.spotifyanalytics.infra.persistence.repository.EstatisticasFreeRepository;
import br.com.spotifyanalytics.infra.persistence.repository.EstatisticasPremiumRepository;
import br.com.spotifyanalytics.infra.persistence.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepoService implements UserRepoServiceImpl {

    private final UserRepository userRepository;
    private final EstatisticasFreeRepository estatisticasFreeRepository;
    private final EstatisticasPremiumRepository estatisticasPremiumRepository;

    public UserRepoService(UserRepository userRepository, EstatisticasFreeRepository estatisticasFreeRepository, EstatisticasPremiumRepository estatisticasPremiumRepository) {
        this.userRepository = userRepository;
        this.estatisticasFreeRepository = estatisticasFreeRepository;
        this.estatisticasPremiumRepository = estatisticasPremiumRepository;
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

    @Override
    @Transactional
    public void deleteUser(String username) {
        userRepository.deleteBySpotifyId(username);
    }

    @Override
    public UsuariosJpa findBySpotifyId(String username) {
        if(userRepository.findBySpotifyId(username).isPresent())
        {
            return userRepository.findBySpotifyId(username).get();
        }
        else
        {
            throw new RuntimeException();
        }

    }


    @Override
    public void saveEstatisticasFree(EstatisticasFreeJpa estatisticasFreeJpa) {
        estatisticasFreeRepository.save(estatisticasFreeJpa);
    }



    @Override
    public void saveEstatisticasPremium(EstatisticasPremiumJPA estatisticasPremiumJPA)
    {
        estatisticasPremiumRepository.save(estatisticasPremiumJPA);

    }


}
