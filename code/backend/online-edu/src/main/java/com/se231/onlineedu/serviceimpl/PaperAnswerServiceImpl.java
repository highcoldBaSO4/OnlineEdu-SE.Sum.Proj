package com.se231.onlineedu.serviceimpl;

import java.io.IOException;
import java.util.*;
import com.se231.onlineedu.exception.*;
import com.se231.onlineedu.message.request.MarkForm;
import com.se231.onlineedu.message.request.QuestionAnswer;
import com.se231.onlineedu.message.request.SubmitAnswerForm;
import com.se231.onlineedu.model.*;
import com.se231.onlineedu.repository.*;
import com.se231.onlineedu.service.CourseService;
import com.se231.onlineedu.service.PaperAnswerService;
import com.se231.onlineedu.service.PaperService;
import com.se231.onlineedu.service.UserService;
import com.se231.onlineedu.util.SaveFileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Zhe Li
 * @date 2019/07/10
 */
@Service
public class PaperAnswerServiceImpl implements PaperAnswerService {

    @Autowired
    UserService userService;

    @Autowired
    PaperRepository paperRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    PaperAnswerRepository paperAnswerRepository;

    @Autowired
    PaperService paperService;

    @Autowired
    PaperWithQuestionsRepository paperWithQuestionsRepository;

    private static final int MAX_TIMES = 3;

    private static final int LIMIT = 5120000;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public PaperAnswer submitAnswer(Long userId, Long courseId, Long paperId, SubmitAnswerForm form) {
        PaperAnswer paperAnswer = getPaperAnswer(userId, courseId, paperId);
        Paper paper = paperRepository.getOne(paperId);
        timeTest(paper);
        List<Answer> answerList = new ArrayList<>();
        try{
            for (QuestionAnswer questionAnswer :form.getAnswerList()){
                Question question = questionRepository.findById(questionAnswer.getQuestionId())
                        .orElseThrow(() -> new AnswerException("Question Not Found"));
                if(!paper.getQuestionList().contains(question)){
                    throw new AnswerException("This paper doesn't contain this question");
                }
                AnswerPrimaryKey answerPrimaryKey = new AnswerPrimaryKey(paperAnswer,question);
                Answer answer = new Answer(answerPrimaryKey,questionAnswer.getAnswer(),0);
                answerList.add(answer);
            }
        } catch (AnswerException e){
            if(paperAnswer.getState() == null) {
                paperAnswerRepository.delete(paperAnswer);
            }
            throw e;
        }
        if(paperAnswer.getState()!=null&&paperAnswer.getState().equals(PaperAnswerState.TEMP_SAVE)) {
            paperAnswer.getAnswers().addAll(answerList);
        }else {
            paperAnswer.setAnswers(answerList);
        }
        paperAnswer.setState(PaperAnswerState.valueOf(form.getState()));
        return paperAnswerRepository.save(paperAnswer);
    }

    @Override
    public void autoMark(Long paperId) {
        Paper paper = paperRepository.getOne(paperId);
        Map<Long,Double> scoreTable= new HashMap<>(paper.getQuestions().size());
        boolean hasSubjective=false;
        for (PaperWithQuestions questions:paper.getQuestions()) {
            if(questions.getPaperWithQuestionsPrimaryKey().getQuestion().getQuestionType().equals(QuestionType.SUBJECTIVE)){
                hasSubjective=true;
            }
            scoreTable.put(questions.getPaperWithQuestionsPrimaryKey().getQuestion().getId(),questions.getScore());
        }
        final boolean sub = hasSubjective;
        List<PaperAnswer> paperAnswerList = paperAnswerRepository.getPaperAnswers(paperId);
        paperAnswerList.forEach(paperAnswer ->{
            double totalScore=0D;
            for(Answer answer:paperAnswer.getAnswers()){
                //若题目为主观题，默认不做批改，直接不打分留题。若为客观题则直接检测与答案是否匹配。
                Question question = answer.getAnswerPrimaryKey().getQuestion();
                double score= (!question.getQuestionType().equals(QuestionType.SUBJECTIVE)&&
                    answer.getAnswer().equals(question.getAnswer()))?scoreTable.get(question.getId()):0;
                answer.setGrade(score);
                answerRepository.save(answer);
                totalScore+=score;
            }
            PaperAnswerState state = sub ? PaperAnswerState.NOT_MARKED : PaperAnswerState.MARKED;
            paperAnswer.setGrade(totalScore);
            paperAnswer.setState(state);
            paperAnswerRepository.save(paperAnswer);
        });
    }

    @Override
    public List<PaperAnswer> getPersonalPaperAnswer(Long paperId, Long userId) {
        return paperAnswerRepository.getPersonalPaperAnswer(paperId,userId);
    }

    @Override
    public PaperAnswer markStudentPaper(Long courseId, Long studentId, Long paperId, Integer times, Set<MarkForm> markForms) {
        User student = userService.getUserInfo(studentId);
        Paper paper = paperService.getPaperInfo(paperId,courseId);
        PaperAnswerPrimaryKey paperAnswerPrimaryKey= new PaperAnswerPrimaryKey(student,paper,times);
        PaperAnswer paperAnswer = paperAnswerRepository.findById(paperAnswerPrimaryKey)
                .orElseThrow(()->new NotFoundException("No corresponding paper answer"));
        double grade = paperAnswer.getGrade();
        for(MarkForm markForm : markForms){
            Question question = questionRepository.findById(markForm.getQuestionId())
                    .orElseThrow(()-> new NotFoundException("No corresponding question"));
            AnswerPrimaryKey answerPrimaryKey = new AnswerPrimaryKey(paperAnswer,question);
            Answer answer = answerRepository.findById(answerPrimaryKey)
                    .orElseThrow(()->new NotFoundException("No answer for this question"));
            PaperWithQuestions paperWithQuestions = paperWithQuestionsRepository.getOne(new PaperWithQuestionsPrimaryKey(paper,question));
            if(markForm.getScore()>paperWithQuestions.getScore()){
                throw new AnswerException("该题满分为："+paperWithQuestions.getScore());
            }
            answer.setGrade(markForm.getScore());
            answer.setComment(markForm.getComment());
            grade+=answer.getGrade();
        }
        paperAnswer.setGrade(grade);
        paperAnswer.setState(PaperAnswerState.MARKED);
        return paperAnswerRepository.save(paperAnswer);
    }

    @Override
    public PaperAnswer submitSubjectiveQuestion(Long courseId, Long userId, Long paperId, Long questionId,
                                                String answerText, MultipartFile[] images,MultipartFile[] file,
                                                PaperAnswerState state) {
        //File check
        /**
        empty space to fill
         */
        PaperAnswer paperAnswer = getPaperAnswer(userId, courseId, paperId);
        Paper paper = paperRepository.getOne(paperId);
        timeTest(paper);
        paperAnswer.setState(state);
        Question question = questionRepository.findById(questionId)
                .orElseThrow(()->new NotFoundException("No corresponding question"));
        if(!paper.getQuestionList().contains(question)){
            throw new NotMatchException("This paper doesn't have this question.");
        }

        AnswerPrimaryKey answerPrimaryKey = new AnswerPrimaryKey(paperAnswer,question);
        Answer answer = new Answer(answerPrimaryKey,answerText,0);
        try {
            if(file.length==1) {
                String suffix = file[0].getOriginalFilename().substring(file[0].getOriginalFilename().lastIndexOf("."));
                answer.setResource(SaveFileUtil.saveFile(file[0], suffix));
            }
            answer.setImageUrls(SaveFileUtil.saveImages(images, LIMIT));

        } catch (IOException e){
            e.printStackTrace();
        }
        paperAnswer.getAnswers().add(answer);
        return paperAnswerRepository.save(paperAnswer);
    }

    private PaperAnswer getPaperAnswer(Long userId,Long courseId,Long paperId){
        //initialize( found corresponding user and paper)
        User user = userService.getUserInfo(userId);
        Paper paper = paperService.getPaperInfo(paperId,courseId);
        //get times the user has answered
        int times=paperAnswerRepository.getMaxTimes(userId,paperId).orElse(0);
        if(times>0) {
            PaperAnswerPrimaryKey lastAnswerPrimaryKey = new PaperAnswerPrimaryKey(user, paper, times);
            PaperAnswer lastAnswer = paperAnswerRepository.getOne(lastAnswerPrimaryKey);
            if (lastAnswer.getState().equals(PaperAnswerState.NOT_FINISH)){
                return lastAnswer;
            }
            if(lastAnswer.getState().equals(PaperAnswerState.TEMP_SAVE)){
                return lastAnswer;
            }
        }if(times==MAX_TIMES){
            throw new ValidationException("You Have Answered Three Times");
        }
        PaperAnswerPrimaryKey paperAnswerPrimaryKey= new PaperAnswerPrimaryKey(user,paper,times+1);
        PaperAnswer paperAnswer = new PaperAnswer(paperAnswerPrimaryKey);
        return paperAnswerRepository.save(paperAnswer);
    }

    @Override
    public PaperAnswer changePaperAnswerState(Long courseId, Long userId, Long paperId, PaperAnswerState state) {
        PaperAnswer paperAnswer = getPaperAnswer(userId, courseId, paperId);
        paperAnswer.setState(state);
        return paperAnswerRepository.save(paperAnswer);
    }

    @Override
    public List<PaperAnswer> getStudentAnswer(Long courseId, Long paperId, Long studentId) {
        User user = userService.getUserInfo(studentId);
        Paper paper = paperService.getPaperInfo(paperId,courseId);
        return paperAnswerRepository.findAllByPaperAnswerPrimaryKey_PaperAndAndPaperAnswerPrimaryKey_User(paper,user);
    }

    private void timeTest(Paper paper){
        Date currentTime = new Date();
        if(currentTime.before(paper.getStart())){
            throw new BeforeStartException("作业尚未开始");
        }
        if(currentTime.after(paper.getEnd())){
            throw new AfterEndException("作业已经结束");
        }
    }
}
