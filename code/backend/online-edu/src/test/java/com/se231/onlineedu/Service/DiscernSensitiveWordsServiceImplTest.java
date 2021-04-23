package com.se231.onlineedu.Service;

import com.se231.onlineedu.service.DiscernSensitiveWordsService;
import com.se231.onlineedu.serviceimpl.DiscernSensitiveWordsServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class DiscernSensitiveWordsServiceImplTest {
    @TestConfiguration
    static class DiscernSensitiveWordsServiceImplTestConfig{
        @Bean
        public DiscernSensitiveWordsService discernSensitiveWordsService(){
            return new DiscernSensitiveWordsServiceImpl();
        }
    }

    @Autowired
    private DiscernSensitiveWordsService discernSensitiveWordsService;

    @Test
    public void discern(){
//        assertThat(discernSensitiveWordsService.discern("佛年共产党")).isFalse();

        assertThat(discernSensitiveWordsService.discern("几年大吉")).isTrue();

    }
}
