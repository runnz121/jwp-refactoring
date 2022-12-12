package kitchenpos.acceptance;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.UnsupportedEncodingException;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

@SpringBootTest
@AutoConfigureMockMvc
public class MockMvcAcceptanceTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    WebApplicationContext ctx;

    @BeforeEach
    void beforeEach(){
        this.mockMvc = webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }


    ResultActions mockGet(String url) throws Exception{
        return mockMvc.perform(get(url)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("UTF-8")
        );
    }

    ResultActions mockPost(String url, Object body) throws Exception{
        String content = objectMapper.writeValueAsString(body);
        return mockMvc.perform(post(url)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("UTF-8")
                        .content(content)
        );
    }

    ResultActions mockPut(String url, Long targetId, Object body) throws Exception{
        String content = objectMapper.writeValueAsString(body);
        return mockMvc.perform(put(url, targetId)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("UTF-8")
                .content(content)
        );
    }

    ResultActions mockDelete(String url, Long targetId) throws Exception{
        return mockMvc.perform(delete(url, targetId)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("UTF-8")
        );
    }

    <T> T getObjectByResponse(ResultActions resultActions, Class<T> clazz)
            throws JsonProcessingException, UnsupportedEncodingException {
        String responseContent = resultActions.andReturn().getResponse().getContentAsString();
        return objectMapper.readValue(responseContent, clazz);
    }
}
