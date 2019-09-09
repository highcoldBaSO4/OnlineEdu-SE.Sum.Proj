package com.se231.onlineedu.service;

import java.util.List;
import com.se231.onlineedu.message.request.TitleAndDes;
import com.se231.onlineedu.model.Apply;
import com.se231.onlineedu.model.CoursePrototype;
import com.se231.onlineedu.model.Resource;


/**
 * Interface of courses' service
 *
 * Support service for courses.
 *
 * @author Zhe Li
 *
 * @date 2019/7/3
 */

public interface CoursePrototypeService {

    /**
     * Teaching Admin(or higher)could apply to create a new course prototype.
     *
     * @param userId the id of applicant
     * @param form create application form
     * @return response for front
     */
    CoursePrototype createCourse(TitleAndDes form, Long userId) ;

    /**
     * Teaching Admin could apply for the usage of a course prototype.
     * @param courseId  the applying course
     * @param userId the id of applicant
     * @return  response for front
     */
    Apply applyForCourse(Long courseId,Long userId) ;

    /**
     * Admin approve the application of create course.
     *
     * @param coursePrototypeId id of the application of creating a course
     * @param decision -1 represent disapproval while 1 represent approval
     * @return the course prototype
     */
    CoursePrototype decideCreateCourse(Long coursePrototypeId, String decision);

    /**
     * Admin approve the application of using a course
     * @param courseId id of the a course
     * @param applicantId id of the applicant
     * @param decision -1 represent disapproval while 1 represent approval
     * @return the application
     */
    Apply decideUseCourse(Long courseId, Long applicantId, String decision);

    /**
     * @param coursePrototypeId
     * @param resource
     * @return
     */
    CoursePrototype saveResource(Long coursePrototypeId, Resource resource);

    /**
     * this service allow user to get all prototypes
     * @return  list of all prototypes
     */
    List<CoursePrototype> getAllCoursePrototype();

    /**
     * this service allow admin to get all the apply to a specific course prototype
     * @param prototypeId   id of the prototype
     * @return  list of apply
     */
    List<Apply> getApplyByCoursePrototype(Long prototypeId);

    /**
     * @param id
     * @return
     */
    CoursePrototype getCoursePrototypeInfo(Long id);

    /**
     * @param coursePrototypeId
     * @param name
     */
    void deleteResources(Long coursePrototypeId, String name);

}
