package br.com.blizan.msemail.services;

import br.com.blizan.msemail.models.Contato;
import br.com.blizan.msemail.models.Mensagem;
import br.com.blizan.msemail.models.Tag;
import br.com.blizan.msemail.repositories.ContatoRepository;
import br.com.blizan.msemail.repositories.MensagemRepository;
import br.com.blizan.msemail.repositories.TagRepository;
import com.github.sonus21.rqueue.core.RqueueMessageEnqueuer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@Transactional
@Service
public class MensagemService {

    @Autowired
    TagRepository tagRepository;

    @Autowired
    ContatoRepository contatoRepository;

    @Autowired
    MensagemRepository mensagemRepository;

    @Autowired
    RqueueMessageEnqueuer rqueueMessageEnqueuer;

    public void save(Mensagem mensagem){
        mensagemRepository.save(mensagem);
    }

    public void run(Mensagem mensagem, List<Tag> tags){
        List<Contato> contatos = contatoRepository.findDistinctContatoAllByTagsIn(tags);

        for (Contato contato : contatos) {
            Mensagem newMensagem = new Mensagem();
            BeanUtils.copyProperties(mensagem, newMensagem);
            newMensagem.setEmailTo(contato.getEmail());
            newMensagem.setTags(tags);
            newMensagem = mensagemRepository.save(newMensagem);
            rqueueMessageEnqueuer.enqueue("email-queue", newMensagem);
        }
    }
}
