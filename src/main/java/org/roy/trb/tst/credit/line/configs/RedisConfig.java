package org.roy.trb.tst.credit.line.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

@Configuration
public class RedisConfig {

  @Bean
  public RedisTemplate<String, Integer> redisTemplate(
      RedisConnectionFactory connectionFactory) {
    RedisTemplate<String, Integer> template = new RedisTemplate<>();
    template.setConnectionFactory(connectionFactory);
    template.setKeySerializer(new Jackson2JsonRedisSerializer<>(String.class));
    template.setValueSerializer(new Jackson2JsonRedisSerializer<>(Integer.class));

    return template;
  }

}
