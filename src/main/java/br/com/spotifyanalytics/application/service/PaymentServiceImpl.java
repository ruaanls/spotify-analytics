package br.com.spotifyanalytics.application.service;

import br.com.spotifyanalytics.application.dto.MercadoPagoResponseDTO;

import java.util.concurrent.ExecutionException;

public interface PaymentServiceImpl
{
    MercadoPagoResponseDTO getLinkPagamento(String username) throws ExecutionException, InterruptedException;
}
