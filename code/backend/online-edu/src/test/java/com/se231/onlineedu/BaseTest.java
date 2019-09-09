package com.se231.onlineedu;

import java.util.*;
import com.se231.onlineedu.model.*;
import com.se231.onlineedu.repository.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Zhe Li
 * @date 2019/07/22
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class BaseTest {

    protected User user;

    protected CoursePrototype coursePrototype;

    protected Course course;

    protected Paper paper;

    protected Question question;

    protected Section section;

    protected SectionBranches sectionBranches;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CoursePrototypeRepository coursePrototypeRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    PaperRepository paperRepository;

    @Autowired
    PaperWithQuestionsRepository paperWithQuestionsRepository;

    @Autowired
    SectionRepository sectionRepository;

    @Autowired
    SectionBranchRepository sectionBranchRepository;

    @Before
    public void setup(){
        coursePrototype = createCoursePrototype("prototype 1",CoursePrototypeState.USING);

        user = createUser("admin1","admin@1.com",13000000000L);

        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        start.add(Calendar.DATE,5);
        end.add(Calendar.DATE,20);

        course=createCourse("course 1",start.getTime(),end.getTime(),CourseState.READY_TO_START,coursePrototype);

        question=createQuestion("where is JBoss?\0\rplane\0\rschool\0\rParis\0\rBar",
                QuestionType.SINGLE_ANSWER,"D",coursePrototype);

        start.add(Calendar.DATE,1);
        end.add(Calendar.DATE,2);
        Map<Long,Integer> questions = new HashMap<>(10);
        questions.put(question.getId(),5);
        paper = createPaper("paper1",course,start.getTime(),end.getTime(),questions);

        section = createSection("sec_1",course,1,1);

        sectionBranches = createSectionBranches(1,"branch_1",1,section);
    }

    protected User createUser(String username,String email,Long tel){
        User user1 = new User();
        user1.setEmail(email);
        user1.setUsername(username);
        user1.setTel(tel);
        user1.setPassword("password");
        return userRepository.save(user1);
    }

    protected CoursePrototype createCoursePrototype(String title,CoursePrototypeState coursePrototypeState){
        CoursePrototype coursePrototype1 = new CoursePrototype();
        coursePrototype1.setTitle(title);
        coursePrototype1.setState(coursePrototypeState);
        return coursePrototypeRepository.save(coursePrototype1);
    }

    protected Course createCourse(String title ,Date start ,Date end ,CourseState courseState ,CoursePrototype coursePrototype){
        Course course1 = new Course();
        course1.setCourseTitle(title);
        course1.setStartDate(start);
        course1.setEndDate(end);
        course1.setState(CourseState.APPLYING);
        course1.setCoursePrototype(coursePrototype);
        return courseRepository.save(course1);
    }

    protected Question createQuestion(String content,QuestionType questionType,String answer,CoursePrototype coursePrototype){
        Question question1 = new Question();
        question1.setQuestion(content);
        question1.setQuestionType(QuestionType.SINGLE_ANSWER);
        question1.setCoursePrototype(coursePrototype);
        question1.setAnswer(answer);
        return questionRepository.save(question1);
    }

    protected Paper createPaper(String title, Course course, Date start, Date end, Map<Long, Integer> scores){
        Paper paper1 = new Paper();
        paper1.setTitle(title);
        paper1.setCourse(course);
        paper1.setStart(start);
        paper1.setEnd(end);
        paper1 = paperRepository.save(paper1);
        List<PaperWithQuestions> questionsList1 = new ArrayList<>();
        int cnt = 0;
        for(Map.Entry<Long,Integer> entry : scores.entrySet()){
            PaperWithQuestions paperWithQuestions1 = new PaperWithQuestions();
            paperWithQuestions1.setScore(entry.getValue());
            paperWithQuestions1.setQuestionNumber(++cnt);
            Question question1 = questionRepository.findById(entry.getKey()).orElseThrow();
            paperWithQuestions1.setPaperAnswerPrimaryKey(new PaperWithQuestionsPrimaryKey(paper1,question1));
            questionsList1.add(paperWithQuestionsRepository.save(paperWithQuestions1));
        }
        paper1.setQuestions(questionsList1);
        return paperRepository.save(paper1);
    }

    protected Section createSection(String title,Course course,int secNo,int secId){
        Section section1 = new Section();
        section1.setSecNo(secNo);
        section1.setTitle(title);
        section1.setSectionPrimaryKey(new SectionPrimaryKey(course,secId));
        return sectionRepository.save(section1);
    }

    protected SectionBranches createSectionBranches(int branchNo,String title,int branchId,Section section){
        SectionBranches sectionBranches1 = new SectionBranches();
        sectionBranches1.setBranchNo(branchNo);
        sectionBranches1.setTitle(title);
        sectionBranches1.setSectionBranchesPrimaryKey(new SectionBranchesPrimaryKey(section,branchId));
        return sectionBranchRepository.save(sectionBranches1);
    }

    @Test
    public void init(){
        System.out.println("1");
    }
}
