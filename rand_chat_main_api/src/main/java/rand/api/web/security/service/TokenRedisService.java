package rand.api.web.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rand.api.domain.common.repository.InMemRepository;
import rand.api.domain.common.repository.RedisRepositroy;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenRedisService implements TokenService{
    private final InMemRepository inMemRepository;

    @Override
    public void addRefresh(String key , String token) {
        TimeUnit timeUnit =TimeUnit.HOURS;

        long ttl = 24L;
        String rediskey = "refresh:"+key;
        inMemRepository.save(rediskey , token,ttl,timeUnit);
    }

    @Override
    public void deleteRefresh(String key , String token) {
        String rediskey = "refresh:"+key;

        String findToken =(String) inMemRepository.getValue(rediskey);

        if(findToken.equals(token)){
            inMemRepository.delete(rediskey);
        }

    }

    @Override
    public boolean isExist(String token) {
        String key = "refresh";

        boolean isEx = inMemRepository.scan(key,token);

        return isEx;
    }
}
