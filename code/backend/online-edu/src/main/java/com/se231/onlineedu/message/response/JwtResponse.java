package com.se231.onlineedu.message.response;

/**
 * JwtReponse Class
 *
 * response when sign in successfully
 *
 * @author Yuxuan Liu
 *
 * @date 2019/07/01
 */
public class JwtResponse {
    private String accessToken;
    private String tokenType;

    public JwtResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType == null? "Bearer" : tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public JwtResponse() {
    }
}
