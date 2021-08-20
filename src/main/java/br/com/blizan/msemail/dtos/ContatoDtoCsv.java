package br.com.blizan.msemail.dtos;

import com.opencsv.bean.CsvBindByPosition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class ContatoDtoCsv {

    private String email;
    private String identificador;
    private String nome;
}
