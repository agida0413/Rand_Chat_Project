package rand.api.web.config.redis;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
public class DynamicLowLvRedis {
    // 각 Redis 설정 값을 @Value로 주입
    @Value("${spring.redis.cache.host}")
    private String cacheHost;

    @Value("${spring.redis.cache.port}")
    private int cachePort;

    @Value("${spring.redis.chat.host}")
    private String chatHost;

    @Value("${spring.redis.chat.port}")
    private int chatPort;

    @Value("${spring.redis.chat-replica.host}")
    private String chatReplicaHost;

    @Value("${spring.redis.chat-replica.port}")
    private int chatReplicaPort;

    @Value("${spring.redis.token.host}")
    private String tokenHost;

    @Value("${spring.redis.token.port}")
    private int tokenPort;

    @Value("${spring.redis.token-replica.host}")
    private String tokenReplicaHost;

    @Value("${spring.redis.token-replica.port}")
    private int tokenReplicaPort;

    @Value("${spring.redis.persistent.host}")
    private String persistentHost;

    @Value("${spring.redis.persistent.port}")
    private int persistentPort;

    @Value("${spring.redis.persistent-replica.host}")
    private String persistentReplicaHost;

    @Value("${spring.redis.persistent-replica.port}")
    private int persistentReplicaPort;
    //저급 lettuce 사용 컨피그
    private final Map<String, LettuceConnectionFactory> connectionFactories = new HashMap<>();

    @PostConstruct
    public void init() {
        log.error("김용준={}",cacheHost);
        // 여러 Redis 서버에 대한 연결 설정
        connectionFactories.put("redis-cache", createConnectionFactory(cacheHost, cachePort));
        connectionFactories.put("redis-chat", createConnectionFactory(chatHost, chatPort));
        connectionFactories.put("redis-chat-replica", createConnectionFactory(chatReplicaHost, chatReplicaPort));
        connectionFactories.put("redis-token", createConnectionFactory(tokenHost, tokenPort));
        connectionFactories.put("redis-token-replica", createConnectionFactory(tokenReplicaHost, tokenReplicaPort));
        connectionFactories.put("redis-persistent", createConnectionFactory(persistentHost, persistentPort));
        connectionFactories.put("redis-persistent-replica", createConnectionFactory(persistentReplicaHost, persistentReplicaPort));
    }
    public DynamicLowLvRedis() {

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

            throw new UnsupportedOperationException("Use getConnection(String key) instead.");
        }

        public RedisConnection getConnection(String key) {
            // 라우팅 로직을 구현
            String routingKey = determineRoutingKey(key);

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
