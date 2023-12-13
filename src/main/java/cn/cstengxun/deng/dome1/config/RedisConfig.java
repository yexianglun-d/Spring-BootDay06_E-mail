package cn.cstengxun.deng.dome1.config;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @ClassName RedisConfig
 * @Description TODO
 * @Author Mr.Deng
 * @Date 2023/12/1 17:49
 * @Version 1.0
 */
@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory redisConnectionFactory) {

        final RedisTemplate<String, Object> template = new RedisTemplate<>();

        // 使用fastjson序列化
        final FastJsonRedisSerializer<?> fastJsonRedisSerializer = new FastJsonRedisSerializer<>(Object.class);

        // key的序列化采用StringRedisSerializer
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        // value值的序列化采用fastJsonRedisSerializer
        template.setValueSerializer(fastJsonRedisSerializer);
        template.setHashValueSerializer(fastJsonRedisSerializer);

        // 开启事务
        template.setEnableTransactionSupport(true);
        template.setConnectionFactory(redisConnectionFactory);

        return template;
    }

}
