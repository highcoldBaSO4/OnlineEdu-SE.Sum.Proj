package com.se231.onlineedu.serviceimpl;

import com.se231.onlineedu.exception.NotFoundException;
import com.se231.onlineedu.exception.ValidationException;
import com.se231.onlineedu.message.request.TitleAndDes;
import com.se231.onlineedu.model.*;
import com.se231.onlineedu.repository.ApplyRepository;
import com.se231.onlineedu.repository.CoursePrototypeRepository;
import com.se231.onlineedu.repository.ResourceRepository;
import com.se231.onlineedu.service.CoursePrototypeService;
import com.se231.onlineedu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of course service interface
 *
 * contains the main service logic of backend about course.
 *
 * @author Zhe Li
 *
 * @date 2019/7/3
 */
@Service
public class CoursePrototypeServiceImpl implements CoursePrototypeService {
    private static final String approval = ApplyState.APPROVAL.toString().toLowerCase();

    @Autowired
    private UserService userService;


    @Autowired
    private CoursePrototypeRepository coursePrototypeRepository;

    @Autowired
    private ApplyRepository applyRepository;

    @Autowired
    private ResourceRepository resourceRepository;


    @Override
    public CoursePrototype createCourse(TitleAndDes form, Long userId) {
        CoursePrototype coursePrototype = new CoursePrototype();
        coursePrototype.setTitle(form.getTitle());
        coursePrototype.setDescription(form.getDescription());
        coursePrototype.setState(CoursePrototypeState.NOT_DECIDE);
        User user = userService.getUserInfo(userId);
        List<User> userList = new ArrayList<>();
        userList.add(user);
        coursePrototype.setUser(userList);
        return coursePrototypeRepository.save(coursePrototype);
    }

    @Override
    public Apply applyForCourse(Long coursePrototypeId, Long userId) {
        CoursePrototype coursePrototype = getCoursePrototypeInfo(coursePrototypeId);
        User user=userService.getUserInfo(userId);
        ApplyPrimaryKey id = new ApplyPrimaryKey(user,coursePrototype);
        Apply application;
        if(!applyRepository.existsById(id)){
            application=new Apply(id);
            return applyRepository.save(application);
        }
        else {
            return applyRepository.getOne(id);
        }
    }

    @Override
    public CoursePrototype getCoursePrototypeInfo(Long id){
        return coursePrototypeRepository.findById(id).orElseThrow(()->new NotFoundException("该课程原型不存在"));
    }

    @Override
    public CoursePrototype decideCreateCourse(Long coursePrototypeId,String decision){
        CoursePrototype coursePrototype = getCoursePrototypeInfo(coursePrototypeId);
        switch (decision) {
            case "approval":
                coursePrototype.setState(CoursePrototypeState.USING);
                break;
            case "disapproval":
                coursePrototype.setState(CoursePrototypeState.DENIAL);
                break;
            default:
                coursePrototype.setState(CoursePrototypeState.NOT_DECIDE);
                throw new RuntimeException("Unknown decision");
        }
        return coursePrototypeRepository.save(coursePrototype);
    }

    @Override
    public Apply decideUseCourse(Long coursePrototypeId,Long applicantId,String decision) {
        CoursePrototype coursePrototype = getCoursePrototypeInfo(coursePrototypeId);
        User user = userService.getUserInfo(applicantId);
        ApplyPrimaryKey applyPrimaryKey=new ApplyPrimaryKey(user,coursePrototype);
        Apply apply = applyRepository.findById(applyPrimaryKey).orElseThrow(()->new NotFoundException("该申请不存在"));
        apply.setApplyState(ApplyState.valueOf(decision.toUpperCase()));
        if(approval.equals(decision)){
            coursePrototype.getUsers().add(user);
            coursePrototypeRepository.save(coursePrototype);
        }
        return applyRepository.save(apply);
    }

    @Override
    public CoursePrototype saveResource(Long coursePrototypeId, Resource resource) {
        Resource resourceSaved = resourceRepository.save(resource);
        CoursePrototype coursePrototype = getCoursePrototypeInfo(coursePrototypeId);
        coursePrototype.getResources().add(resourceSaved);
        return coursePrototypeRepository.save(coursePrototype);
    }

    @Override
    public List<CoursePrototype> getAllCoursePrototype() {
        return coursePrototypeRepository.findAll();
    }

    @Override
    public List<Apply> getApplyByCoursePrototype(Long prototypeId) {
        return applyRepository.findAppliesByPrototypeId(prototypeId);
    }

    @Override
    public void deleteResources(Long coursePrototypeId, String name) {
        CoursePrototype coursePrototype = getCoursePrototypeInfo(coursePrototypeId);
        int index = 0;
        int size = coursePrototype.getResources().size();
        for(Resource resource :coursePrototype.getResources()){
            if(resource.getUrl().equals(name)){
                break;
            }
            index++;
        }
        if(index == size){
            throw new ValidationException("该文件不存在");
        }
        coursePrototype.getResources().remove(index);
        coursePrototypeRepository.save(coursePrototype);
    }
}
