package br.com.blizan.msemail.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "TBL_CONTATOS")
public class ContatoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique=true)
    private String email;
    private String identificador;
    private String nome;
    @ManyToMany(mappedBy = "contatos")
    private List<Tag> tags;

}
