//package com.se231.onlineedu.controller;
//
//import com.se231.onlineedu.model.Resource;
//import com.se231.onlineedu.service.CoursePrototypeService;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
///**
// * @author yuxuanLiu
// * @date 2019/07/31
// */
//@RunWith(SpringRunner.class)
//@ActiveProfiles("test")
//@WebMvcTest(ResourceController.class)
//public class ResourcesControllerTest {
//    @Autowired
//    private MockMvc mvc;
//
//    @MockBean
//    public CoursePrototypeService coursePrototypeService;
//
//    @Test
//    public void uploadFile() throws Exception {
//        File file = new File("src/test/resources/whale.png");
//        MockMultipartFile multipartFile = new MockMultipartFile("resource",new FileInputStream(file));
//        mvc.perform(multipart("/api/coursePrototypes/1/video").file(multipartFile))
//                .andExpect(status().isOk())
//                .andExpect(content().string("ok"));
//
//    }
//}
