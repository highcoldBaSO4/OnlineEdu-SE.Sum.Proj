package com.se231.onlineedu.service;

import com.se231.onlineedu.model.VerificationToken;

/**
 * @author liu
 * @date 2019/07/11
 */
public interface VerificationTokenService {

    /**
     * verify if token correct
     * @param verificationToken
     * @param token
     */
    void verify(VerificationToken verificationToken, String token);

    /**
     * generate verification token
     *
     * @return
     */
    VerificationToken generateToken();
}
