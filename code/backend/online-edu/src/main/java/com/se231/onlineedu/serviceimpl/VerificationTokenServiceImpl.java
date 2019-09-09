package com.se231.onlineedu.serviceimpl;

import com.se231.onlineedu.exception.VerificationTokenExpiredException;
import com.se231.onlineedu.exception.VerificationTokenWrongException;
import com.se231.onlineedu.model.VerificationToken;
import com.se231.onlineedu.service.VerificationTokenService;
import org.springframework.stereotype.Service;

import java.util.Calendar;

/**
 * @author liu
 * @date 2019/07/11
 */
@Service
public class VerificationTokenServiceImpl implements VerificationTokenService {
    @Override
    public void verify(VerificationToken verificationToken, String token){
        if(!verificationToken.getToken().equals(token)){
           throw new VerificationTokenWrongException();
        }
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            throw new VerificationTokenExpiredException();
        }
    }

    @Override
    public VerificationToken generateToken(){
        return new VerificationToken();
    }
}
