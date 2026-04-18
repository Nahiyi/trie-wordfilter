package cn.clazs.trie.autoconfigure;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
}
