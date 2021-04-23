//package com.se231.onlineedu;
//
//import com.alibaba.fastjson.JSON;
//import com.se231.onlineedu.message.response.JwtResponse;
//import com.se231.onlineedu.message.response.PersonalInfo;
//import com.se231.onlineedu.model.Role;
//import com.se231.onlineedu.model.RoleType;
//import com.se231.onlineedu.model.User;
//import com.se231.onlineedu.model.VerificationToken;
//import com.se231.onlineedu.repository.RoleRepository;
//import com.se231.onlineedu.repository.UserRepository;
//import org.assertj.core.util.DateUtil;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.test.context.support.WithMockUser;
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
//import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
///**
// * Manage Personal Information Test
// * <p>
// * Test operation about managing personal information.
// *
// * @author Zhe Li
// * @date 2019/07/09
// */
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
//public class UserInfoTest {
//
//    //related repository
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private RoleRepository roleRepository;
//    //web context
//    @Autowired
//    private WebApplicationContext context;
//
//    @Autowired
//    private PasswordEncoder encoder;
//
//    private static String signUpResponse = "User registered successfully!";
//
//    private static boolean setUpIsDone = false;
//
//    private MockMvc mvc;
//
//    private User admin = new User("admin", "");
//
//    private User user1 = new User("user1", "");
//
//    private User user2 = new User("user2", "");
//
//    private List<Role> adminRole = new ArrayList<>();
//
//    private String result;
//
//    private static String adminSignIn = "{\n" +
//            "\t\"username\":\"admin\",\n" +
//            "\t\"password\":\"password\"" +
//            "}";
//
//    private static String user1SignIn = "{ \"username\":\"user1\",\n" +
//            "  \"password\":\"password\"\n" +
//            "}";
//
//    private static String user2SignIn = "{ \"username\":\"user2\",\n" +
//            "  \"password\":\"password\"\n" +
//            "}";
//
//    private static PersonalInfo personalInfo;
//
//    private static List<Role> roles = new ArrayList<>();
//
//    private static List<Role> userRole = new ArrayList<>();
//
//    public UserInfoTest() {
//    }
//
//    @Before
//    public void setup() throws Exception {
//        if (setUpIsDone) {
//            mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
//            return;
//        }
//        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
//
//        userRole.add(roleRepository.save(new Role(RoleType.ROLE_USER)));
//        adminRole.add(roleRepository.save(new Role(RoleType.ROLE_ADMIN)));
//        roleRepository.save(new Role(RoleType.ROLE_SUPER_ADMIN));
//        roleRepository.save(new Role(RoleType.ROLE_TEACHING_ADMIN));
//
//        admin.setRoles(adminRole);
//        admin.setPassword(encoder.encode("password"));
//        admin.setEmail("admin@qq.com");
//        admin.setTel(13345678901L);
//        admin.setEnabled(true);
//        userRepository.save(admin);
//
//        user1.setEmail("vabs@dv.com");
//        user1.setPassword(encoder.encode("password"));
//        user1.setTel(15678954652L);
//        user1.setRoles(userRole);
//        user1.setEnabled(true);
//        userRepository.save(user1);
//
//
//        user2.setEmail("vabs@dvsd.com");
//        user2.setPassword(encoder.encode("password"));
//        user2.setTel(15678954689L);
//        user2.setRoles(userRole);
//        user2.setEnabled(true);
//        userRepository.save(user2);
//
//        setUpIsDone = true;
//    }
//
//
//    @Test
//    @WithMockUser
//    public void checkAndModifyInfoTest() throws Exception {
//        mvc.perform(post("/api/auth/signin")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(user1SignIn))
//                .andExpect(status().isOk());
//
//        personalInfo = new PersonalInfo(userRepository.getOne(2L));
//
//        result = mvc.perform(get("/api/users/info"))
//                .andExpect(status().isOk())
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//
//        Assert.assertEquals(personalInfo, JSON.parseObject(result, PersonalInfo.class));
//
//        result = mvc.perform(post("/api/users/info/modify")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(JSON.toJSONString(personalInfo)))
//                .andExpect(status().isOk())
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//
//        Assert.assertEquals(personalInfo, JSON.parseObject(result, PersonalInfo.class));
//    }
//
//    @Test
//    @WithMockUser
//    public void getAndManageUserInfo() throws Exception {
//        mvc.perform(post("/api/auth/signin")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(adminSignIn))
//                .andExpect(status().isOk());
//
//        result = mvc.perform(get("/api/users/info/all"))
//                .andExpect(status().isOk())
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//
//        Assert.assertEquals(userRepository.findAll(), JSON.parseArray(result, User.class));
//
//
//        result = mvc.perform(post("/api/users/2/info/modify")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(JSON.toJSONString(personalInfo)))
//                .andExpect(status().isOk())
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//
//        Assert.assertEquals(personalInfo, JSON.parseObject(result, PersonalInfo.class));
//
//        result = mvc.perform(get("/api/users/checkSame/username")
//                .param("username", "user1"))
//                .andExpect(status().isOk())
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//
//        Assert.assertEquals("true", result);
//
//        result = mvc.perform(get("/api/users/checkSame/username")
//                .param("username", "use"))
//                .andExpect(status().isOk())
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//
//        Assert.assertEquals("false", result);
//
//        result = mvc.perform(get("/api/users/checkSame/email")
//                .param("email", "admin@qq.com"))
//                .andExpect(status().isOk())
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//
//        Assert.assertEquals("true", result);
//
//        result = mvc.perform(get("/api/users/checkSame/email")
//                .param("email", "uses@163.com"))
//                .andExpect(status().isOk())
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//
//        Assert.assertEquals("false", result);
//
//        result = mvc.perform(get("/api/users/checkSame/tel")
//                .param("tel", "18332112332"))
//                .andExpect(status().isOk())
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//
//        Assert.assertEquals("false", result);
//
//        result = mvc.perform(get("/api/users/checkSame/tel")
//                .param("tel", "13345678901"))
//                .andExpect(status().isOk())
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//
//        Assert.assertEquals("true", result);
//
//    }
//
////    @Test
////    public void updateAvatarTest() throws Exception {
////        result = mvc.perform(post("/api/auth/signin")
////                .contentType(MediaType.APPLICATION_JSON)
////                .content(adminSignIn))
////                .andExpect(status().isOk())
////                .andReturn().getResponse().getContentAsString();
////
////        JwtResponse jwtResponse = JSON.parseObject(result, JwtResponse.class);
////
////        byte[] bytes = new byte[512001];
////        MockMultipartFile avatarBig = new MockMultipartFile("avatar", "file1.jpg", "image/jpeg", bytes);
////        MockMultipartFile avatarWrong = new MockMultipartFile("avatar", "file1.txt", "plain/text", "abcd".getBytes());
////        MockMultipartFile avatar = new MockMultipartFile("avatar", "file1.jpg", "image/jpeg", "abcdefg".getBytes());
////        mvc.perform(MockMvcRequestBuilders.multipart("/api/users/1/avatar")
////                .file(avatarBig)
////                .header("Authorization", jwtResponse.getTokenType() + " " + jwtResponse.getAccessToken()))
////                .andExpect(status().isBadRequest())
////                .andExpect(content().string("exceeded max size"));
////
////        mvc.perform(MockMvcRequestBuilders.multipart("/api/users/1/avatar")
////                .file(avatarWrong)
////                .header("Authorization", jwtResponse.getTokenType() + " " + jwtResponse.getAccessToken()))
////                .andExpect(status().isBadRequest())
////                .andExpect(content().string("file format not supported"));
////
////
////        mvc.perform(MockMvcRequestBuilders.multipart("/api/users/1/avatar")
////                .file(avatar)
////                .header("Authorization", jwtResponse.getTokenType() + " " + jwtResponse.getAccessToken()))
////                .andExpect(status().isOk());
////    }
//
//    @Test
//    public void updatePassword() throws Exception {
//        result = mvc.perform(post("/api/auth/signin")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(adminSignIn))
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString();
//
//        JwtResponse jwtResponse = JSON.parseObject(result, JwtResponse.class);
//        mvc.perform(patch("/api/users/1/password")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content("{\"password\":\"password1\"}")
//                .header("Authorization", jwtResponse.getTokenType() + " " + jwtResponse.getAccessToken()))
//                .andExpect(status().isOk());
//
//        VerificationToken verificationTokenExpire = new VerificationToken("111111", DateUtil.yesterday());
//        VerificationToken verificationTokenWrong = new VerificationToken("111112");
//        VerificationToken verificationToken = new VerificationToken("111111");
//        Map<String, Object> sessionAttrs = new HashMap<>();
//        sessionAttrs.put("token",verificationToken);
//        sessionAttrs.put("password","password1");
//
//        mvc.perform(get("/api/users/1/password/confirm")
//                .sessionAttrs(sessionAttrs)
//                .param("verificationToken","111111")
//                .header("Authorization", jwtResponse.getTokenType() + " " + jwtResponse.getAccessToken()))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(status().isOk());
//
//        sessionAttrs.put("token", verificationTokenExpire);
//        mvc.perform(get("/api/users/1/password/confirm")
//                .sessionAttrs(sessionAttrs)
//                .param("verificationToken","111111")
//                .header("Authorization", jwtResponse.getTokenType() + " " + jwtResponse.getAccessToken()))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(status().isBadRequest());
//
//        sessionAttrs.put("token", verificationTokenWrong);
//        mvc.perform(get("/api/users/1/password/confirm")
//                .sessionAttrs(sessionAttrs)
//                .param("verificationToken","111111")
//                .header("Authorization", jwtResponse.getTokenType() + " " + jwtResponse.getAccessToken()))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(status().isBadRequest());
//
//    }
//
//    @Test
//    public void updateEmail() throws Exception {
//        result = mvc.perform(post("/api/auth/signin")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(adminSignIn))
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString();
//
//        JwtResponse jwtResponse = JSON.parseObject(result, JwtResponse.class);
//        mvc.perform(patch("/api/users/1/email")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content("{\"email\":\"paasfb1@aa.com\"}")
//                .header("Authorization", jwtResponse.getTokenType() + " " + jwtResponse.getAccessToken()))
//                .andExpect(status().isOk());
//
//        VerificationToken verificationTokenExpire = new VerificationToken("111111", DateUtil.yesterday());
//        VerificationToken verificationTokenWrong = new VerificationToken("111112");
//        VerificationToken verificationToken = new VerificationToken("111111");
//        Map<String, Object> sessionAttrs = new HashMap<>();
//        sessionAttrs.put("token",verificationToken);
//        sessionAttrs.put("email","paasfb1@aa.com");
//
//        mvc.perform(get("/api/users/1/email/confirm")
//                .sessionAttrs(sessionAttrs)
//                .param("verificationToken","111111")
//                .header("Authorization", jwtResponse.getTokenType() + " " + jwtResponse.getAccessToken()))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(status().isOk());
//
//        sessionAttrs.put("token", verificationTokenExpire);
//        mvc.perform(get("/api/users/1/email/confirm")
//                .sessionAttrs(sessionAttrs)
//                .param("verificationToken","111111")
//                .header("Authorization", jwtResponse.getTokenType() + " " + jwtResponse.getAccessToken()))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(status().isBadRequest());
//
//        sessionAttrs.put("token", verificationTokenWrong);
//        mvc.perform(get("/api/users/1/email/confirm")
//                .sessionAttrs(sessionAttrs)
//                .param("verificationToken","111111")
//                .header("Authorization", jwtResponse.getTokenType() + " " + jwtResponse.getAccessToken()))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(status().isBadRequest());
//    }
//
////    @Test
////    @WithMockUser(roles = {"ADMIN"})
////    public void importUserTest() throws Exception {
////        String path = UserInfoTest.class.getClassLoader().getResource("UserData.xlsx").getPath();
////        String pathEmpty = UserInfoTest.class.getClassLoader().getResource("UserDataEmpty.xlsx").getPath();
////        String pathSameUsername = UserInfoTest.class.getClassLoader().getResource("UserDataSameUsername.xlsx").getPath();
////        String pathSameEmail = UserInfoTest.class.getClassLoader().getResource("UserDataSameEmail.xlsx").getPath();
////        String pathSameTel = UserInfoTest.class.getClassLoader().getResource("UserDataSameTel.xlsx").getPath();
////
////        MockMultipartFile multipartFile = new MockMultipartFile("excel", "UserData.xlsx","plain/text", new FileInputStream(path));
////        MockMultipartFile multipartFileWrongFormat = new MockMultipartFile("excel", "UserData.xl","plain/text", new FileInputStream(path));
////        MockMultipartFile multipartFileEmpty = new MockMultipartFile("excel", "UserData.xlsx","plain/text", new FileInputStream(pathEmpty));
////        MockMultipartFile multipartFileSameUsername = new MockMultipartFile("excel", "UserData.xlsx","plain/text", new FileInputStream(pathSameUsername));
////        MockMultipartFile multipartFileSameEmail = new MockMultipartFile("excel", "UserData.xlsx","plain/text", new FileInputStream(pathSameEmail));
////        MockMultipartFile multipartFileSameTel = new MockMultipartFile("excel", "UserData.xlsx","plain/text", new FileInputStream(pathSameTel));
////
////
////        mvc.perform(MockMvcRequestBuilders.multipart("/api/users/bulkImport")
////                .file(multipartFileEmpty))
////                .andDo(MockMvcResultHandlers.print())
////                .andExpect(status().isBadRequest());
////
////        mvc.perform(MockMvcRequestBuilders.multipart("/api/users/bulkImport")
////                .file(multipartFileSameUsername))
////                .andDo(MockMvcResultHandlers.print())
////                .andExpect(status().isBadRequest());
////
////        mvc.perform(MockMvcRequestBuilders.multipart("/api/users/bulkImport")
////                .file(multipartFileSameEmail))
////                .andDo(MockMvcResultHandlers.print())
////                .andExpect(status().isBadRequest());
////
////        mvc.perform(MockMvcRequestBuilders.multipart("/api/users/bulkImport")
////                .file(multipartFileSameTel))
////                .andDo(MockMvcResultHandlers.print())
////                .andExpect(status().isBadRequest());
////
////        mvc.perform(MockMvcRequestBuilders.multipart("/api/users/bulkImport")
////                .file(multipartFile))
////                .andDo(MockMvcResultHandlers.print())
////                .andExpect(status().isOk());
////
////        mvc.perform(MockMvcRequestBuilders.multipart("/api/users/bulkImport")
////                .file(multipartFileWrongFormat))
////                .andDo(MockMvcResultHandlers.print())
////                .andExpect(status().isBadRequest());
////
////
////    }
//}
