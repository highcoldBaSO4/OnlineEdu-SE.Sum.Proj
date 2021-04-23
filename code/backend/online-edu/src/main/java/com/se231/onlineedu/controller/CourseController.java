package com.se231.onlineedu.controller;

import com.se231.onlineedu.exception.FileFormatNotSupportException;
import com.se231.onlineedu.exception.FileSizeExceededException;
import com.se231.onlineedu.message.request.CourseApplicationForm;
import com.se231.onlineedu.message.request.CourseModifyForm;
import com.se231.onlineedu.message.response.CourseWithIdentity;
import com.se231.onlineedu.message.response.GradeTable;
import com.se231.onlineedu.model.Course;
import com.se231.onlineedu.model.Learn;
import com.se231.onlineedu.model.Notice;
import com.se231.onlineedu.model.User;
import com.se231.onlineedu.security.services.UserPrinciple;
import com.se231.onlineedu.service.CourseService;
import com.se231.onlineedu.util.FileCheckUtil;
import com.se231.onlineedu.util.SaveFileUtil;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * Course Controller Class
 * controller of the api related to course(concrete course)
 *
 * @author Zhe Li
 * @date 2019/7/4
 */
@Api(tags = "课程相关的控制类")
@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private static int limit = 5120000;

    @Autowired
    CourseService courseService;

    @ApiOperation(value = "教师上传公告")
    @PostMapping("/{id}/notices/")
    @PreAuthorize("hasAnyRole('TEACHING_ADMIN','ADMIN','SUPER_ADMIN')")
    public Course uploadNotice(@PathVariable Long id, @RequestBody Notice notice){
        return courseService.saveNotice(id, notice);
    }

    @ApiOperation(value = "教师基于已有的课程原型申请开课")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "prototypeId",value = "该课程基于的课程原型的id",paramType = "param"),
            @ApiImplicitParam(name = "form",paramType = "requestBody")
    })
    @PostMapping("/start")
    @PreAuthorize("hasAnyRole('TEACHING_ADMIN','ADMIN','SUPER_ADMIN')")
    public Course applyToStartCourse(@RequestParam("prototypeId") Long prototypeId,
                                     @Valid @RequestBody CourseApplicationForm form,
                                     @AuthenticationPrincipal UserPrinciple userPrinciple) {
        return courseService.applyToStartCourse(form,prototypeId,userPrinciple.getId());
    }

    @ApiOperation(value = "管理员审核教师的开课申请")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value ="生成的课程的id",paramType = "path"),
            @ApiImplicitParam(name = "decision",value = "决定",examples = @Example({
                    @ExampleProperty(value = "disapproval"),
                    @ExampleProperty(value = "approval")}))
    })
    @PostMapping("{id}/start")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public Course examineStartCourse(@RequestParam String decision,
                                     @PathVariable("id") Long courseId) {
        return courseService.examineStartCourseApplication(courseId, decision);
    }


    @ApiOperation("学生选课")
    @ApiImplicitParam(name = "id",value = "选课的学生的id",paramType = "param")
    @PostMapping("/{id}/pick")
    @PreAuthorize("hasRole('USER')")
    public Learn pickCourse(@PathVariable(name = "id")Long id,
                            @AuthenticationPrincipal UserPrinciple userPrinciple){
        return courseService.pickCourse(userPrinciple.getId(),id);
    }

    @ApiOperation("获取某课程的学生名单")
    @ApiImplicitParam(name = "id",value = "获取的课程的id",type = "path")
    @GetMapping("/{id}/students")
    public List<User> getStudentsList(@PathVariable(name = "id")Long courseId) {
        return courseService.getStudentsList(courseId);
    }

    @ApiImplicitParam(name = "id",value = "获取的课程的id",type = "path")
    @ApiOperation("获取某课程的信息")
    @GetMapping("/{id}/info")
    public CourseWithIdentity getCourseInfo(@PathVariable(name = "id")Long courseId, @AuthenticationPrincipal UserPrinciple userPrinciple) {
        return courseService.getCourseInfoWithIdentity(courseId, userPrinciple.getId());
    }

    @ApiOperation("获取所有课程的信息")
    @GetMapping("/all/info")
    public List<Course> getAllCourse() {
        return courseService.getAllCourse();
    }

    @ApiOperation(value = "用户可以上传课程的头像", httpMethod = "POST")
    @ApiImplicitParam(name = "id", value = "上传的课程id", type = "path")
    @PostMapping("/{id}/avatar")
    public Course patchAvatar(@PathVariable Long id, @RequestParam(value = "avatar") MultipartFile multipartFile) throws IOException {
        if (FileCheckUtil.checkImageSizeExceed(multipartFile,limit)) {
            throw new FileSizeExceededException("exceeded max size");
        }
        String suffix = multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf("."));
        if (FileCheckUtil.checkImageTypeWrong(suffix)) {
            throw new FileFormatNotSupportException("file format not supported");
        }
        return courseService.updateCourseAvatar(SaveFileUtil.saveFile(multipartFile,suffix), id);
    }

    @ApiOperation("查询用户是否选了某门课")
    @GetMapping("/{id}/isPicked")
    public Boolean checkWhetherPicked(@AuthenticationPrincipal UserPrinciple userPrinciple,
                                                      @PathVariable("id")Long id) {
        return courseService.checkIfUserPick(id,userPrinciple.getId());
    }

    @ApiOperation("管理员或教师修改课程信息")
    @PutMapping("/{id}/modify")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHING_ADMIN')")
    public Course modifyCourseInfo(@RequestBody CourseModifyForm form,
                                   @PathVariable("id")Long id) {
        return courseService.modifyCourseInfo(id,form);
    }

    @ApiOperation("教师将课程内学生转变为助教")
    @PostMapping("/{id}/teacherAssistant")
    public Course selectTeacherAssistant(@PathVariable Long id, @RequestParam("teacherAssistantId") Long teacherAssistantId) {
        return courseService.selectTeacherAssistant(id, teacherAssistantId);
    }

    @ApiOperation("获取课程的学生成绩单")
    @GetMapping("/{id}/scoreList")
    public GradeTable getGrade(@PathVariable("id")Long courseId){
        return courseService.getGrade(courseId);
    }

    @ApiOperation("修改学生成绩")
    @PutMapping("/{id}/{studentId}/grade")
    public Learn setGrade(@PathVariable("id")Long courseId,
                          @PathVariable("studentId")Long studentId,
                          @RequestParam("grade")double grade){
        return courseService.setGrade(studentId,courseId,grade);
    }

    @ApiOperation("批量导入学生成绩")
    @PutMapping("/{id}/bulkImportGrade")
    public String bulkImportGrade(@PathVariable("id")Long courseId,
                                  @RequestParam("excel")MultipartFile multipartFile)throws Exception{
        return courseService.bulkImportGrade(multipartFile,courseId);
    }

    @ApiOperation("删除某课程")
    @DeleteMapping("/{id}")
    public String deleteCourse(@PathVariable("id")Long id){
        return courseService.deleteCourse(id);
    }

    @ApiOperation("学生获取自己该门课的成绩")
    @GetMapping("/{id}/score")
    public double getScore(@AuthenticationPrincipal UserPrinciple userPrinciple,
                        @PathVariable(name = "id") Long courseId){
        return courseService.getScore(userPrinciple.getId(),courseId);
    }

}
