package br.com.spotifyanalytics.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MercadoPagoResponseDTO
{
    private String redirectUrl;
}
