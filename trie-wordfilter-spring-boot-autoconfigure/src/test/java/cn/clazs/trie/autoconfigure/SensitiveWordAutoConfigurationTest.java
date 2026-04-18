package cn.clazs.trie.autoconfigure;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

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
}
