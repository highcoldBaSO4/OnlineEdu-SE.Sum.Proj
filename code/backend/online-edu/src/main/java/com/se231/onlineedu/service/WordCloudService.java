package com.se231.onlineedu.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * @author yuxuanLiu
 * @date 2019/07/24
 */
public interface WordCloudService {
    /**
     * generate word cloud
     * @param strings
     * @return
     * @throws IOException
     */
    String generateWordCloud(List<String> strings) throws IOException;
}
