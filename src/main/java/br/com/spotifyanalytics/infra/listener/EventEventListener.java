package br.com.spotifyanalytics.infra.listener;

import br.com.spotifyanalytics.application.dto.EventResponseDTO;
import br.com.spotifyanalytics.application.dto.SnsEnvelopeDTO;
import br.com.spotifyanalytics.domain.model.Role;
import br.com.spotifyanalytics.domain.repository.UserRepoServiceImpl;
import br.com.spotifyanalytics.infra.persistence.entity.UsuariosJpa;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
@Slf4j
@Component
public class EventEventListener
{

    private final ObjectMapper objectMapper;
    private final UserRepoServiceImpl userRepoService;

    public EventEventListener(ObjectMapper objectMapper, UserRepoServiceImpl userRepoService) {
        this.objectMapper = objectMapper;
        this.userRepoService = userRepoService;
    }

    @SqsListener("${app.aws.sqs.queue-name}")
    public void receivePaymentNotification(String rawMessage) throws JsonProcessingException {
        SnsEnvelopeDTO envelope = objectMapper.readValue(rawMessage, SnsEnvelopeDTO.class);
        log.info("Message extraído: {}", envelope.getMessage());
        EventResponseDTO payment = objectMapper.readValue(envelope.getMessage(), EventResponseDTO.class);
        UsuariosJpa usuariosJpa = this.userRepoService.findBySpotifyId(payment.getUsername());
        usuariosJpa.setTipo(Role.PREMIUM);
        this.userRepoService.updateUser(usuariosJpa);
    }
}
