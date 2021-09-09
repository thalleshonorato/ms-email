package br.com.blizan.msemail.services;

import br.com.blizan.msemail.dtos.ContatoDtoCsv;
import br.com.blizan.msemail.models.Contato;
import br.com.blizan.msemail.models.Tag;
import br.com.blizan.msemail.repositories.ContatoRepository;
import br.com.blizan.msemail.repositories.TagRepository;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ImportaContatosService {

    @Autowired
    ContatoRepository contatoRepository;

    @Autowired
    TagRepository tagRepository;

    public List<Contato> converteContatosCsvEmModel(Reader reader){
        List<ContatoDtoCsv> contatosCsv = new CsvToBeanBuilder(reader)
                .withType(ContatoDtoCsv.class)
                .build()
                .parse();

        List<Contato> contatos = new ArrayList<Contato>();
        for (ContatoDtoCsv contatoCsv: contatosCsv) {
            Contato contato = new Contato();
            BeanUtils.copyProperties(contatoCsv, contato);
            contatos.add(contato);
        }

        return contatos;
    }

    public List<Tag> criaTags(List<Tag> tags){
        List<Tag> createdTags = new ArrayList<>();
        for (Tag tag : tags) {
            if(tagRepository.findByTitle(tag.getTitle()).isPresent()){
                createdTags.add(tagRepository.findByTitle(tag.getTitle()).get());
            }else{
                Tag newTag = tagRepository.save(tag);
                createdTags.add(newTag);
            }
        }

        return createdTags;
    }

    public void criaContatos(List<Contato> contatos, List<Tag> tags){
        for(Contato contato : contatos){
            Contato contatoExist = contatoRepository.findByEmail(contato.getEmail());
            if(contatoExist != null){
                contatoExist.setTags(Stream.of(contatoExist.getTags(), tags).flatMap(List::stream).collect(Collectors.toList()));
                contatoRepository.save(contatoExist);
            }else{
                contato.setTags(tags);
                contatoRepository.save(contato);
            }
        }
    }

    public void importaCsv(Reader reader, List<Tag> tags){
        List<Tag> tagsCriadas = criaTags(tags);
        List<Contato> contatos = converteContatosCsvEmModel(reader);
        criaContatos(contatos, tagsCriadas);
    }
}
