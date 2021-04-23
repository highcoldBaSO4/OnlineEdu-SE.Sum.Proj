package com.se231.onlineedu.jwtTest;


import com.se231.onlineedu.exception.BeforeStartException;
import com.se231.onlineedu.exception.NotFoundException;
import com.se231.onlineedu.exception.ValidationException;
import com.se231.onlineedu.security.jwt.JwtProvider;
import io.jsonwebtoken.Clock;
import org.assertj.core.util.DateUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;



import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.when;

/**
 * @author liu
 */
public class JwtProviderTest {
    private static final int EXPIRE = 360000;
    private static final int NEG_EXPIRE = -360000;
    private static final String SECRET = "ydtucvykb";
    private static final String SECRET_EMPTY = "";
    private static final String USERNAME = "testUser";
    private static final String USERNAME_EMPTY = "";
    private static final String ERROR_INFO = "abcd";

    @Mock
    private Clock clockMocker;

    @InjectMocks
    private JwtProvider jwtProvider;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
    }


    @Test(expected = NotFoundException.class)
    public void usernameNull(){
        when(clockMocker.now()).thenReturn(DateUtil.now());
        getToken(EXPIRE, SECRET, USERNAME_EMPTY, true);
    }

    @Test(expected = BeforeStartException.class)
    public void minusExpire(){
        when(clockMocker.now()).thenReturn(DateUtil.now());
        getToken(NEG_EXPIRE, SECRET, USERNAME, true);
    }

    @Test(expected = ValidationException.class)
    public void blankSecret(){
        when(clockMocker.now()).thenReturn(DateUtil.now());
        getToken(EXPIRE, SECRET_EMPTY, USERNAME, true);
    }

    @Test(expected = ValidationException.class)
    public void notActivated(){
        when(clockMocker.now()).thenReturn(DateUtil.now());
        getToken(EXPIRE, SECRET, USERNAME, false);
    }


    @Test
    public void differentTimeShouldCreateDifferentTokens(){
        when(clockMocker.now())
                .thenReturn(DateUtil.yesterday())
                .thenReturn(DateUtil.now());
        final String token = getToken();
        final String tokenLater = getToken();
        assertThat(token).isNotEqualTo(tokenLater);
    }

    @Test
    public void usernameFromTokenShouldBeTheSameAsItWas(){
        when(clockMocker.now()).thenReturn(DateUtil.now());

        final String token = getToken();

        assertThat(jwtProvider.getUserNameFromJwtToken(token)).isEqualTo(USERNAME);
    }


    @Test(expected = ValidationException.class)
    public void emptyToken(){
        jwtProvider.getUserNameFromJwtToken("");
    }
    @Test
    public void failCases(){
        when(clockMocker.now()).thenReturn(DateUtil.yesterday());

        final String token = getToken();

        assertFalse("invalid token", jwtProvider.validateJwtToken(ERROR_INFO + token));
        assertFalse("expired token", jwtProvider.validateJwtToken(token));
        assertFalse("empty token", jwtProvider.validateJwtToken(""));
    }

    private String getToken(int jwtExpiration, String secret, String username, boolean isEnabled) {
        ReflectionTestUtils.setField(jwtProvider, "jwtExpiration", jwtExpiration); // an hour
        ReflectionTestUtils.setField(jwtProvider, "jwtSecret",secret);

        return jwtProvider.generateJwtToken(new UserDetailsDummy(username, isEnabled));
    }

    private String getToken(){
        ReflectionTestUtils.setField(jwtProvider, "jwtExpiration", EXPIRE); // an hour
        ReflectionTestUtils.setField(jwtProvider, "jwtSecret",SECRET);

        return jwtProvider.generateJwtToken(new UserDetailsDummy(USERNAME));
    }
}