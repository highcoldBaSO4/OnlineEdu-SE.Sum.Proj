package com.se231.onlineedu.Repository;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.ArrayList;
import java.util.List;
import com.se231.onlineedu.BaseTest;
import com.se231.onlineedu.model.*;
import com.se231.onlineedu.repository.AnswerRepository;
import com.se231.onlineedu.repository.PaperAnswerRepository;
import com.se231.onlineedu.repository.PaperRepository;
import com.se231.onlineedu.repository.QuestionRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Zhe Li
 * @date 2019/07/22
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class PaperAnswerRepositoryTest extends BaseTest {
    @Autowired
    PaperAnswerRepository paperAnswerRepository;

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    PaperRepository paperRepository;

    @Autowired
    QuestionRepository questionRepository;

    private void answer(String answer){
        int times = paperAnswerRepository.getMaxTimes(user.getId(),paper.getId()).orElse(0);
        PaperAnswer paperAnswer1 = new PaperAnswer();
        paperAnswer1.setState(PaperAnswerState.FINISHED);
        paperAnswer1.setPaperAnswerPrimaryKey(new PaperAnswerPrimaryKey(user,paper,times+1));
        paperAnswer1 = paperAnswerRepository.save(paperAnswer1);
        List<Answer> answerList1 = new ArrayList<>();
        Answer answer1 = new Answer();
        answer1.setAnswer(answer);
        answer1.setAnswerPrimaryKey(new AnswerPrimaryKey(paperAnswer1,question));
        answerList1.add(answerRepository.save(answer1));
        paperAnswer1.setAnswers(answerList1);
        paperAnswerRepository.save(paperAnswer1);
    }

    @Test
    public void getMaxTimesTest(){
        int times1 = paperAnswerRepository.getMaxTimes(user.getId(),paper.getId()).orElse(0);
        assertThat(times1).isEqualTo(0);
        answer("A");
        int times2 = paperAnswerRepository.getMaxTimes(user.getId(),paper.getId()).orElse(0);
        assertThat(times2).isEqualTo(1);
    }

    @Test
    public void getAnswerList(){
        answer("A");
        answer("B");
        List<PaperAnswer> paperAnswerList = paperAnswerRepository.getPaperAnswers(paper.getId());
        assertThat(paperAnswerList.size()).isEqualTo(2);
    }

    @Test
    public void getPersonalTest(){
        answer("A");
        answer("B");
        List<PaperAnswer> paperAnswerList = paperAnswerRepository.getPersonalPaperAnswer(paper.getId(),user.getId());
        assertThat(paperAnswerList.get(1).getAnswers().get(0).getAnswer()).isEqualTo("B");
    }

    @Test
    public void getStudentAnswerTest(){
        answer("A");
        answer("B");
        List<PaperAnswer> paperAnswerList = paperAnswerRepository
                .findAllByPaperAnswerPrimaryKey_PaperAndAndPaperAnswerPrimaryKey_User(paper,user);
        assertThat(paperAnswerList.get(1).getAnswers().get(0).getAnswer()).isEqualTo("B");
    }



}
