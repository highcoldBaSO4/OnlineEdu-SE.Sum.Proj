package com.se231.onlineedu.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.se231.onlineedu.exception.NotFoundException;
import com.se231.onlineedu.exception.NotMatchException;
import com.se231.onlineedu.message.request.PaperForm;
import com.se231.onlineedu.message.request.PaperQuestionForm;
import com.se231.onlineedu.message.response.PaperFinish;
import com.se231.onlineedu.model.*;
import com.se231.onlineedu.repository.*;
import com.se231.onlineedu.scheduler.SchedulerHandler;
import com.se231.onlineedu.service.CourseService;
import com.se231.onlineedu.service.PaperService;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Paper Service Interface Implementation Class
 *
 * contains main service logic related to paper.
 *
 * @author Zhe Li
 *
 * @date 2019/7/5
 */
@Service
public class PaperServiceImpl implements PaperService {

    @Autowired
    private PaperWithQuestionsRepository paperWithQuestionsRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private PaperRepository paperRepository;

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaperAnswerRepository paperAnswerRepository;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public Paper addNewPaper(PaperForm form, Long courseId) {
        Paper paper=new Paper();
        paper = paperRepository.save(paper);
        List <PaperWithQuestions> questions = new ArrayList<>();
        Course course = courseService.getCourseInfo(courseId);
        for(PaperQuestionForm paperQuestionForm:form.getQuestionFormList()){
                Question question = questionRepository.findById(paperQuestionForm.getQuestionId())
                        .orElseThrow(()->new NotFoundException("No corresponding question"));
                PaperWithQuestions paperWithQuestions = new PaperWithQuestions(paper,question,
                        paperQuestionForm.getQuestionNumber(),paperQuestionForm.getScore());
                questions.add(paperWithQuestionsRepository.save(paperWithQuestions));
        }
        paper.setQuestions(questions);
        paper.setStart(form.getStart());
        paper.setEnd(form.getEnd());
        paper.setTitle(form.getTitle());
        paper.setDescription(form.getDescription());
        paper.setCourse(course);
        paper=paperRepository.save(paper);
        try{
            SchedulerHandler.setAnswerStateAndAutoMark(form.getEnd(),paper.getId());
        }catch (SchedulerException e){
            e.printStackTrace();
        }
        return paper;
    }

    @Override
    public List<PaperFinish> getPaperFinish(Long userId, Long courseId) {
        List<PaperFinish> paperFinishList = new ArrayList<>();
        Course course = courseService.getCourseInfo(courseId);
        course.getPapers().forEach(paper -> paperFinishList.add(setPaperFinish(userId,paper.getId())));
        return paperFinishList;
    }

    @Override
    public List<PaperFinish> getStudentFinish(Long courseId, Long paperId) {
        List<PaperFinish> paperFinishList = new ArrayList<>();
        Course course = courseService.getCourseInfo(courseId);
        getPaperInfo(paperId,courseId);
        course.getStudents().forEach(student-> paperFinishList.add(setPaperFinish(student.getId(),paperId)));
        return paperFinishList;
    }

    private PaperFinish setPaperFinish(Long userId,Long paperId){
        Paper paper = paperRepository.findById(paperId).orElseThrow(()-> new NotFoundException("No corresponding paper"));
        User user = userRepository.getOne(userId);
        PaperFinish paperFinish = new PaperFinish();
        paperFinish.setPaper(paper);
        paperFinish.setStudent(user);
        int maxTimes = paperAnswerRepository.getMaxTimes(userId,paperId).orElse(0);
        paperFinish.setTimes(maxTimes);
        if(maxTimes==0){
            paperFinish.setState(PaperAnswerState.NOT_START);
        }
        else{
            PaperAnswerPrimaryKey paperAnswerPrimaryKey = new PaperAnswerPrimaryKey(user,paper,maxTimes);
            PaperAnswer paperAnswer = paperAnswerRepository.getOne(paperAnswerPrimaryKey);
            paperFinish.setState(paperAnswer.getState());
        }
        return paperFinish;
    }

    @Override
    public Paper getPaperInfo(Long paperId, Long courseId) {
        Paper paper = paperRepository.findById(paperId)
                .orElseThrow(()->new NotFoundException("No corresponding paper"));
        Course course = courseService.getCourseInfo(courseId);
        if(!paper.getCourse().equals(course)){
            throw new NotMatchException("This course doesn't have this paper");
        }
        return paper;

    }
}
