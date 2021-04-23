package com.se231.onlineedu.Service;

import com.se231.onlineedu.exception.VerificationTokenExpiredException;
import com.se231.onlineedu.exception.VerificationTokenWrongException;
import com.se231.onlineedu.model.VerificationToken;
import com.se231.onlineedu.service.VerificationTokenService;
import com.se231.onlineedu.serviceimpl.VerificationTokenServiceImpl;
import org.assertj.core.util.DateUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class VerificationTokenServiceImplTest {
    @TestConfiguration
    static class VerificationTokenServiceConfig{
        @Bean VerificationTokenService verificationTokenService(){
            return new VerificationTokenServiceImpl();
        }
    }

    @Autowired
    private VerificationTokenService verificationTokenService;

    @Test
    public void verify(){
        VerificationToken verificationToken = new VerificationToken("111111", DateUtil.tomorrow());
        verificationTokenService.verify(verificationToken,"111111");
    }

    @Test(expected = VerificationTokenExpiredException.class)
    public void verifyExpired(){
        VerificationToken verificationToken = new VerificationToken("111111", DateUtil.yesterday());
        verificationTokenService.verify(verificationToken,"111111");
    }

    @Test(expected = VerificationTokenWrongException.class)
    public void verifyWrong(){
        VerificationToken verificationToken = new VerificationToken("1111121", DateUtil.tomorrow());
        verificationTokenService.verify(verificationToken,"111111");
    }

    @Test
    public void generate(){
        VerificationToken verificationToken = verificationTokenService.generateToken();
    }
}
