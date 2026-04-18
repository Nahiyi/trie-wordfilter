package cn.clazs.trie.demo;

import cn.clazs.trie.autoconfigure.SensitiveWordTemplate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TrieWordfilterDemoApplicationTest {

    @Autowired
    private SensitiveWordTemplate sensitiveWordTemplate;

    @Test
    void shouldInjectTemplateAndUseDefaultDictionary() {
        Assertions.assertNotNull(sensitiveWordTemplate);
        Assertions.assertTrue(sensitiveWordTemplate.contains("This is a bad message."));
        Assertions.assertEquals("This is a *** message.", sensitiveWordTemplate.filter("This is a bad message."));
    }
}
