package com.se231.onlineedu.service;

import com.se231.onlineedu.model.Learn;

/**
 * @author yuxuanLiu
 * @date 2019/07/24
 */
public interface LearnService {
    /**
     * get learn info
     * @param userId
     * @param courseId
     * @return
     */
    Learn getLearnInfo(Long userId, Long courseId);

    /**
     * save word cloud url
     * @param courseId
     * @param userId
     * @param url
     * @return
     */
    Learn saveWordCloudUrl(Long userId, Long courseId, String url);
}
