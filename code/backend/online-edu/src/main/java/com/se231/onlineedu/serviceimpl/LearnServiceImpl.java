package com.se231.onlineedu.serviceimpl;

import com.se231.onlineedu.exception.NotFoundException;
import com.se231.onlineedu.model.Learn;
import com.se231.onlineedu.repository.LearnRepository;
import com.se231.onlineedu.service.LearnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yuxuanLiu
 * @date 2019/07/24
 */
@Service
public class LearnServiceImpl implements LearnService {
    @Autowired
    private LearnRepository learnRepository;
    @Override
    public Learn getLearnInfo(Long userId, Long courseId) {
        return learnRepository.findByLearnPrimaryKey_Student_IdAndLearnPrimaryKey_Course_Id(userId, courseId).orElseThrow(()->new NotFoundException("该学生未学习课程不存在。"));
    }

    @Override
    public Learn saveWordCloudUrl(Long userId, Long courseId, String url) {
        Learn learn = getLearnInfo(userId, courseId);
        learn.setWordCloudUrl(url);
        return learnRepository.save(learn);
    }

}
