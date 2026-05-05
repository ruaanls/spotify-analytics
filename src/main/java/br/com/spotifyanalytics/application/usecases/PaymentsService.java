package br.com.spotifyanalytics.application.usecases;

import br.com.spotifyanalytics.application.dto.MercadoPagoResponseDTO;
import br.com.spotifyanalytics.application.dto.PaymentRequestDTO;
import br.com.spotifyanalytics.application.service.PaymentServiceImpl;
import br.com.spotifyanalytics.domain.repository.UserRepoServiceImpl;
import br.com.spotifyanalytics.infra.persistence.entity.UsuariosJpa;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;

import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class PaymentsService implements PaymentServiceImpl
{
    private final UserRepoServiceImpl userRepoService;
    private final WebClient ms2WebClient;

    public PaymentsService(UserRepoServiceImpl userRepoService, WebClient ms2WebClient) {
        this.userRepoService = userRepoService;
        this.ms2WebClient = ms2WebClient;
    }

    @Override
    public MercadoPagoResponseDTO getLinkPagamento(String username) throws ExecutionException, InterruptedException {
        UsuariosJpa usuariosJpa = this.userRepoService.findBySpotifyId(username);
        PaymentRequestDTO request = PaymentRequestDTO.build(username,usuariosJpa.getNome());
        return chamarMs2(request).get();
    }

    @CircuitBreaker(name = "ms2-service", fallbackMethod = "fallbackMs2")
    @TimeLimiter(name = "ms2-service")
    @Retry(name = "ms2-service")
    public CompletableFuture<MercadoPagoResponseDTO> chamarMs2(PaymentRequestDTO requestDTO) {
        return ms2WebClient.get()
                .uri("/payments")
                .retrieve()
                .bodyToMono(MercadoPagoResponseDTO.class)
                .toFuture();
    }

    public CompletableFuture<String> fallbackMs2(PaymentRequestDTO requestDTO,Throwable ex) {
        throw new RuntimeException("Serviço de pagamento indisponível. Tente novamente mais tarde.");

    }
}
