package br.com.blizan.msemail.models;

import br.com.blizan.msemail.enums.StatusTag;
import lombok.Data;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.List;

@Data
@Entity
@Table(name = "TBL_TAGS")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique=true)
    private String title;
    private StatusTag statusTag;
    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    private List<Contato> contatos;
    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    private List<Mensagem> mensagens;

    public Tag() {
    }
    public Tag(String title) {
        this.title = title;
    }

    @Transactional
    public List<Mensagem> getMensagens() {
        return mensagens;
    }

    @Transactional
    public List<Contato> getContatos() {
        return contatos;
    }
}
