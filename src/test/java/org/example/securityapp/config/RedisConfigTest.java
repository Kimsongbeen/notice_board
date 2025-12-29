package org.example.securityapp.config;

import org.example.securityapp.common.config.RedisConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = RedisConfig.class)
@ActiveProfiles("test")
public class RedisConfigTest {

    @MockitoBean
    RedisConnectionFactory redisConnectionFactory;

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Test
    @DisplayName("빈 주입 체크")
    void redisTemplate_autowired_check() {
        assertThat(redisTemplate).isNotNull();
    }
}

