package com.rand.config;

import com.rand.config.constant.PubSubChannel;
import com.rand.redis.pubsub.SubsCriber;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
@Configuration
public class MatchingRedisConfig {
    @Bean
    public RedisMessageListenerContainer redisContainer(
            RedisConnectionFactory connectionFactory, SubsCriber subscriber) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        // 매칭 채널
        container.addMessageListener(subscriber, new PatternTopic(PubSubChannel.MATCHING_CHANNEL.toString()));
        container.addMessageListener(subscriber, new PatternTopic(PubSubChannel.MATCHING_ACCEPT_CHANNEL.toString()));


        return container;
    }
}
