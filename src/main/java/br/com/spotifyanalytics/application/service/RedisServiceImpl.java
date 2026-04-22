package br.com.spotifyanalytics.application.service;

import org.springframework.stereotype.Service;

@Service
public interface RedisServiceImpl
{
    void saveTokenRedis(String id, String value, String type);
    String getTokenRedis(String id, String type);
}
