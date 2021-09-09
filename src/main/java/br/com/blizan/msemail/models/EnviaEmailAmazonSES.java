package br.com.blizan.msemail.models;

import br.com.blizan.msemail.config.CustomPropertyConfig;
import br.com.blizan.msemail.enums.StatusEmail;
import br.com.blizan.msemail.services.EmailService;
import br.com.blizan.msemail.services.MensagemService;
import com.github.sonus21.rqueue.annotation.RqueueListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class EnviaEmailAmazonSES implements EnviaEmail {

    @Autowired
    EmailService emailService;

    @Autowired
    MensagemService mensagemService;

    @Autowired
    CustomPropertyConfig customPropertyConfig;

    @RqueueListener(value = "email-queue", concurrency = "5-10")
    public void sendEmailQueue(Mensagem mensagem){

        try{
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(customPropertyConfig.mailFrom);
            simpleMailMessage.setTo(mensagem.getEmailTo());
            simpleMailMessage.setSubject(mensagem.getSubject());
            simpleMailMessage.setText(mensagem.getText());
            emailService.sendingEmail(simpleMailMessage);

            mensagem.setStatusEmail(StatusEmail.SENT);
            mensagem.setSendDateEmail(LocalDateTime.now());
        }catch(Exception e) {
            e.printStackTrace();
            mensagem.setStatusEmail(StatusEmail.ERROR);
        }finally {
            mensagemService.save(mensagem);
        }

        log.info("email-queue: {}", mensagem);
    }
}
