package com.se231.onlineedu;

import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.bg.PixelBoundryBackground;
import com.kennycason.kumo.font.scale.LinearFontScalar;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import com.kennycason.kumo.nlp.tokenizers.ChineseWordTokenizer;
import com.kennycason.kumo.palette.ColorPalette;
import org.junit.Test;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


/**
 * @author yuxuanLiu
 * @date 2019/07/24
 */
public class WordCloudTest {
    public List<String> getStopWords() throws IOException {
        List<String> stopWords = new ArrayList<>();
        File inFile = new File("src/test/resources/stopwords.txt");
        BufferedReader bufferedReader = new BufferedReader(new FileReader(inFile));
        String word;
        while ((word = bufferedReader.readLine()) != null) {
            stopWords.add(word);
        }
        bufferedReader.close();
        return stopWords;
    }

    @Test
    public void haha() throws IOException {
        final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.setWordFrequenciesToReturn(600);
        frequencyAnalyzer.setMinWordLength(2);
        frequencyAnalyzer.setStopWords(getStopWords());
        frequencyAnalyzer.setWordTokenizer(new ChineseWordTokenizer());

        String string1 = "Docker自2013年以来非常火热，无论是从 github 上的代码活跃度，还是Redhat在RHEL6.5中集成对Docker的支持, 就连 Google 的 Compute Engine 也支持 docker 在其之上运行。\n" +
                "一款开源软件能否在商业上成功，很大程度上依赖三件事 - 成功的";

        String string2 = "    环境管理复杂 - 从各种OS到各种中间件到各种app, 一款产品能够成功作为开发者需要关心的东西太多，且难于管理，这个问题几乎在所有现代IT相关行业都需要面对。\n" +
                "    云计算时代的到来 - AWS的成功, 引导开发者将应用转移到 cloud 上, 解决了硬件管理的问题，然而中间件相关的问题依然存在 (所以openstack HEAT和 AWS cloudformation 都着力解决这个问题)。开发者思路变化提供了可能性。\n" +
                "    虚拟化手段的变化 - cloud 时代采用标配硬件来降低成";

        List<String> strings = List.of(string1, string2);

        final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(strings);
        final Dimension dimension = new Dimension(500, 312);
        final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
        wordCloud.setPadding(2);
        wordCloud.setBackground(new PixelBoundryBackground("src/test/resources/whale_small.png"));
        wordCloud.setColorPalette(new ColorPalette(new Color(0x4055F1), new Color(0x408DF1), new Color(0x40AAF1), new Color(0x40C5F1), new Color(0x40D3F1), new Color(0xFFFFFF)));
        wordCloud.setFontScalar(new LinearFontScalar(10, 40));
        wordCloud.build(wordFrequencies);
        wordCloud.writeToFile("src/test/resources/whale_wordcloud_small.png");
    }
}
