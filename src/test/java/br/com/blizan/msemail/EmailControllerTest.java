package br.com.blizan.msemail;


import br.com.blizan.msemail.dtos.EmailDto;
import br.com.blizan.msemail.models.EmailModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class EmailControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getEmailTest() throws Exception {
        mockMvc.perform(get("/get-email"))
                .andExpect(status().isOk());
    }

    @Test
    public void sendEmailTest() throws Exception {
        EmailDto emailDto = new EmailDto("Thalles", "thallesrv1@gmail.com", "fulano@gmail.com", "Teste email", "corpo email");

        mockMvc.perform(post("/send-email")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(emailDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void naoDeveSerPermitidoEnvioDeEmailComEmailInvalido() throws Exception {
        EmailDto emailDto = new EmailDto("Thalles", "123546", "fulano@gmail.com", "Teste email", "corpo email");

        mockMvc.perform(post("/send-email")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(emailDto)))
                .andExpect(status().isBadRequest());
    }
}
