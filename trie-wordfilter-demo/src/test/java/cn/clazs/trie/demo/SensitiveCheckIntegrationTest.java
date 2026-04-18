package cn.clazs.trie.demo;

import cn.clazs.trie.autoconfigure.annotation.SensitiveCheck;
import cn.clazs.trie.autoconfigure.exception.SensitiveWordException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = {TrieWordfilterDemoApplication.class, SensitiveCheckIntegrationTest.TestConfiguration.class})
class SensitiveCheckIntegrationTest {

    @Autowired
    private TestPublishService testPublishService;

    @Test
    void shouldBlockAnnotatedMethodWhenSensitiveWordIsDetected() {
        assertThrows(SensitiveWordException.class, () -> testPublishService.publish("This is a bad message."));
    }

    @Test
    void shouldAllowAnnotatedMethodWhenTextIsClean() {
        assertEquals("normal content", testPublishService.publish("normal content"));
    }

    @Configuration
    static class TestConfiguration {

        @Bean
        TestPublishService testPublishService() {
            return new TestPublishService();
        }
    }

    static class TestPublishService {

        @SensitiveCheck
        public String publish(String text) {
            return text;
        }
    }
}
