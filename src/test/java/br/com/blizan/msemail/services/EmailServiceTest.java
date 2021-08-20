package br.com.blizan.msemail.services;

import br.com.blizan.msemail.models.EmailModel;
import br.com.blizan.msemail.services.EmailService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

@RunWith(SpringRunner.class)
public class EmailServiceTest {

    @Autowired
    EmailService emailService;

    @Test
    public void sendingEmailTest(){
        EmailModel newEmailModel = new EmailModel("Thalles",
                "thallesrv1@gmail.com",
                "fulano@gmail.com",
                "Teste email",
                "corpo email",
                LocalDateTime.now());

        EmailModel emailModel = emailService.sendingEmail(newEmailModel);

        Assertions.assertEquals(emailModel, newEmailModel);
    }
}
