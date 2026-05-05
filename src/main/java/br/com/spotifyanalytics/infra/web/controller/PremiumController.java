package br.com.spotifyanalytics.infra.web.controller;

import br.com.spotifyanalytics.application.dto.MercadoPagoResponseDTO;
import br.com.spotifyanalytics.application.service.PaymentServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/premium")
@Setter
@RequiredArgsConstructor
public class PremiumController
{
    private final PaymentServiceImpl paymentService;

    @GetMapping()
    public ResponseEntity<MercadoPagoResponseDTO> loginSpotifyRedirect (@AuthenticationPrincipal String username) throws ExecutionException, InterruptedException {
        return new ResponseEntity<>(paymentService.getLinkPagamento(username), HttpStatus.PERMANENT_REDIRECT);
    }
}
