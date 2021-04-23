package com.se231.onlineedu.serviceimpl;

import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.bg.PixelBoundryBackground;
import com.kennycason.kumo.font.scale.LinearFontScalar;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import com.kennycason.kumo.nlp.tokenizers.ChineseWordTokenizer;
import com.kennycason.kumo.palette.ColorPalette;
import com.se231.onlineedu.service.WordCloudService;
import com.se231.onlineedu.util.SaveFileUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author yuxuanLiu
 * @date 2019/07/24
 */
@Service
public class WordCloudServiceImpl implements WordCloudService {

    public List<String> getStopWords() throws IOException {
        List<String> stopWords = new ArrayList<>();
        File inFile = ResourceUtils.getFile("classpath:stopwords.txt");
        BufferedReader bufferedReader = new BufferedReader(new FileReader(inFile));
        String word;
        while ((word = bufferedReader.readLine()) != null) {
            stopWords.add(word);
        }
        bufferedReader.close();
        return stopWords;
    }

    @Override
    public String generateWordCloud(List<String> strings) throws IOException {
        final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.setWordFrequenciesToReturn(600);
        frequencyAnalyzer.setMinWordLength(2);
        frequencyAnalyzer.setStopWords(getStopWords());
        frequencyAnalyzer.setWordTokenizer(new ChineseWordTokenizer());

        final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(strings);
        final Dimension dimension = new Dimension(500, 312);
        final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
        wordCloud.setPadding(2);
        File inFile = ResourceUtils.getFile("classpath:whale_small.png");
        wordCloud.setBackground(new PixelBoundryBackground(inFile));
        wordCloud.setColorPalette(new ColorPalette(new Color(0x4055F1), new Color(0x408DF1), new Color(0x40AAF1), new Color(0x40C5F1), new Color(0x40D3F1), new Color(0xFFFFFF)));
        wordCloud.setFontScalar(new LinearFontScalar(10, 40));
        wordCloud.build(wordFrequencies);
        UUID uuid = UUID.randomUUID();
        File file = new File("/home/ubuntu/"+uuid+".png");
        file.createNewFile();
        wordCloud.writeToFile("/home/ubuntu/"+uuid+".png");
        FileInputStream fileInputStream = new FileInputStream("/home/ubuntu/"+uuid+".png");
        return SaveFileUtil.saveFile(fileInputStream, ".png");
    }
}
