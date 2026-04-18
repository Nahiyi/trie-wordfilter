package cn.clazs.trie.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "clazs.wordfilter.annotation-enabled=false")
@AutoConfigureMockMvc
class AnnotationDisabledDemoTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldAllowSensitiveTextWhenAnnotationAspectIsDisabled() throws Exception {
        mockMvc.perform(post("/demo/publish")
                        .param("text", "This is a bad message."))
                .andExpect(status().isOk())
                .andExpect(content().string("发布成功: This is a bad message."));
    }
}
