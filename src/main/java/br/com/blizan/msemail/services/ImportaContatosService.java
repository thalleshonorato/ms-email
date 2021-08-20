package br.com.blizan.msemail.services;

import br.com.blizan.msemail.dtos.ContatoDtoCsv;
import br.com.blizan.msemail.models.ContatoModel;
import br.com.blizan.msemail.models.Tag;
import br.com.blizan.msemail.repositories.ContatoRepository;
import br.com.blizan.msemail.repositories.TagRepository;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ImportaContatosService {

    @Autowired
    ContatoRepository contatoRepository;

    @Autowired
    TagRepository tagRepository;

    public List<ContatoModel> converteContatosCsvEmModel(Reader reader){
        List<ContatoDtoCsv> contatosCsv = new CsvToBeanBuilder(reader)
                .withType(ContatoDtoCsv.class)
                .build()
                .parse();

        List<ContatoModel> contatos = new ArrayList<ContatoModel>();
        for (ContatoDtoCsv contatoCsv: contatosCsv) {
            ContatoModel contatoModel = new ContatoModel();
            BeanUtils.copyProperties(contatoCsv, contatoModel);
            contatos.add(contatoModel);
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

    public void criaContatos(List<ContatoModel> contatos, List<Tag> tags){
        for(ContatoModel contatoModel : contatos){
            ContatoModel contatoModelExist = contatoRepository.findByEmail(contatoModel.getEmail());
            if(contatoModelExist != null){
                contatoModelExist.setTags(Stream.of(contatoModelExist.getTags(), tags).flatMap(List::stream).collect(Collectors.toList()));
                contatoRepository.save(contatoModelExist);
            }else{
                contatoModel.setTags(tags);
                contatoRepository.save(contatoModel);
            }
        }
    }

    public void importaCsv(Reader reader, List<Tag> tags){
        List<Tag> createdTags = criaTags(tags);
        List<ContatoModel> contatos = converteContatosCsvEmModel(reader);
        criaContatos(contatos, createdTags);
    }
}
