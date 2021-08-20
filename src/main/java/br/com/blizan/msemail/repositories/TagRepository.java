package br.com.blizan.msemail.repositories;

import br.com.blizan.msemail.models.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    List<Tag> findAllByTitleIn(List<String> title);

    Optional<Tag> findByTitle(String title);
}
