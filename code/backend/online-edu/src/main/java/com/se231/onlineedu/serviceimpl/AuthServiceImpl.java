package com.se231.onlineedu.serviceimpl;

import com.se231.onlineedu.exception.NotFoundException;
import com.se231.onlineedu.exception.ValidationException;
import com.se231.onlineedu.message.request.SignUpForm;
import com.se231.onlineedu.message.response.JwtResponse;
import com.se231.onlineedu.model.Role;
import com.se231.onlineedu.model.RoleType;
import com.se231.onlineedu.model.User;
import com.se231.onlineedu.model.VerificationToken;
import com.se231.onlineedu.repository.RoleRepository;
import com.se231.onlineedu.repository.UserRepository;
import com.se231.onlineedu.security.jwt.JwtProvider;
import com.se231.onlineedu.security.services.UserPrinciple;
import com.se231.onlineedu.service.AuthService;
import com.se231.onlineedu.service.EmailSenderService;
import com.se231.onlineedu.service.UserService;
import com.se231.onlineedu.service.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Service Implementation related to auth.
 *
 * contain the main service logic related to auth control
 *
 * @author Zhe Li
 *
 * @date 2019/07/08
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private VerificationTokenService verificationTokenService;


    @Autowired
    private EmailSenderService emailSenderService;


    @Override
    public JwtResponse userSignIn(String username, String password){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username,
                        password
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateJwtToken((UserPrinciple) authentication.getPrincipal());
        return new JwtResponse(jwt);
    }

    @Override
    public User userSignUp(SignUpForm form, HttpSession httpSession) {
        if(userRepository.existsByUsername(form.getUsername())) {
            throw new ValidationException("Fail -> Username is already taken");
        }

        if(userRepository.existsByEmail(form.getEmail())) {
            throw new ValidationException("Fail -> Email Address is already taken!");
        }

        if(userRepository.existsByTel(Long.parseLong(form.getTel()))) {
            throw new ValidationException("Fail -> Telephone Number is already taken!");
        }

        User user = new User(form);
        user.setPassword(encoder.encode(form.getPassword()));
        user.setEnabled(false);

        List<Role> roles=new ArrayList<>();
        Role userRole = roleRepository.findByRole(RoleType.ROLE_USER).
                orElseThrow(()->new RuntimeException("Fail -> Cause: User Role Not Found"));
        roles.add(userRole);
        user.setRoles(roles);
        httpSession.setAttribute("user", user);
        VerificationToken verificationToken = verificationTokenService.generateToken();
        httpSession.setAttribute("token", verificationToken);
        emailSenderService.sendVerificationEmail(user.getEmail(),verificationToken);
        return user;
    }

    @Override
    public String addTeachingAdmin(Long userId){
        User user = userService.getUserInfo(userId);
        Role teachingAdmin = roleRepository.findByRole(RoleType.ROLE_TEACHING_ADMIN).orElseThrow(() -> new NotFoundException("该角色不存在"));
        if(user.getRoles().contains(teachingAdmin)){
            throw new ValidationException("This User has already been a teaching admin.");
        }
        user.getRoles().add(teachingAdmin);
        userRepository.save(user);
        return "Add Teaching Admin successfully";
    }

    @Override
    public User saveRegisteredUser(User user) {
        user.setEnabled(true);
        return userRepository.save(user);
    }


}
