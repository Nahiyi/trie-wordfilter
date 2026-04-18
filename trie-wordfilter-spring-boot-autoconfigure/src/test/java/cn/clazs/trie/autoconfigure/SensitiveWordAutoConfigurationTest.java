package cn.clazs.trie.autoconfigure;

import cn.clazs.trie.core.SensitiveWordFilter;
import cn.clazs.trie.core.WordDictionary;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SensitiveWordAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(SensitiveWordAutoConfiguration.class));

    @Test
    void shouldRegisterTemplateAndProperties() {
        contextRunner.run(context -> {
            context.getBean(SensitiveWordTemplate.class);
            context.getBean(SensitiveWordProperties.class);
        });
    }

    @Test
    void shouldLoadDefaultDictionaryIntoTemplate() {
        contextRunner.run(context -> {
            SensitiveWordTemplate template = context.getBean(SensitiveWordTemplate.class);

            assertTrue(template.contains("This is a bad message."));
            assertEquals("This is a *** message.", template.filter("This is a bad message."));
        });
    }

    @Test
    void shouldRespectDisabledProperty() {
        contextRunner.withPropertyValues("clazs.wordfilter.enabled=false")
                .run(context -> {
                    assertFalse(context.containsBean("sensitiveWordTemplate"));
                    assertFalse(context.containsBean("sensitiveWordFilter"));
                });
    }

    @Test
    void shouldBindFilterOptionsFromProperties() {
        contextRunner.withPropertyValues(
                        "clazs.wordfilter.replacement=#",
                        "clazs.wordfilter.ignore-case=false")
                .run(context -> {
                    SensitiveWordTemplate template = context.getBean(SensitiveWordTemplate.class);

                    assertFalse(template.contains("BAD"));
                    assertEquals("this is a ### message.", template.filter("this is a bad message."));
                });
    }

    @Test
    void shouldFailFastWhenCustomDictionaryPathDoesNotExist() {
        contextRunner.withPropertyValues("clazs.wordfilter.dict-path=classpath:not-found-words.txt")
                .run(context -> {
                    assertNotNull(context.getStartupFailure());
                    assertTrue(context.getStartupFailure().getMessage().contains("not-found-words.txt"));
                });
    }

    @Test
    void shouldPrepareFilterBeanBeforeTemplateCreation() {
        contextRunner.run(context -> {
            assertTrue(context.getBean("sensitiveWordFilter", cn.clazs.trie.core.SensitiveWordFilter.class)
                    .contains("This is a bad message."));
        });
    }

    @Test
    void shouldLoadCustomDictionaryPath() {
        contextRunner.withPropertyValues("clazs.wordfilter.dict-path=classpath:custom-sensitive-words.txt")
                .run(context -> {
                    SensitiveWordTemplate template = context.getBean(SensitiveWordTemplate.class);

                    assertTrue(template.contains("this is custom content"));
                    assertEquals("this is ****** content", template.filter("this is custom content"));
                    assertFalse(template.contains("this is a bad message."));
                });
    }

    @Test
    void shouldRespectCustomSensitiveWordFilterBean() {
        contextRunner.withUserConfiguration(CustomFilterConfiguration.class)
                .run(context -> {
                    SensitiveWordTemplate template = context.getBean(SensitiveWordTemplate.class);
                    SensitiveWordFilter filter = context.getBean(SensitiveWordFilter.class);

                    Assertions.assertSame(filter, context.getBean(SensitiveWordFilter.class));
                    assertTrue(template.contains("custom only text"));
                    assertFalse(template.contains("This is a bad message."));
                });
    }

    @Test
    void shouldRespectCustomWordDictionaryBean() {
        contextRunner.withUserConfiguration(CustomDictionaryConfiguration.class)
                .run(context -> {
                    SensitiveWordTemplate template = context.getBean(SensitiveWordTemplate.class);

                    assertTrue(template.contains("only custom dictionary"));
                    assertFalse(template.contains("This is a bad message."));
                });
    }

    @Configuration
    static class CustomFilterConfiguration {

        @Bean
        SensitiveWordFilter sensitiveWordFilter() {
            SensitiveWordFilter filter = new SensitiveWordFilter();
            filter.addWord("custom");
            return filter;
        }
    }

    @Configuration
    static class CustomDictionaryConfiguration {

        @Bean
        WordDictionary wordDictionary() {
            WordDictionary dictionary = new WordDictionary();
            dictionary.addWord("custom");
            return dictionary;
        }
    }
}
