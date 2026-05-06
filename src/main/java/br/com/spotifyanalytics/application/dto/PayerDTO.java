package br.com.spotifyanalytics.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PayerDTO
{
    private String first_name;
    private IdentificationDTO identification;
}
