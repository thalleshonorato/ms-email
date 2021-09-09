package br.com.blizan.msemail.services;

import br.com.blizan.msemail.models.Contato;
import br.com.blizan.msemail.models.Mensagem;
import br.com.blizan.msemail.models.Tag;
import br.com.blizan.msemail.repositories.ContatoRepository;
import br.com.blizan.msemail.repositories.MensagemRepository;
import br.com.blizan.msemail.repositories.TagRepository;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration
public class MensagemServiceTest {

    @TestConfiguration
    static class EnviaEmailServiceTestConfiguration{

        @Bean
        public MensagemService enviaEmailService(){
            return new MensagemService();
        }

        @Bean
        public ImportaContatosService importaContatosService(){
            return new ImportaContatosService();
        }
    }

    @Autowired
    private MensagemService mensagemService;

    @Autowired
    private MensagemRepository mensagemRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private ContatoRepository contatoRepository;

    @Autowired
    private ImportaContatosService importaContatosService;

    @DisplayName("Capaz de cadastrar a mensagem a ser enviada")
    @Test
    public void cadastraMensagem() throws FileNotFoundException {
        Tag tag = new Tag();
        tag.setTitle("Nova Campanha");
        Tag tag2 = new Tag();
        tag2.setTitle("Campanha de marketing");

        importaContatosService.importaCsv(new FileReader(ResourceUtils.getFile("classpath:contatosCsv.csv")), Arrays.asList(tag, tag2));

        List<Tag> createdTags = tagRepository.findAll();
        assertThat(createdTags, hasItems(
                hasProperty("title", is(tag.getTitle())),
                hasProperty("title", is(tag2.getTitle()))
        ));

        List<Contato> createdContacts = contatoRepository.findAll();
        assertThat(createdContacts, hasItems(
                hasProperty("email", is("thallesrv1@gmail.com")),
                hasProperty("email", is("thallesrv2@gmail.com")),
                hasProperty("email", is("thallesrv3@gmail.com"))
        ));

        Mensagem mensagem = new Mensagem("thallesrv1@gmail.com", "thallesrv1@gmail.com", "Teste email", "testando");

        mensagemService.run(mensagem, createdTags);

        List<Mensagem> mensagens = mensagemRepository.findAll();

        assertEquals(mensagens.size(), contatoRepository.findAll().size());

        assertThat(mensagens, hasItems(
                hasProperty("emailFrom", is("thallesrv1@gmail.com")),
                hasProperty("subject", is("Teste email"))
        ));

        assertThat(mensagens.get(0).getTags(), hasItems(
                hasProperty("title", is("Nova Campanha")),
                hasProperty("title", is("Campanha de marketing"))
        ));
    }
}
