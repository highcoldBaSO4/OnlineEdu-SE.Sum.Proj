package com.se231.onlineedu.controller;

import javax.validation.Valid;
import java.util.List;
import com.se231.onlineedu.message.request.PaperForm;
import com.se231.onlineedu.message.response.PaperFinish;
import com.se231.onlineedu.model.Paper;
import com.se231.onlineedu.model.Section;
import com.se231.onlineedu.security.services.UserPrinciple;
import com.se231.onlineedu.service.PaperService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * Paper Controller Class
 *
 * controller of the api related to papers(based a existing course).
 *
 * @author Zhe Li
 *
 * @date 2019/7/5
 */
@Api(tags = "与作业，试卷相关的控制类")
@RestController
@RequestMapping("api/courses/{id}/papers")
public class PaperController {

    @Autowired
    private PaperService paperService;

    @ApiOperation("增加新试卷")
    @ApiImplicitParam(name = "id",value = "课程的id",paramType = "path")
    @PostMapping
    public ResponseEntity<Paper> addNewPaper(@PathVariable("id")Long courseId,
                                             @Valid @RequestBody PaperForm form)throws Exception{
        return ResponseEntity.ok(paperService.addNewPaper(form,courseId));
    }

    @ApiOperation("查询用户对课程的试卷状态")
    @GetMapping("/state")
    public List<PaperFinish> getPaperState(@PathVariable("id")Long courseId,
                                           @AuthenticationPrincipal UserPrinciple userPrinciple){
        return paperService.getPaperFinish(userPrinciple.getId(),courseId);
    }

    @ApiOperation("查询所有学生的作业完成情况")
    @GetMapping("/{paperId}/state/all")
    public List<PaperFinish> getStudentPaperState(@PathVariable("id")Long courseId,
                                                  @PathVariable("paperId")Long paperId){
        return paperService.getStudentFinish(courseId,paperId);
    }

}
