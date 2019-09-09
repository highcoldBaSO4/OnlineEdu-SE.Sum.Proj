package com.se231.onlineedu.controller;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;
import com.se231.onlineedu.message.request.MarkForm;
import com.se231.onlineedu.message.request.SubmitAnswerForm;
import com.se231.onlineedu.model.PaperAnswer;
import com.se231.onlineedu.model.PaperAnswerState;
import com.se231.onlineedu.security.services.UserPrinciple;
import com.se231.onlineedu.service.PaperAnswerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


/**
 * @author Zhe Li
 * @date 2019/07/10
 */
@RestController
@Api(tags = "学生完成作业的控制类")
@RequestMapping("/api/courses/{courseId}/papers/{paperId}/answer")
public class PaperAnswerController {

    @Autowired
    PaperAnswerService paperAnswerService;

    @ApiOperation("学生提交作业")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "课程id",name="courseId",paramType = "path"),
            @ApiImplicitParam(value = "作业id",name="paperId",paramType = "path")
    })
    @PostMapping
    public PaperAnswer submitAnswer(@PathVariable("courseId")Long courseId,
                                                    @PathVariable("paperId")Long paperId,
                                                    @AuthenticationPrincipal UserPrinciple userPrinciple,
                                                    @Valid @RequestBody SubmitAnswerForm form)throws Exception{

        return paperAnswerService.submitAnswer(userPrinciple.getId(),courseId,paperId,form);
    }

    @ApiOperation("学生获取试卷信息")
    @GetMapping
    public List<PaperAnswer> getPersonalPaperAnswer(@PathVariable("paperId")Long paperId,
                                                    @AuthenticationPrincipal UserPrinciple userPrinciple){
        return paperAnswerService.getPersonalPaperAnswer(paperId,userPrinciple.getId());
    }

    @ApiOperation("教师批改学生回答")
    @PutMapping("/mark/{studentId}/{times}")
    public PaperAnswer markStudentPaper(@RequestBody Set<MarkForm> marks,
                                        @PathVariable("courseId")Long courseId,
                                        @PathVariable("paperId")Long paperId,
                                        @PathVariable("studentId")Long studentId,
                                        @PathVariable("times")Integer times){
        return paperAnswerService.markStudentPaper(courseId,studentId,paperId,times,marks);
    }

    @ApiOperation("提交主观题")
    @PostMapping("/subjective")
    public PaperAnswer submitSubjective(@PathVariable("courseId")Long courseId,
                                        @PathVariable("paperId")Long paperId,
                                        @AuthenticationPrincipal UserPrinciple userPrinciple,
                                        @RequestParam("file")MultipartFile[] file,
                                        @RequestParam("images")MultipartFile[] images,
                                        @RequestParam("questionId")Long questionId,
                                        @RequestParam("answerText")String answerText,
                                        @RequestParam("state")String state){
        return paperAnswerService.submitSubjectiveQuestion(courseId,userPrinciple.getId(),
                paperId,questionId,answerText,images,file, PaperAnswerState.valueOf(state));
    }

    @ApiOperation("改变提交的作业的状态")
    @PutMapping("/state/change")
    public PaperAnswer changeState(@PathVariable("courseId")Long courseId,
                                   @PathVariable("paperId")Long paperId,
                                   @AuthenticationPrincipal UserPrinciple userPrinciple,
                                   @RequestParam("state")String state){
        return paperAnswerService.changePaperAnswerState(courseId,userPrinciple.getId(),paperId,PaperAnswerState.valueOf(state));
    }

    @ApiOperation("教师获取某个学生的作业情况")
    @GetMapping("/{studentId}/all")
    public List<PaperAnswer> getStudentAnswer(@PathVariable("courseId")Long courseId,
                                              @PathVariable("paperId")Long paperId,
                                              @PathVariable("studentId")Long studentId){
        return paperAnswerService.getStudentAnswer(courseId,paperId,studentId);
    }
}
