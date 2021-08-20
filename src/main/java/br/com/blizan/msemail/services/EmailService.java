package br.com.blizan.msemail.services;

import br.com.blizan.msemail.models.EmailModel;
import br.com.blizan.msemail.repositories.EmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    EmailRepository emailRepository;

    public EmailModel sendingEmail(EmailModel emailModel){
        return emailModel;
    }
}
