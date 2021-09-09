package br.com.blizan.msemail.services;

import br.com.blizan.msemail.models.Mensagem;
import br.com.blizan.msemail.repositories.MensagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private MailSender mailSender;

    @Autowired
    MensagemRepository mensagemRepository;

    public void sendingEmail(SimpleMailMessage simpleMailMessage){
        this.mailSender.send(simpleMailMessage);
    }
}
