package br.com.spotifyanalytics.application.usecases;

import br.com.spotifyanalytics.application.exception.TokenRedisNotFound;
import br.com.spotifyanalytics.application.service.RedisServiceImpl;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RedisService implements RedisServiceImpl
{

    private final RedisTemplate<String, Object> redisTemplate;
    private final Duration TOKEN_TTL = Duration.ofMinutes(58);

    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void saveTokenRedis(String id, String value, String type) {
        String key = buildKey(id, type);
        redisTemplate.opsForValue().set(key, value, TOKEN_TTL);
    }

    @Override
    public String getTokenRedis(String id, String type) {
        String key = buildKey(id, type);
        try
        {
            return (String) redisTemplate.opsForValue().get(key);
        }
        catch(Exception e)
        {
            throw new TokenRedisNotFound();
        }

    }

    private String buildKey(String id, String type)
    {
        return type+":" + id;
    }
}
