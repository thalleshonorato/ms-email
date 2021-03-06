package br.com.blizan.msemail.services;

import br.com.blizan.msemail.models.Contato;
import br.com.blizan.msemail.models.Tag;
import br.com.blizan.msemail.repositories.ContatoRepository;
import br.com.blizan.msemail.repositories.TagRepository;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.junit.Assert.assertEquals;


import java.io.*;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ImportaContatosServiceTest {

    @TestConfiguration
    static class ImportaContatosServiceTestConfiguration{

        @Bean
        public ImportaContatosService importaContatosService(){
            return new ImportaContatosService();
        }
    }

    @Autowired
    private ImportaContatosService importaContatosService;

    @Autowired
    private ContatoRepository contatoRepository;

    @Autowired
    private TagRepository tagRepository;

    @DisplayName("Shoud be able to import new contacts with csv file")
    @Test
    public void importaListaDeContatosEmCsv() throws FileNotFoundException {
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
    }

    @DisplayName("Verifica se j?? existe uma tag cadastrada")
    @Test
    public void verificaSeATagJaFoiCadastrada() throws FileNotFoundException {
        Tag tag = new Tag();
        tag.setTitle("Nova Campanha");
        Tag tag2 = new Tag();
        tag2.setTitle("Campanha de marketing 2");

        importaContatosService.importaCsv(new FileReader(ResourceUtils.getFile("classpath:contatosCsv.csv")), Arrays.asList(tag, tag2));
        List<Tag> createdTags = tagRepository.findAll();
        assertThat(createdTags, hasItems(
                hasProperty("title", is(tag.getTitle())),
                hasProperty("title", is(tag2.getTitle()))
        ));
    }

    @DisplayName("Verifica se j?? existe um usuario cadastrado")
    @Test
    public void verificaSeUsuarioJaFoiCadastrado() throws FileNotFoundException {
        Tag tag = new Tag();
        tag.setTitle("Nova Campanha");
        Tag tag2 = new Tag();
        tag2.setTitle("Campanha de marketing");

        importaContatosService.importaCsv(new FileReader(ResourceUtils.getFile("classpath:contatosCsv.csv")), Arrays.asList(tag, tag2));

        Tag tag3 = new Tag();
        tag3.setTitle("Nova Tag");

        importaContatosService.importaCsv(new FileReader(ResourceUtils.getFile("classpath:contatosCsv2.csv")), Arrays.asList(tag3));

        List<Contato> createdContacts = contatoRepository.findAllByEmailIn(Arrays.asList("thallesrv1@gmail.com"));

        assertEquals(createdContacts.size(), 1);
        assertThat(createdContacts.get(0).getTags(), hasItems(
                hasProperty("title", is("Nova Tag")),
                hasProperty("title", is("Nova Campanha")),
                hasProperty("title", is("Campanha de marketing"))
        ));
    }
}
