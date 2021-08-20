package br.com.blizan.msemail.repositories;

import br.com.blizan.msemail.models.ContatoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContatoRepository extends JpaRepository<ContatoModel, Long> {
    List<ContatoModel> findAllByEmailIn(List<String> emails);
    ContatoModel findByEmail(String email);
}
