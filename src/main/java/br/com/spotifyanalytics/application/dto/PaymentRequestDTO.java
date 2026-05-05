package br.com.spotifyanalytics.application.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PaymentRequestDTO
{
    private String username;
    private PayerDTO payer;

    public static PaymentRequestDTO build (String username, String nome)
    {
        return PaymentRequestDTO.builder()
                .username(username)
                .payer(PayerDTO.builder()
                        .first_name(nome)
                        .identification(IdentificationDTO.builder()
                                .type("CPF")
                                .number("12345678909")
                                .build())
                        .build())
                .build();
    }
}
