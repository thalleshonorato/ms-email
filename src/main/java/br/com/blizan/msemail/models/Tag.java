package br.com.blizan.msemail.models;

import br.com.blizan.msemail.enums.StatusTag;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "tags")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique=true)
    private String title;
    private StatusTag statusTag;
    @ManyToMany
    private List<ContatoModel> contatos;
}
