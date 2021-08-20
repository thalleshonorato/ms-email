package br.com.blizan.msemail.controllers;

import br.com.blizan.msemail.dtos.EmailDto;
import br.com.blizan.msemail.models.EmailModel;
import br.com.blizan.msemail.services.EmailService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Email;

@RestController
public class EmailController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/get-email")
    @ResponseBody
    public String sendingEmail(){
        return "OK";
    }

    @PostMapping("/send-email")
    @ResponseBody
    public ResponseEntity<EmailModel> sendingEmail(@RequestBody @Validated EmailDto emailDto){
        EmailModel emailModel = new EmailModel();
        BeanUtils.copyProperties(emailDto, emailModel);

        return ResponseEntity.status(HttpStatus.OK).body(emailModel);
    }
}
