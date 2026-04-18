package cn.clazs.trie.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TrieWordfilterDemoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldFilterTextThroughHttpEndpoint() throws Exception {
        mockMvc.perform(get("/demo/filter")
                        .param("text", "This is a bad message.")
                        .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(content().string("This is a *** message."));
    }

    @Test
    void shouldPublishCleanTextThroughAnnotatedEndpoint() throws Exception {
        mockMvc.perform(post("/demo/publish")
                        .param("text", "normal content")
                        .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(content().string("发布成功: normal content"));
    }

    @Test
    void shouldBlockSensitiveTextThroughAnnotatedEndpoint() throws Exception {
        mockMvc.perform(post("/demo/publish")
                        .param("text", "This is a bad message.")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"message\":\"检测到敏感词: bad\"}"));
    }
}
