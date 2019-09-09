package com.se231.onlineedu.Service;

import com.se231.onlineedu.model.Role;
import com.se231.onlineedu.model.RoleType;
import com.se231.onlineedu.model.User;
import com.se231.onlineedu.repository.RoleRepository;
import com.se231.onlineedu.repository.UserRepository;
import com.se231.onlineedu.security.jwt.JwtProvider;
import com.se231.onlineedu.service.AuthService;
import com.se231.onlineedu.service.EmailSenderService;
import com.se231.onlineedu.service.UserService;
import com.se231.onlineedu.service.VerificationTokenService;
import com.se231.onlineedu.serviceimpl.AuthServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
public class AuthServiceImplTest {
    @TestConfiguration
    static class AuthServiceImplTestContextConfig{
        @Bean
        public AuthService authService(){
            return new AuthServiceImpl();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }

    @Autowired
    private AuthService authService;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private JwtProvider jwtProvider;

    @MockBean
    private VerificationTokenService verificationTokenService;

    @MockBean
    private EmailSenderService emailSenderService;

    @Test
    public void saveRegisteredUser(){
        User user = new User();
        user.setPassword("123456");
        Optional<User> userOptional = Optional.of(user);

        Mockito.when(userRepository.findById(1L)).thenReturn(userOptional);
        Mockito.when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);
        User found = authService.saveRegisteredUser(user);
        assertThat(found.isEnabled()).isTrue();
    }

//    @Test
//    public void addTeachingAdmin(){
//        User user = new User();
//        user.setTel(531L);
//        user.setUsername("Liu");
//        user.setEmail("cdjddzy@foxmail.com");
//        user.setPassword("password");
//        user.setId(1L);
//        user.getRoles().add(new Role(RoleType.ROLE_USER));
//
//        Mockito.when(userService.getUserInfo(1L)).thenReturn(user);
//
//        String response = authService.addTeachingAdmin(1L);
//        assertThat(response).isEqualTo("Add Teaching Admin successfully");
//    }
}
