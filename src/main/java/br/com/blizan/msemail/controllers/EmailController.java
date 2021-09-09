package br.com.blizan.msemail.controllers;

import br.com.blizan.msemail.dtos.EmailDto;
import br.com.blizan.msemail.models.Contato;
import br.com.blizan.msemail.models.Mensagem;
import br.com.blizan.msemail.models.Tag;
import br.com.blizan.msemail.repositories.TagRepository;
import br.com.blizan.msemail.services.ImportaContatosService;
import br.com.blizan.msemail.services.MensagemService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.junit.Assert.assertEquals;

@RestController
public class EmailController {

    @Autowired
    MensagemService mensagemService;

    @Autowired
    ImportaContatosService importaContatosService;

    @Autowired
    TagRepository tagRepository;

    @PostMapping("/send-email")
    @ResponseBody
    public ResponseEntity<String> sendingEmail(@RequestBody @Validated EmailDto emailDto) throws FileNotFoundException {
        Tag tag = new Tag();
        tag.setTitle("Nova Campanha");
        Tag tag2 = new Tag();
        tag2.setTitle("Campanha de marketing");

        importaContatosService.importaCsv(new FileReader(ResourceUtils.getFile("classpath:contatosCsv.csv")), Arrays.asList(tag, tag2));

        List<Tag> createdTags = tagRepository.findAll();
        Mensagem mensagem = new Mensagem(
                emailDto.getEmailFrom(),
                emailDto.getEmailFrom(),
                emailDto.getSubject(),
                emailDto.getText()
        );

        mensagemService.run(mensagem, createdTags);


        return ResponseEntity.status(HttpStatus.OK).body("Enviado");
    }
}
