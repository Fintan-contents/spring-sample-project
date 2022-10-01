package com.example.common.nablarch.mail;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import nablarch.common.mail.AttachedFile;
import nablarch.common.mail.MailRequester;
import nablarch.common.mail.MailUtil;
import nablarch.common.mail.TemplateMailContext;

@SpringBootTest
public class MailTest {

    @Test
    void mail() {
        TemplateMailContext mailRequest = new TemplateMailContext();
        mailRequest.setFrom("from@tis.co.jp");
        mailRequest.addTo("to@tis.co.jp");
        mailRequest.addCc("cc@tis.co.jp");
        mailRequest.addBcc("bcc@tis.co.jp");
        mailRequest.setTemplateId("example");
        mailRequest.setLang("ja");

        AttachedFile attachedFile = new AttachedFile("text/plain", Path.of("pom.xml").toFile());
        mailRequest.addAttachedFile(attachedFile);

        MailRequester requester = MailUtil.getMailRequester();
        String mailRequestId = requester.requestToSend(mailRequest);

        assertNotNull(mailRequestId);
    }
}
