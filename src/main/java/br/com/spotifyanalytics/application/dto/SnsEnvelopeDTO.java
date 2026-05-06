package br.com.spotifyanalytics.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SnsEnvelopeDTO
{
    @JsonProperty("Type")
    private String type;

    @JsonProperty("MessageId")
    private String messageId;

    @JsonProperty("Message")
    private String message; // ainda é String pois o SNS envia o JSON interno como texto escapado

    @JsonProperty("Timestamp")
    private String timestamp;
}
