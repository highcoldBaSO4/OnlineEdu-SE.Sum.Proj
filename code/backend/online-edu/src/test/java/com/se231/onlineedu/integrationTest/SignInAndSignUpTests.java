//package com.se231.onlineedu;
//
//import com.alibaba.fastjson.JSON;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.se231.onlineedu.message.response.JwtResponse;
//import com.se231.onlineedu.model.Role;
//import com.se231.onlineedu.model.RoleType;
//import com.se231.onlineedu.model.User;
//import com.se231.onlineedu.model.VerificationToken;
//import com.se231.onlineedu.repository.RoleRepository;
//import com.se231.onlineedu.repository.UserRepository;
//import com.se231.onlineedu.service.VerificationTokenService;
//import org.assertj.core.util.DateUtil;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.test.annotation.DirtiesContext;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import static org.junit.Assert.assertTrue;
//import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
///**
// * UserLoginAndRegisterTests class
// * <p>
// * test user register, login, and authorization
// *
// * @author Yuxuan Liu
// * @date 2019/07/01
// */
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
//public class SignInAndSignUpTests {
//
//    @Mock
//    private VerificationTokenService verificationTokenService;
//
//    @Autowired
//    PasswordEncoder passwordEncoder;
//
//    private static String userSignUp = "{ \"username\":\"user1\",\n" +
//            "  \"password\":\"password\",\n" +
//            "  \"email\":\"12312412@qq.com\",\n" +
//            "  \"tel\":\"13345676543\",\n" +
//            "  \"university\":\"sjtu\",\n" +
//            "  \"major\":\"SE\",\n" +
//            "  \"grade\":2,\n" +
//            "  \"sno\":\"12321\",\n" +
//            "  \"realName\":\"小王\",\n" +
//            "  \"sex\":\"男\"\n" +
//            "}";
//
//    private static String userSignUpSameEmail = "{ \"username\":\"user2\",\n" +
//            "  \"password\":\"password\",\n" +
//            "  \"email\":\"12312412@qq.com\",\n" +
//            "  \"tel\":\"13345676543\",\n" +
//            "  \"university\":\"sjtu\",\n" +
//            "  \"major\":\"SE\",\n" +
//            "  \"grade\":2,\n" +
//            "  \"sno\":\"12321\",\n" +
//            "  \"realName\":\"小王\",\n" +
//            "  \"sex\":\"男\"\n" +
//            "}";
//
//    private static String userSignUpSameTel = "{ \"username\":\"user2\",\n" +
//            "  \"password\":\"password\",\n" +
//            "  \"email\":\"12312jk4hj12@qq.com\",\n" +
//            "  \"tel\":\"13345676543\",\n" +
//            "  \"university\":\"sjtu\",\n" +
//            "  \"major\":\"SE\",\n" +
//            "  \"grade\":2,\n" +
//            "  \"sno\":\"12321\",\n" +
//            "  \"realName\":\"小王\",\n" +
//            "  \"sex\":\"男\"\n" +
//            "}";
//
//    private static String userSignIn = "{\n" +
//            "\t\"username\":\"user1\",\n" +
//            "\t\"password\":\"password\"" +
//            "}";
//
//
//    private static String adminSignIn = "{\n" +
//            "\t\"username\":\"admin\",\n" +
//            "\t\"password\":\"password\"" +
//            "}";
//
//    private static String superAdminSignIn = "{\n" +
//            "\t\"username\":\"super_admin\",\n" +
//            "\t\"password\":\"password\"" +
//            "}";
//
//    private static String userContent = "User Contents";
//    private static String adminContent = "Admin Contents";
//    private static String superAdminContent = "Super Admin Contents";
//
//    private static String signUpResponse = "已激活";
//
//    private static String userSignInWrong = "{\n" +
//            "\t\"username\":\"user1\",\n" +
//            "\t\"password\":\"password1\"" +
//            "}";
//
//    private static List<Role> userRole=new ArrayList<>();
//
//    private static List<Role> adminRole=new ArrayList<>();
//
//    private static List<Role> superAdminRole=new ArrayList<>();
//
//
//    private static User admin = new User("admin","");
//
//    private static User superAdmin = new User("super_admin","");
//
//    @Autowired
//    private WebApplicationContext context;
//
//    @Autowired
//    private RoleRepository roleRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    private MockMvc mvc;
//
//    private String result;
//
//    private ObjectMapper objectMapper = new ObjectMapper();
//
//    /**
//     * work around for static requirement of beforeClass
//     * <p>
//     * see https://stackoverflow.com/questions/12087959/junit-run-set-up-method-once
//     */
//    private static boolean setUpIsDone = false;
//
//    @Mock
//    private VerificationToken verificationToken;
//
//    @Before
//    public void setup() throws Exception {
//        if (setUpIsDone) {
//            mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
//            return;
//        }
//
//        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
//
//        userRole.add(roleRepository.save(new Role(RoleType.ROLE_USER)));
//        adminRole.add(roleRepository.save(new Role(RoleType.ROLE_ADMIN)));
//        superAdminRole.add(roleRepository.save(new Role(RoleType.ROLE_SUPER_ADMIN)));
//        roleRepository.save(new Role(RoleType.ROLE_TEACHING_ADMIN));
//
//        admin.setRoles(adminRole);
//        admin.setPassword(passwordEncoder.encode("password"));
//        admin.setEmail("aaa@qq.com");
//        userRepository.save(admin);
//
//        result = mvc.perform(post("/api/auth/signin")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(adminSignIn))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(status().isBadRequest())
//                .andReturn().getResponse().getContentAsString();
//        User adminTmp = userRepository.findByUsername("admin").orElseThrow(()->new UsernameNotFoundException("username not found"));
//        adminTmp.setEnabled(true);
//        userRepository.save(adminTmp);
//
//        superAdmin.setEmail("bbb@126.com");
//        superAdmin.setPassword(passwordEncoder.encode("password"));
//        superAdmin.setRoles(superAdminRole);
//        superAdmin.setEnabled(true);
//        userRepository.save(superAdmin);
//
//        mvc.perform(post("/api/auth/signup")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(userSignUp)).andExpect(status().isOk())
//                .andReturn()
//                .getResponse();
//
//        User user = new User();
//        user.setPassword(passwordEncoder.encode("password"));
//        user.setEmail("12312412@qq.com");
//        user.setTel(13345676543L);
//        user.setUsername("user1");
//        user.setRoles(userRole);
//
//
//        Map<String, Object> sessionAttr = new HashMap<>();
//        sessionAttr.put("token", new VerificationToken("111111"));
//        sessionAttr.put("user", user);
//
//        result = mvc.perform(get("/api/auth/registrationConfirm?verificationToken=111111").sessionAttrs(sessionAttr)
//                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//
//        assertTrue(result.equals(signUpResponse));
//
//
//        setUpIsDone = true;
//    }
//
//    @Test
//    public void testWrongToken() throws Exception {
//        Map<String, Object> sessionAttr = new HashMap<>();
//        sessionAttr.put("token", new VerificationToken("111111", DateUtil.yesterday()));
//        sessionAttr.put("user", new User());
//
//        result = mvc.perform(get("/api/auth/registrationConfirm?verificationToken=111112").sessionAttrs(sessionAttr)
//                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//
//        result = mvc.perform(get("/api/auth/registrationConfirm?verificationToken=111111").sessionAttrs(sessionAttr)
//                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//    }
//
//    @Test
//    public void testSignupWithSameUsername() throws Exception {
//        result = mvc.perform(post("/api/auth/signup")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(userSignUp)).andExpect(status().isBadRequest())
//                .andReturn().getResponse().getContentAsString();
//
//        assertTrue(result.equals("Fail -> Username is already taken!"));
//    }
//
//
//    @Test
//    public void testSignupWithSameEmail() throws Exception {
//        result = mvc.perform(post("/api/auth/signup")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(userSignUpSameEmail)).andExpect(status().isBadRequest())
//                .andReturn().getResponse().getContentAsString();
//
//        assertTrue(result.equals("Fail -> Email Address is already taken!"));
//    }
//
//    @Test
//    public void testSignupWithSameTel() throws Exception {
//        result = mvc.perform(post("/api/auth/signup")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(userSignUpSameTel)).andExpect(status().isBadRequest())
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn().getResponse().getContentAsString();
//
//        assertTrue(result.equals("Fail -> Telephone Number is already taken!"));
//    }
//
//    @Test
//    public void testUserAuthorization() throws Exception {
//        result = mvc.perform(post("/api/auth/signin")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(userSignIn))
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString();
//
//
//        JwtResponse jwtResponse = JSON.parseObject(result, JwtResponse.class);
//
//        result = mvc.perform(get("/api/test/user").header("Authorization", jwtResponse.getTokenType() + " " + jwtResponse.getAccessToken()))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString();
//
//        assertTrue(result.equals(userContent));
//
//        mvc.perform(get("/api/test/admin").header("Authorization", jwtResponse.getTokenType() + " " + jwtResponse.getAccessToken()))
//                .andExpect(status().isForbidden());
//
//        mvc.perform(get("/api/test/superAdmin").header("Authorization", jwtResponse.getTokenType() + " " + jwtResponse.getAccessToken()))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    public void testAdminAuthorization() throws Exception {
//        result = mvc.perform(post("/api/auth/signin")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(adminSignIn))
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString();
//
//        JwtResponse jwtResponse = JSON.parseObject(result, JwtResponse.class);
//        result = mvc.perform(get("/api/test/user").header("Authorization", jwtResponse.getTokenType() + " " + jwtResponse.getAccessToken()))
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString();
//
//        assertTrue(result.equals(userContent));
//
//        result = mvc.perform(get("/api/test/admin").header("Authorization", jwtResponse.getTokenType() + " " + jwtResponse.getAccessToken()))
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString();
//
//        assertTrue(result.equals(adminContent));
//
//        mvc.perform(get("/api/test/superAdmin").header("Authorization", jwtResponse.getTokenType() + " " + jwtResponse.getAccessToken()))
//                .andExpect(status().isForbidden());
//
//        result = mvc.perform(post("/api/auth/3/teachingAdmin").header("Authorization", jwtResponse.getTokenType() + " " + jwtResponse.getAccessToken()))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString();
//
//        assertTrue(result.equals("Add Teaching Admin successfully"));
//
//        result = mvc.perform(post("/api/auth/3/teachingAdmin").header("Authorization", jwtResponse.getTokenType() + " " + jwtResponse.getAccessToken()))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString();
//
//        assertTrue(result.equals("This User has already been a teaching admin."));
//    }
//
//    @Test
//    public void testSuperAdminAuthorization() throws Exception {
//        result = mvc.perform(post("/api/auth/signin")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(superAdminSignIn))
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString();
//
//
//        JwtResponse jwtResponse = JSON.parseObject(result, JwtResponse.class);
//        result = mvc.perform(get("/api/test/user").header("Authorization", jwtResponse.getTokenType() + " " + jwtResponse.getAccessToken()))
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString();
//
//        assertTrue(result.equals(userContent));
//
//        result = mvc.perform(get("/api/test/admin").header("Authorization", jwtResponse.getTokenType() + " " + jwtResponse.getAccessToken()))
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString();
//
//        assertTrue(result.equals(adminContent));
//
//        result = mvc.perform(get("/api/test/superAdmin").header("Authorization", jwtResponse.getTokenType() + " " + jwtResponse.getAccessToken()))
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString();
//
//        assertTrue(result.equals(superAdminContent));
//    }
//
//    @Test
//    public void testWrongLogin() throws Exception {
//        mvc.perform(post("/api/auth/signin")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(userSignInWrong))
//                .andExpect(status().isUnauthorized());
//    }
//
//}
