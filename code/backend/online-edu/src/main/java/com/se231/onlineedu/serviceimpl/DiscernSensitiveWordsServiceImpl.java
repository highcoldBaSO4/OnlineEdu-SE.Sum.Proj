package com.se231.onlineedu.serviceimpl;

import com.baidu.aip.contentcensor.AipContentCensor;
import com.se231.onlineedu.service.DiscernSensitiveWordsService;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * @author liu
 * @date 2019/07/11
 */
@Service
public class DiscernSensitiveWordsServiceImpl implements DiscernSensitiveWordsService {
    private static final String APP_ID = "16807628";

    private static final String API_KEY ="lTHFlL9sUMVsI2L2YGBTbnhV";

    private static final String SECRET_KEY = "HtStYXFsAYOcLHbIuuGnRIpVl1CsfZyF";

    private static AipContentCensor contentCensorClient;

    private static void getClient() {

        //初始化图片审核客户端
        contentCensorClient = new AipContentCensor(APP_ID, API_KEY, SECRET_KEY);
        contentCensorClient.setConnectionTimeoutInMillis(2000);
        contentCensorClient.setSocketTimeoutInMillis(60000);
    }

    @Override
    public boolean discern(String text)   {
        getClient();
        String resp;
        try {
            //防止出现特殊符号，制造异常
            resp = contentCensorClient.antiSpam(URLDecoder.decode(text, "UTF-8"), null).toString();
        } catch (UnsupportedEncodingException e) {
            return false;
        }
        //标识审核是否通过的结果所在未知
        int len = resp.lastIndexOf("m\":") + 3;
        String spam = resp.substring(len,len + 1);
        System.out.println(spam);
        if(spam.equals("0")) {
            return true;
        }else {
            return false;
        }
    }
}
