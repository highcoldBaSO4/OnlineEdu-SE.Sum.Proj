package com.se231.onlineedu.controller;

import com.alibaba.fastjson.JSONObject;
import com.se231.onlineedu.exception.BulkImportDataException;
import com.se231.onlineedu.model.Question;
import com.se231.onlineedu.model.QuestionType;
import com.se231.onlineedu.service.QuestionService;
import com.se231.onlineedu.util.SaveFileUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Question Controller Class
 * <p>
 * controller of the api related to questions(based a existing course prototype.
 *
 * @author Zhe Li
 * @date 2019/7/5
 */
@Api(tags = "与课程原型中的题库有关的控制类")
@RestController
@RequestMapping("/api/coursePrototypes/{id}/questions")
public class QuestionController {
    private static int limit=5120000;

    @Autowired
    QuestionService questionService;

    private final String subjectiveString = QuestionType.SUBJECTIVE.toString();
    private final String trueOrFalseString = QuestionType.T_OR_F.toString();

    @ApiOperation("上传题目")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "课程原型的id", paramType = "path"),
            @ApiImplicitParam(name = "questionJSON", value = "题目的详细信息", paramType = "RequestBody")
    })
    @PostMapping("/submit")
    @PreAuthorize("hasAnyRole('TEACHING_ADMIN','ADMIN','SUPER_ADMIN')")
    public Question submitQuestion(@RequestBody JSONObject questionJSON,
                                                   @PathVariable("id") Long coursePrototypeId){
        String type = ((String) questionJSON.get("type")).toUpperCase();
        StringBuilder questionBuilder = new StringBuilder();
        questionBuilder.append((String) questionJSON.get("content"));
        List<String> options = (List<String>) questionJSON.get("options");
        if(!(subjectiveString.equals(type)||trueOrFalseString.equals(type))) {
            for (String option : options) {
                questionBuilder.append("\0\r" + option);
            }
        }
        else {
            questionBuilder.append("\0\r");
        }
        String question = questionBuilder.toString();

        String answer = (String) questionJSON.get("answer");
        return questionService.submitQuestion(coursePrototypeId,
                QuestionType.valueOf(type), question, answer);
    }


    @ApiOperation("上传题目图片")
    @PostMapping("/{questionId}")
    @PreAuthorize("hasAnyRole('TEACHING_ADMIN','ADMIN','SUPER_ADMIN')")
    public Question submitQuestionImage(@PathVariable("questionId") Long questionId, @RequestParam("file") MultipartFile[] multipartFiles) throws IOException {
        return questionService.saveImages(questionId, SaveFileUtil.saveImages(multipartFiles, limit));
    }


}
