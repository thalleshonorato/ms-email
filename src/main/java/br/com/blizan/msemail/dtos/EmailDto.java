package br.com.blizan.msemail.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@AllArgsConstructor
public class EmailDto {

    @NotBlank
    @Email
    private String emailFrom;
    @NotBlank
    private String subject;
    @NotBlank
    private String text;

    private String[] tags;

}
