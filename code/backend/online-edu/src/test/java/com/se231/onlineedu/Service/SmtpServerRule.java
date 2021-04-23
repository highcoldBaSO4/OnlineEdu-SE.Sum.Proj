package com.se231.onlineedu.Service;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import org.junit.rules.ExternalResource;

import javax.mail.internet.MimeMessage;

/**
 * @author yuxuanLiu
 * @date 2019/07/22
 */
public class SmtpServerRule extends ExternalResource {
    private GreenMail smtpServer;

    private int port;

    public SmtpServerRule(int port) {
        this.port = port;
    }

    @Override
    protected void before() throws Throwable {
        super.before();
        smtpServer = new GreenMail(new ServerSetup(port, null, "smtp"));
        smtpServer.start();
    }

    public MimeMessage[] getMessages() {
        return smtpServer.getReceivedMessages();
    }

    @Override
    protected void after() {
        super.after();
        smtpServer.stop();
    }
}
