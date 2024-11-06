package rand.api.web.config;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConnection;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RedisRoutingConfig {

    private final Map<String, LettuceConnectionFactory> connectionFactories = new HashMap<>();

    public RedisRoutingConfig() {
        // 여러 Redis 서버에 대한 연결 설정
        connectionFactories.put("redis-cache", createConnectionFactory("redis-cache", 6379));
        connectionFactories.put("redis-chat", createConnectionFactory("redis-chat", 6382));
        connectionFactories.put("redis-chat-replica", createConnectionFactory("redis-chat-replica", 6383));
        connectionFactories.put("redis-token", createConnectionFactory("redis-token", 6384));
        connectionFactories.put("redis-token-replica", createConnectionFactory("redis-token-replica", 6385));
        connectionFactories.put("redis-persistent", createConnectionFactory("redis-persistent", 6380));
        connectionFactories.put("redis-persistent-replica", createConnectionFactory("redis-persistent-replica", 6381));
    }

    private LettuceConnectionFactory createConnectionFactory(String host, int port) {
        return new LettuceConnectionFactory(host, port);
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new DynamicRedisConnectionFactory(connectionFactories);
    }

    public static class DynamicRedisConnectionFactory implements RedisConnectionFactory {
        private final Map<String, LettuceConnectionFactory> connectionFactories;

        public DynamicRedisConnectionFactory(Map<String, LettuceConnectionFactory> connectionFactories) {
            this.connectionFactories = connectionFactories;
        }

        @Override
        public boolean getConvertPipelineAndTxResults() {
            return false;
        }
        @Override
        public RedisConnection getConnection() {
            // 기본적인 getConnection() 구현 (필요시 다른 방식으로 구현 가능)
            throw new UnsupportedOperationException("Use getConnection(String key) instead.");
        }

        public RedisConnection getConnection(String key) {
            // 라우팅 로직을 구현
            String routingKey = determineRoutingKey(key); // 이 메서드에서 키에 따라 연결할 Redis 서버를 결정
            LettuceConnectionFactory factory = connectionFactories.get(routingKey);
            if (factory != null) {
                return factory.getConnection();
            } else {
                throw new IllegalArgumentException("No connection factory found for key: " + routingKey);
            }
        }

        @Override
        public RedisClusterConnection getClusterConnection() {
            return null;
        }

        @Override
        public RedisSentinelConnection getSentinelConnection() {
            return null;
        }

        private String determineRoutingKey(String key) {
            String routingKey = null;
            if(key.startsWith("cache")){
                routingKey="redis-cache";
            }
          return routingKey;

        }


        @Override
        public DataAccessException translateExceptionIfPossible(RuntimeException ex) {
            return null;
        }
    }
}
