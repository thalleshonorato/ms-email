package br.com.blizan.msemail.repositories;

import br.com.blizan.msemail.models.Contato;
import br.com.blizan.msemail.models.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContatoRepository extends JpaRepository<Contato, Long> {
    List<Contato> findAllByEmailIn(List<String> emails);
    List<Contato> findDistinctContatoAllByTagsIn(List<Tag> tags);
    Contato findByEmail(String email);
}
