package br.com.spotifyanalytics.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventResponseDTO
{
    @JsonProperty("event")
    private String event;
    @JsonProperty("status")
    private String status;
    @JsonProperty("payment_method")
    private String payment_method;
    @JsonProperty("username")
    private String username;
}
