//package com.se231.onlineedu;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.se231.onlineedu.message.request.CourseApplicationForm;
//import com.se231.onlineedu.message.request.TitleAndDes;
//import com.se231.onlineedu.model.*;
//import com.se231.onlineedu.repository.RoleRepository;
//import com.se231.onlineedu.repository.UserRepository;
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
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.List;
//
//import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
///**
// * Course Test
// *
// * Test operation about opening a new course(and prototype)
// *
// * @author Zhe Li
// *
// * @date 2019/7/5
// */
//@SpringBootTest
//@RunWith(SpringRunner.class)
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
//public class CourseTest {
//    //initialize test case and local variable
//    private static String userSignIn = "{\n" +
//            "\t\"username\":\"user\",\n" +
//            "\t\"password\":\"password\""+
//            "}";
//
//    private static String adminSignIn = "{\n" +
//            "\t\"username\":\"admin\",\n" +
//            "\t\"password\":\"password\"" +
//            "}";
//
//    private static List<Role> userRole=new ArrayList<>();
//
//    private static List<Role> adminRole=new ArrayList<>();
//
//    private static CoursePrototype coursePrototype1= new CoursePrototype();
//
//    private static CourseApplicationForm applyCourse1 = new CourseApplicationForm();
//    private static CourseApplicationForm applyCourse2 = new CourseApplicationForm();
//    private static CourseApplicationForm applyCourse3 = new CourseApplicationForm();
//
//    private static String nullString= JSONObject.toJSONString(new TitleAndDes());
//
//    private static String shortTitle= JSONObject.toJSONString(new TitleAndDes("as",""));
//
//
//
//    private static String LongTitle="{\n" +
//            "\"title\":\"qwertyuiopasdfghjklzxcvbnmqwertyuiuop\"}";
//
//    private static String titleAndDes="{\"title\":\"English\",\n" +
//            "\"description\":\"learing English\"}";
//
//    private static String longDes="{\"title\":\"MATH\",\n" +
//            "\"description\":\"abcdasdedwqwdjqwpiojqdwpjpodqwjpoaxjopasdjopdjqwopjfopqjo[pjkqw[pkd[qpklp[dqwk[pqwdkdqp[wkdqw[pkq[pke[]pqwle[peqwkle[pqwke[pqwmr;qlkq[wpekq[wp,dq[pl,eqw[],qwd[]qwd,[qwk,abcdasdedwqwdjqwpiojqdwpjpodqwjpoaxjopasdjopdjqwopjfopqjo[pjkqw[pkd[qpklp[dqwk[pqwdkdqp[wkdqw[pkq[pke[]pqwle[peqwkle[pqwkewpekq[wp,dq]pl,eqw[],qwd[]qwd,[qwkabcdasdedwqwdjqwpiojqdwpjpodqwjpoaxjopasdjopdjqwopjfopqjo[pjkqw[pkd[qpklp[dqwk[pqwdkdqp[wkdqw[pkq[pke[]pqwle[peqwkle[pqwke[pqwmr;qlkq[wpekq[wp,dq[pl,eqw[],qwd[]qwd,[qwk,abcdasdedwqwdjqwpiojqdwpjpodqwjpoaxjopasdjopdjqwopjfopqjo[pjkqw[pkd[qpklp[dqwk[pqwdkdqp[wkdqw[pkq[pke[]pqwle[peqwkle[pqwkewpekq[wp,dq]pl,eqw[],qwd[]qwd,[qwk,abcdasdedwqwdjqwpiojqdwpjpodqwjpoaxjopasdjopdjqwopjfopqjo[pjkqw[pkd[qpklp[dqwk[pqwdkdqp[wkdqw[pkq[pke[]pqwle[peqwkle[pqwke[pqwmr;qlkq[wpekq[wp,dq[pl,eqw[],qwd[]qwd,[qwk,abcdasdedwqwdjqwpiojqdwpjpodqwjpoaxjopasdjopdjqwopjfopqjo[pjkqw[pkd[qpklp[dqwk[pqwdkdqp[wkdqw[pkq[pke[]pqwle[peqwkle[pqwkewpekq[wp,dq]pl,eqw[],qwd[]qwd,[qwk,abcdasdedwqwdjqwpiojqdwpjpodqwjpoaxjopasdjopdjqwopjfopqjo[pjkqw[pkd[qpklp[dqwk[pqwdkdqp[wkdqw[pkq[pke[]pqwle[peqwkle[pqwke[pqwmr;qlkq[wpekq[wp,dq[pl,eqw[],qwd[]qwd,[qwk,abcdasdedwqwdjqwpiojqdwpjpodqwjpoaxjopasdjopdjqwopjfopqjo[pjkqw[pkd[qpklp[dqwk[pqwdkdqp[wkdqw[pkq[pke[]pqwle[peqwkle[pqwkewpekq[wp,dq]pl,eqw[],qwd[]qwd,[qwk,abcdasdedwqwdjqwpiojqdwpjpodqwjpoaxjopasdjopdjqwopjfopqjo[pjkqw[pkd[qpklp[dqwk[pqwdkdqp[wkdqw[pkq[pke[]pqwle[peqwkle[pqwke[pqwmr;qlkq[wpekq[wp,dq[pl,eqw[],qwd[]qwd,[qwk,abcdasdedwqwdjqwpiojqdwpjpodqwjpoaxjopasdjopdjqwopjfopqjo[pjkqw[pkd[qpklp[dqwk[pqwdkdqp[wkdqw[pkq[pke[]pqwle[peqwkle[pqwkewpekq[wp,dq]pl,eqw[],qwd[]qwd,[qwk,abcdasdedwqwdjqwpiojqdwpjpodqwjpoaxjopasdjopdjqwopjfopqjo[pjkqw[pkd[qpklp[dqwk[pqwdkdqp[wkdqw[pkq[pke[]pqwle[peqwkle[pqwke[pqwmr;qlkq[wpekq[wp,dq[pl,eqw[],qwd[]qwd,[qwk,abcdasdedwqwdjqwpiojqdwpjpodqwjpoaxjopasdjopdjqwopjfopqjo[pjkqw[pkd[qpklp[dqwk[pqwdkdqp[wkdqw[pkq[pke[]pqwle[peqwkle[pqwkewpekq[wp,dq]pl,eqw[],qwd[]qwd,[qwk,abcdasdedwqwdjqwpiojqdwpjpodqwjpoaxjopasdjopdjqwopjfopqjo[pjkqw[pkd[qpklp[dqwk[pqwdkdqp[wkdqw[pkq[pke[]pqwle[peqwkle[pqwke[pqwmr;qlkq[wpekq[wp,dq[pl,eqw[],qwd[]qwd,[qwk,abcdasdedwqwdjqwpiojqdwpjpodqwjpoaxjopasdjopdjqwopjfopqjo[pjkqw[pkd[qpklp[dqwk[pqwdkdqp[wkdqw[pkq[pke[]pqwle[peqwkle[pqwkewpekq[wp,dq]pl,eqw[],qwd[]qwd,[qwk,abcdasdedwqwdjqwpiojqdwpjpodqwjpoaxjopasdjopdjqwopjfopqjo[pjkqw[pkd[qpklp[dqwk[pqwdkdqp[wkdqw[pkq[pke[]pqwle[peqwkle[pqwke[pqwmr;qlkq[wpekq[wp,dq[pl,eqw[],qwd[]qwd,[qwk,abcdasdedwqwdjqwpiojqdwpjpodqwjpoaxjopasdjopdjqwopjfopqjo[pjkqw[pkd[qpklp[dqwk[pqwdkdqp[wkdqw[pkq[pke[]pqwle[peqwkle[pqwkewpekq[wp,dq]pl,eqw[],qwd[]qwd,[qwk,abcdasdedwqwdjqwpiojqdwpjpodqwjpoaxjopasdjopdjqwopjfopqjo[pjkqw[pkd[qpklp[dqwk[pqwdkdqp[wkdqw[pkq[pke[]pqwle[peqwkle[pqwke[pqwmr;qlkq[wpekq[wp,dq[pl,eqw[],qwd[]qwd,[qwk,abcdasdedwqwdjqwpiojqdwpjpodqwjpoaxjopasdjopdjqwopjfopqjo[pjkqw[pkd[qpklp[dqwk[pqwdkdqp[wkdqw[pkq[pke[]pqwle[peqwkle[pqwkewpekq[wp,dq]pl,eqw[],qwd[]qwd,[qwk,abcdasdedwqwdjqwpiojqdwpjpodqwjpoaxjopasdjopdjqwopjfopqjo[pjkqw[pkd[qpklp[dqwk[pqwdkdqp[wkdqw[pkq[pke[]pqwle[peqwkle[pqwke[pqwmr;qlkq[wpekq[wp,dq[pl,eqw[],qwd[]qwd,[qwk,abcdasdedwqwdjqwpiojqdwpjpodqwjpoaxjopasdjopdjqwopjfopqjo[pjkqw[pkd[qpklp[dqwk[pqwdkdqp[wkdqw[pkq[pke[]pqwle[peqwkle[pqwkewpekq[wp,dq]pl,eqw[],qwd[]qwd,[qwk,abcdasdedwqwdjqwpiojqdwpjpodqwjpoaxjopasdjopdjqwopjfopqjo[pjkqw[pkd[qpklp[dqwk[pqwdkdqp[wkdqw[pkq[pke[]pqwle[peqwkle[pqwke[pqwmr;qlkq[wpekq[wp,dq[pl,eqw[],qwd[]qwd,[qwk,abcdasdedwqwdjqwpiojqdwpjpodqwjpoaxjopasdjopdjqwopjfopqjo[pjkqw[pkd[qpklp[dqwk[pqwdkdqp[wkdqw[pkq[pke[]pqwle[peqwkle[pqwkewpekq[wp,dq]pl,eqw[],qwd[]qwd,[qwk,abcdasdedwqwdjqwpiojqdwpjpodqwjpoaxjopasdjopdjqwopjfopqjo[pjkqw[pkd[qpklp[dqwk[pqwdkdqp[wkdqw[pkq[pke[]pqwle[peqwkle[pqwke[pqwmr;qlkq[wpekq[wp,dq[pl,eqw[],qwd[]qwd,[qwk,abcdasdedwqwdjqwpiojqdwpjpodqwjpoaxjopasdjopdjqwopjfopqjo[pjkqw[pkd[qpklp[dqwk[pqwdkdqp[wkdqw[pkq[pke[]pqwle[peqwkle[pqwkewpekq[wp,dq]pl,eqw[],qwd[]qwd,[qwk,abcdasdedwqwdjqwpiojqdwpjpodqwjpoaxjopasdjopdjqwopjfopqjo[pjkqw[pkd[qpklp[dqwk[pqwdkdqp[wkdqw[pkq[pke[]pqwle[peqwkle[pqwke[pqwmr;qlkq[wpekq[wp,dq[pl,eqw[],qwd[]qwd,[qwk,abcdasdedwqwdjqwpiojqdwpjpodqwjpoaxjopasdjopdjqwopjfopqjo[pjkqw[pkd[qpklp[dqwk[pqwdkdqp[wkdqw[pkq[pke[]pqwle[peqwkle[pqwkewpekq[wp,dq]pl,eqw[],qwd[]qwd,[qwk,abcdasdedwqwdjqwpiojqdwpjpodqwjpoaxjopasdjopdjqwopjfopqjo[pjkqw[pkd[qpklp[dqwk[pqwdkdqp[wkdqw[pkq[pke[]pqwle[peqwkle[pqwke[pqwmr;qlkq[wpekq[wp,dq[pl,eqw[],qwd[]qwd,[qwk,abcdasdedwqwdjqwpiojqdwpjpodqwjpoaxjopasdjopdjqwopjfopqjo[pjkqw[pkd[qpklp[dqwk[pqwdkdqp[wkdqw[pkq[pke[]pqwle[peqwkle[pqwkewpekq[wp,dq]pl,eqw[],qwd[]qwd,[qwk,abcdasdedwqwdjqwpiojqdwpjpodqwjpoaxjopasdjopdjqwopjfopqjo[pjkqw[pkd[qpklp[dqwk[pqwdkdqp[wkdqw[pkq[pke[]pqwle[peqwkle[pqwke[pqwmr;qlkq[wpekq[wp,dq[pl,eqw[],qwd[]qwd,[qwk,abcdasdedwqwdjqwpiojqdwpjpodqwjpoaxjopasdjopdjqwopjfopqjo[pjkqw[pkd[qpklp[dqwk[pqwdkdqp[wkdqw[pkq[pke[]pqwle[peqwkle[pqwkewpekq[wp,dq]pl,eqw[],qwd[]qwd,[qwk,abcdasdedwqwdjqwpiojqdwpjpodqwjpoaxjopasdjopdjqwopjfopqjo[pjkqw[pkd[qpklp[dqwk[pqwdkdqp[wkdqw[pkq[pke[]pqwle[peqwkle[pqwke[pqwmr;qlkq[wpekq[wp,dq[pl,eqw[],qwd[]qwd,[qwk,abcdasdedwqwdjqwpiojqdwpjpodqwjpoaxjopasdjopdjqwopjfopqjo[pjkqw[pkd[qpklp[dqwk[pqwdkdqp[wkdqw[pkq[pke[]pqwle[peqwkle[pqwkewpekq[wp,dq]pl,eqw[],qwd[]qwd,[qwk,abcdasdedwqwdjqwpiojqdwpjpodqwjpoaxjopasdjopdjqwopjfopqjo[pjkqw[pkd[qpklp[dqwk[pqwdkdqp[wkdqw[pkq[pke[]pqwle[peqwkle[pqwke[pqwmr;qlkq[wpekq[wp,dq[pl,eqw[],qwd[]qwd,[qwk,abcdasdedwqwdjqwpiojqdwpjpodqwjpoaxjopasdjopdjqwopjfopqjo[pjkqw[pkd[qpklp[dqwk[pqwdkdqp[wkdqw[pkq[pke[]pqwle[peqwkle[pqwkewpekq[wp,dq]pl,eqw[],qwd[]qwd,[qwk,abcdasdedwqwdjqwpiojqdwpjpodqwjpoaxjopasdjopdjqwopjfopqjo[pjkqw[pkd[qpklp[dqwk[pqwdkdqp[wkdqw[pkq[pke[]pqwle[peqwkle[pqwke[pqwmr;qlkq[wpekq[wp,dq[pl,eqw[],qwd[]qwd,[qwk,abcdasdedwqwdjqwpiojqdwpjpodqwjpoaxjopasdjopdjqwopjfopqjo[pjkqw[pkd[qpklp[dqwk[pqwdkdqp[wkdqw[pkq[pke[]pqwle[peqwkle[pqwkewpekq[wp,dq]pl,eqw[],qwd[]qwd,[qwk,abcdasdedwqwdjqwpiojqdwpjpodqwjpoaxjopasdjopdjqwopjfopqjo[pjkqw[pkd[qpklp[dqwk[pqwdkdqp[wkdqw[pkq[pke[]pqwle[peqwkle[pqwke[pqwmr;qlkq[wpekq[wp,dq[pl,eqw[],qwd[]qwd,[qwk,abcdasdedwqwdjqwpiojqdwpjpodqwjpoaxjopasdjopdjqwopjfopqjo[pjkqw[pkd[qpklp[dqwk[pqwdkdqp[wkdqw[pkq[pke[]pqwle[peqwkle[pqwkewpekq[wp,dq]pl,eqw[],qwd[]qwd,[qwk,abcdasdedwqwdjqwpiojqdwpjpodqwjpoaxjopasdjopdjqwopjfopqjo[pjkqw[pkd[qpklp[dqwk[pqwdkdqp[wkdqw[pkq[pke[]pqwle[peqwkle[pqwke[pqwmr;qlkq[wpekq[wp,dq[pl,eqw[],qwd[]qwd,[qwk,abcdasdedwqwdjqwpiojqdwpjpodqwjpoaxjopasdjopdjqwopjfopqjo[pjkqw[pkd[qpklp[dqwk[pqwdkdqp[wkdqw[pkq[pke[]pqwle[peqwkle[pqwkewpekq[wp,dq]pl,eqw[],qwd[]qwd,[qwk,abcdasdedwqwdjqwpiojqdwpjpodqwjpoaxjopasdjopdjqwopjfopqjo[pjkqw[pkd[qpklp[dqwk[pqwdkdqp[wkdqw[pkq[pke[]pqwle[peqwkle[pqwke[pqwmr;qlkq[wpekq[wp,dq[pl,eqw[],qwd[]qwd,[qwk,abcdasdedwqwdjqwpiojqdwpjpodqwjpoaxjopasdjopdjqwopjfopqjo[pjkqw[pkd[qpklp[dqwk[pqwdkdqp[wkdqw[pkq[pke[]pqwle[peqwkle[pqwkewpekq[wp,dq]pl,eqw[],qwd[]qwd,[qwk,abcdasdedwqwdjqwpiojqdwpjpodqwjpoaxjopasdjopdjqwopjfopqjo[pjkqw[pkd[qpklp[dqwk[pqwdkdqp[wkdqw[pkq[pke[]pqwle[peqwkle[pqwke[pqwmr;qlkq[wpekq[wp,dq[pl,eqw[],qwd[]qwd,[qwk,abcdasdedwqwdjqwpiojqdwpjpodqwjpoaxjopasdjopdjqwopjfopqjo[pjkqw[pkd[qpklp[dqwk[pqwdkdqp[wkdqw[pkq[pke[]pqwle[peqwkle[pqwkewpekq[wp,dq]pl,eqw[],qwd[]qwd,[qwk,abcdasdedwqwdjqwpiojqdwpjpodqwjpoaxjopasdjopdjqwopjfopqjo[pjkqw[pkd[qpklp[dqwk[pqwdkdqp[wkdqw[pkq[pke[]pqwle[peqwkle[pqwke[pqwmr;qlkq[wpekq[wp,dq[pl,eqw[],qwd[]qwd,[qwk,abcdasdedwqwdjqwpiojqdwpjpodqwjpoaxjopasdjopdjqwopjfopqjo[pjkqw[pkd[qpklp[dqwk[pqwdkdqp[wkdqw[pkq[pke[]pqwle[peqwkle[pqwkewpekq[wp,dq]pl,eqw[],qwd[]qwd,[qwk,abcdasdedwqwdjqwpiojqdwpjpodqwjpoaxjopasdjopdjqwopjfopqjo[pjkqw[pkd[qpklp[dqwk[pqwdkdqp[wkdqw[pkq[pke[]pqwle[peqwkle[pqwke[pqwmr;qlkq[wpekq[wp,dq[pl,eqw[],qwd[]qwd,[qwk,abcdasdedwqwdjqwpiojqdwpjpodqwjpoaxjopasdjopdjqwopjfopqjo[pjkqw[pkd[qpklp[dqwk[pqwdkdqp[wkdqw[pkq[pke[]pqwle[peqwkle[pqwkewpekq[wp,dq]pl,eqw[],qwd[]qwd,[qwk,abcdasdedwqwdjqwpiojqdwpjpodqwjpoaxjopasdjopdjqwopjfopqjo[pjkqw[pkd[qpklp[dqwk[pqwdkdqp[wkdqw[pkq[pke[]pqwle[peqwkle[pqwke[pqwmr;qlkq[wpekq[wp,dq[pl,eqw[],qwd[]qwd,[qwk,abcdasdedwqwdjqwpiojqdwpjpodqwjpoaxjopasdjopdjqwopjfopqjo[pjkqw[pkd[qpklp[dqwk[pqwdkdqp[wkdqw[pkq[pke[]pqwle[peqwkle[pqwkewpekq[wp,dq]pl,eqw[],qwd[]qwd,[qwk,abcdasdedwqwdjqwpiojqdwpjpodqwjpoaxjopasdjopdjqwopjfopqjo[pjkqw[pkd[qpklp[dqwk[pqwdkdqp[wkdqw[pkq[pke[]pqwle[peqwkle[pqwke[pqwmr;qlkq[wpekq[wp,dq[pl,eqw[],qwd[]qwd,[qwk,abcdasdedwqwdjqwpiojqdwpjpodqwjpoaxjopasdjopdjqwopjfopqjo[pjkqw[pkd[qpklp[dqwk[pqwdkdqp[wkdqw[pkq[pke[]pqwle[peqwkle[pqwkewpekq[wp,dq]pl,eqw[],qwd[]qwd,[qwk,abcdasdedwqwdjqwpiojqdwpjpodqwjpoaxjopasdjopdjqwopjfopqjo[pjkqw[pkd[qpklp[dqwk[pqwdkdqp[wkdqw[pkq[pke[]pqwle[peqwkle[pqwke[pqwmr;qlkq[wpekq[wp,dq[pl,eqw[],qwd[]qwd,[qwk,abcdasdedwqwdjqwpiojqdwpjpodqwjpoaxjopasdjopdjqwopjfopqjo[pjkqw[pkd[qpklp[dqwk[pqwdkdqp[wkdqw[pkq[pke[]pqwle[peqwkle[pqwkewpekq[wp,dq]pl,eqw[],qwd[]qwd,[qwk,abcdasdedwqwdjqwpiojqdwpjpodqwjpoaxjopasdjopdjqwopjfopqjo[pjkqw[pkd[qpklp[dqwk[pqwdkdqp[wkdqw[pkq[pke[]pqwle[peqwkle[pqwke[pqwmr;qlkq[wpekq[wp,dq[pl,eqw[],qwd[]qwd,[qwk,abcdasdedwqwdjqwpiojqdwpjpodqwjpoaxjopasdjopdjqwopjfopqjo[pjkqw[pkd[qpklp[dqwk[pqwdkdqp[wkdqw[pkq[pke[]pqwle[peqwkle[pqwkewpekq[wp,dq]pl,eqw[],qwd[]qwd,[qwk,abcdasdedwqwdjqwpiojqdwpjpodqwjpoaxjopasdjopdjqwopjfopqjo[pjkqw[pkd[qpklp[dqwk[pqwdkdqp[wkdqw[pkq[pke[]pqwle[peqwkle[pqwke[pqwmr;qlkq[wpekq[wp,dq[pl,eqw[],qwd[]qwd,[qwk,abcdasdedwqwdjqwpiojqdwpjpodqwjpoaxjopasdjopdjqwopjfopqjo[pjkqw[pkd[qpklp[dqwk[pqwdkdqp[wkdqw[pkq[pke[]pqwle[peqwkle[pqwkewpekq[wp,dq]pl,eqw[],qwd[]qwd,[qwk,abcdasdedwqwdjqwpiojqdwpjpodqwjpoaxjopasdjopdjqwopjfopqjo[pjkqw[pkd[qpklp[dqwk[pqwdkdqp[wkdqw[pkq[pke[]pqwle[peqwkle[pqwke[pqwmr;qlkq[wpekq[wp,dq[pl,eqw[],qwd[]qwd,[qwk,abcdasdedwqwdjqwpiojqdwpjpodqwjpoaxjopasdjopdjqwopjfopqjo[pjkqw[pkd[qpklp[dqwk[pqwdkdqp[wkdqw[pkq[pke[]pqwle[peqwkle[pqwkewpekq[wp,dq]pl,eqw[],qwd[]qwd,[qwk,abcdasdedwqwdjqwpiojqdwpjpodqwjpoaxjopasdjopdjqwopjfopqjo[pjkqw[pkd[qpklp[dqwk[pqwdkdqp[wkdqw[pkq[pke[]pqwle[peqwkle[pqwke[pqwmr;qlkq[wpekq[wp,dq[pl,eqw[],qwd[]qwd,[qwk,abcdasdedwqwdjqwpiojqdwpjpodqwjpoaxjopasdjopdjqwopjfopqjo[pjkqw[pkd[qpklp[dqwk[pqwdkdqp[wkdqw[pkq[pke[]pqwle[peqwkle[pqwkewpekq[wp,dq]pl,eqw[],qwd[]qwd,[qwk,abcdasdedwqwdjqwpiojqdwpjpodqwjpoaxjopasdjopdjqwopjfopqjo[pjkqw[pkd[qpklp[dqwk[pqwdkdqp[wkdqw[pkq[pke[]pqwle[peqwkle[pqwke[pqwmr;qlkq[wpekq[wp,dq[pl,eqw[],qwd[]qwd,[qwk,abcdasdedwqwdjqwpiojqdwpjpodqwjpoaxjopasdjopdjqwopjfopqjo[pjkqw[pkd[qpklp[dqwk[pqwdkdqp[wkdqw[pkq[pke[]pqwle[peqwkle[pqwkewpekq[wp,dq]pl,eqw[],qwd[]qwd,[qwk,abcdasdedwqwdjqwpiojqdwpjpodqwjpoaxjopasdjopdjqwopjfopqjo[pjkqw[pkd[qpklp[dqwk[pqwdkdqp[wkdqw[pkq[pke[]pqwle[peqwkle[pqwke[pqwmr;qlkq[wpekq[wp,dq[pl,eqw[],qwd[]qwd,[qwk,abcdasdedwqwdjqwpiojqdwpjpodqwjpoaxjopasdjopdjqwopjfopqjo[pjkqw[pkd[qpklp[dqwk[pqwdkdqp[wkdqw[pkq[pke[]pqwle[peqwkle[pqwkewpekq[wp,dq]pl,eqw[],qwd[]qwd,[qwk,abcdasdedwqwdjqwpiojqdwpjpodqwjpoaxjopasdjopdjqwopjfopqjo[pjkqw[pkd[qpklp[dqwk[pqwdkdqp[wkdqw[pkq[pke[]pqwle[peqwkle[pqwke[pqwmr;qlkq[wpekq[wp,dq[pl,eqw[],qwd[]qwd,[qwk,abcdasdedwqwdjqwpiojqdwpjpodqwjpoaxjopasdjopdjqwopjfopqjo[pjkqw[pkd[qpklp[dqwk[pqwdkdqp[wkdqw[pkq[pke[]pqwle[peqwkle[pqwkewpekq[wp,dq]pl,eqw[],qwd[]qwd,[qwk,abcdasdedwqwdjqwpiojqdwpjpodqwjpoaxjopasdjopdjqwopjfopqjo[pjkqw[pkd[qpklp[dqwk[pqwdkdqp[wkdqw[pkq[pke[]pqwle[peqwkle[pqwke[pqwmr;qlkq[wpekq[wp,dq[pl,eqw[],qwd[]qwd,[qwk,abcdasdedwqwdjqwpiojqdwpjpodqwjpoaxjopasdjopdjqwopjfopqjo[pjkqw[pkd[qpklp[dqwk[pqwdkdqp[wkdqw[pkq[pke[]pqwle[peqwkle[pqwkewpekq[wp,dq]pl,eqw[],qwd[]qwd,[qwk,abcdasdedwqwdjqwpiojqdwpjpodqwjpoaxjopasdjopdjqwopjfopqjo[pjkqw[pkd[qpklp[dqwk[pqwdkdqp[wkdqw[pkq[pke[]pqwle[peqwkle[pqwke[pqwmr;qlkq[wpekq[wp,dq[pl,eqw[],qwd[]qwd,[qwk,abcdasdedwqwdjqwpiojqdwpjpodqwjpoaxjopasdjopdjqwopjfopqjo[pjkqw[pkd[qpklp[dqwk[pqwdkdqp[wkdqw[pkq[pke[]pqwle[peqwkle[pqwkewpekq[wp,dq]pl,eqw[],qwd[]qwd,[qwk,abcdasdedwqwdjqwpiojqdwpjpodqwjpoaxjopasdjopdjqwopjfopqjo[pjkqw[pkd[qpklp[dqwk[pqwdkdqp[wkdqw[pkq[pke[]pqwle[peqwkle[pqwke[pqwmr;qlkq[wpekq[wp,dq[pl,eqw[],qwd[]qwd,[qwk,abcdasdedwqwdjqwpiojqdwpjpodqwjpoaxjopasdjopdjqwopjfopqjo[pjkqw[pkd[qpklp[dqwk[pqwdkdqp[wkdqw[pkq[pke[]pqwle[peqwkle[pqwkewpekq[wp,dq]pl,eqw[],qwd[]qwd,[qwk,\"}";
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
//    @Autowired
//    private PasswordEncoder encoder;
//
//
//    private MockMvc mvc;
//
//    private String result;
//
//    private static User user = new User("user","");
//
//    private static User admin = new User("admin","");
//    /**
//     * work around for static requirement of beforeClass
//     * <p>
//     * see https://stackoverflow.com/questions/12087959/junit-run-set-up-method-once
//     */
//
//    private static boolean setUpIsDone = false;
//
//    @Before
//    public void setup() throws Exception {
//        if (setUpIsDone) {
//            mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
//            return;
//        }
//
//        userRole.add(roleRepository.save(new Role(RoleType.ROLE_USER)));
//        adminRole.add(roleRepository.save(new Role(RoleType.ROLE_ADMIN)));
//        roleRepository.save(new Role(RoleType.ROLE_SUPER_ADMIN));
//        roleRepository.save(new Role(RoleType.ROLE_TEACHING_ADMIN));
//
//        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
//
//        //initialize uer and admin
//        user.setPassword(encoder.encode("password"));
//        user.setTel(13345676543L);
//        user.setEmail("user@user.com");
//        user.setRoles(userRole);
//        user.setEnabled(true);
//        userRepository.save(user);
//
//        admin.setPassword(encoder.encode("password"));
//        admin.setEmail("admin@admin.com");
//        admin.setTel(13345676043L);
//        admin.setRoles(adminRole);
//        admin.setEnabled(true);
//        userRepository.save(admin);
//
//        //store a prototype
//        coursePrototype1.setTitle("English");
//        coursePrototype1.setId(1L);
//        coursePrototype1.setState(CoursePrototypeState.NOT_DECIDE);
//
//        //initialize several course test
//        // valid apply
//        Calendar startDate1 = Calendar.getInstance();
//        Calendar endDate1 = Calendar.getInstance();
//        startDate1.add(Calendar.DATE,10);
//        endDate1.add(Calendar.DATE,60);
//        applyCourse1.setStartDate(startDate1.getTime());
//        applyCourse1.setEndDate(endDate1.getTime());
//
//        //end date comes before start day
//        Calendar startDate2 = Calendar.getInstance();
//        Calendar endDate2 = Calendar.getInstance();
//        startDate2.add(Calendar.DATE,10);
//        endDate2.add(Calendar.DATE,5);
//        applyCourse2.setStartDate(startDate2.getTime());
//        applyCourse2.setEndDate(endDate2.getTime());
//
//        //test approval comes after start date
//        applyCourse3.setEndDate(endDate2.getTime());
//
//        applyCourse1.setCourseTitle("Math");
//
//        applyCourse2.setCourseTitle("Physics");
//
//        applyCourse3.setCourseTitle("Chemistry");
//
//        setUpIsDone=true;
//    }
//
//    @Test
//    @WithMockUser(roles = "ADMIN",username = "admin")
//    public void testCreateCourse() throws Exception {
//
//        mvc.perform(post("/api/auth/signin")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(adminSignIn));
//
//        mvc.perform(post("/api/coursePrototypes/")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(nullString))
//                .andExpect(status().isBadRequest());
//
//        mvc.perform(post("/api/coursePrototypes/")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(LongTitle))
//                .andExpect(status().isBadRequest());
//
//        mvc.perform(post("/api/coursePrototypes/")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(shortTitle))
//                .andExpect(status().isBadRequest());
//
//        result = mvc.perform(post("/api/coursePrototypes/")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(titleAndDes))
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString();
//
//        Assert.assertEquals(coursePrototype1.getTitle(),JSON.parseObject(result,CoursePrototype.class).getTitle());
//
//        mvc.perform(post("/api/coursePrototypes/")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(longDes))
//                .andExpect(status().isBadRequest());
//
//        result = mvc.perform(post("/api/coursePrototypes/1/create")
//                .contentType(MediaType.APPLICATION_JSON)
//                .param("decision","approval"))
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString();
//
//        Assert.assertEquals(CoursePrototypeState.USING,JSON.parseObject(result,CoursePrototype.class).getState());
//
//    }
//
//    @Test
//    @WithMockUser(username = "admin2",roles = "ADMIN")
//    public void applyCoursePrototypeTest() throws Exception{
//        mvc.perform(post("/api/auth/signin")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(adminSignIn));
//
//        mvc.perform(post("/api/coursePrototypes/")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(titleAndDes))
//                .andExpect(status().isOk());
//
//        mvc.perform(post("/api/coursePrototypes/1/apply"))
//                .andExpect(status().isOk());
//
//        mvc.perform(post("/api/coursePrototypes/1/apply"))
//                .andExpect(status().isOk());
//
//        mvc.perform(post("/api/coursePrototypes/apply")
//                .param("decision","disapproval")
//                .param("coursePrototype_id","1")
//                .param("applicant_id","2"))
//                .andExpect(status().isOk());
//
//    }
//
//    @Test
//    @WithMockUser(username = "admin2",roles = "ADMIN")
//    public void applyCourseTest() throws Exception{
//        mvc.perform(post("/api/auth/signin")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(adminSignIn));
//
//        mvc.perform(post("/api/coursePrototypes/")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(titleAndDes))
//                .andExpect(status().isOk());
//
//        mvc.perform(post("/api/courses/start")
//                .contentType(MediaType.APPLICATION_JSON)
//                .param("prototypeId","1")
//                .content(JSONObject.toJSONString(applyCourse1)))
//                .andExpect(status().isOk());
//
//        mvc.perform(post("/api/courses/start")
//                .contentType(MediaType.APPLICATION_JSON)
//                .param("prototypeId","1")
//                .content(JSONObject.toJSONString(applyCourse1)))
//                .andExpect(status().isOk());
//
//        try {
//            mvc.perform(post("/api/courses/start")
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .param("prototypeId", "1")
//                    .content(JSONObject.toJSONString(applyCourse2)))
//                    .andExpect(status().isInternalServerError())
//                    .andExpect(content().string("end date comes before start date!")).andDo(print());
//        } catch (Exception e){
//        }
//        Calendar startDate3 =Calendar.getInstance();
//        startDate3.add(Calendar.MINUTE,1);
//        applyCourse3.setStartDate(startDate3.getTime());
//
//        mvc.perform(post("/api/courses/start")
//                .contentType(MediaType.APPLICATION_JSON)
//                .param("prototypeId","1")
//                .content(JSONObject.toJSONString(applyCourse3)))
//                .andExpect(status().isOk());
//
//        Thread.sleep(60000);
//
//        result = mvc.perform(post("/api/courses/1/start")
//                .param("decision","approval"))
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString();
//
//        Assert.assertEquals(CourseState.READY_TO_START,JSON.parseObject(result,Course.class).getState());
//
//        result = mvc.perform(post("/api/courses/3/start")
//                .param("decision","approval"))
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString();
//
//        Assert.assertEquals(CourseState.TEACHING,JSON.parseObject(result,Course.class).getState());
//
//        mvc.perform(post("/api/courses/start")
//                .contentType(MediaType.APPLICATION_JSON)
//                .param("prototypeId","1")
//                .content(JSONObject.toJSONString(applyCourse1)))
//                .andExpect(status().isOk());
//
//        result = mvc.perform(post("/api/courses/2/start")
//                .param("decision","isapproval"))
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString();
//
//        Assert.assertEquals(CourseState.APPLYING,JSON.parseObject(result,Course.class).getState());
//
//        result = mvc.perform(post("/api/courses/2/start")
//                .param("decision","disapproval"))
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString();
//
//        Assert.assertEquals(CourseState.NOT_PASS,JSON.parseObject(result,Course.class).getState());
//
//
//    }
//
//    @Test
//    @WithMockUser(username = "admin2",roles = "ADMIN")
//    public void pickCourseTest() throws Exception{
//        mvc.perform(post("/api/auth/signin")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(adminSignIn));
//
//        mvc.perform(post("/api/coursePrototypes/")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(titleAndDes))
//                .andExpect(status().isOk());
//
//        mvc.perform(post("/api/courses/start")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(JSONObject.toJSONString(applyCourse1))
//                .param("prototypeId","1"))
//                .andExpect(status().isOk());
//
//        mvc.perform(post("/api/courses/1/start")
//                .param("decision","approval"))
//                .andExpect(status().isOk());
//
//        mvc.perform(post("/api/auth/signin")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(userSignIn)).andExpect(status().isOk());
//
//        mvc.perform(post("/api/courses/1/pick"))
//                .andExpect(status().isOk());
//
//    }
//
//
//
//}