package com.example.project.springbootproject;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.example.project.springbootproject.domain.BoardDTO;
import com.example.project.springbootproject.domain.BoardDTO.InsertDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(properties = "spring.config.location=" +
    "classpath:/application.properties" )
@AutoConfigureMockMvc
@Transactional
class SpringBootProjectApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


//    @Test
//    @Rollback(value = false)
    void contextLoads() throws Exception {
        for(long i = 0; i < 100000; i++) {
            ResultActions result = this.mockMvc.perform(post("/post/insert")
                .content(objectMapper.writeValueAsString(makeRequest()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
            );
            System.out.println(i);
        }

    }

    private InsertDto makeRequest() {
        InsertDto InsertDto = new InsertDto("title", "content", "userTest");

        return InsertDto;
    }

}
