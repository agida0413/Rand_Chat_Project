package rand.api.web.config;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConnection;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.HashMap;
import java.util.Map;

@Slf4j
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

        LettuceConnectionFactory factory = new LettuceConnectionFactory(host,port);
        factory.start();  // 연결을 시작합니다.
        return factory;
    }
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory); // 동적 라우팅을 위한 RedisConnectionFactory 설정
        template.setKeySerializer(new StringRedisSerializer()); // key 직렬화 방식 설정
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer()); // value 직렬화 방식 설정
        return template;
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
            System.out.println("1111");

            throw new UnsupportedOperationException("Use getConnection(String key) instead.");
        }

        public RedisConnection getConnection(String key) {
            // 라우팅 로직을 구현
            String routingKey = determineRoutingKey(key);
            System.out.println("222222");
            System.out.println(key);
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
            log.info("key = {}",key);
            if (key.startsWith("cache")) {
                return "redis-cache";
            } else if (key.startsWith("chat")) {
                return "redis-chat";
            } else if (key.startsWith("token")) {
                return "redis-token";
            } else if (key.startsWith("persistent")) {
                return "redis-persistent";
            } else {
                throw new IllegalArgumentException("Unknown key prefix: " + key);
            }
        }

        @Override
        public DataAccessException translateExceptionIfPossible(RuntimeException ex) {
            return null;
        }
    }
}
