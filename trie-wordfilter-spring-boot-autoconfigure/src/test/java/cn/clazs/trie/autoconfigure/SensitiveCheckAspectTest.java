package cn.clazs.trie.autoconfigure;

import cn.clazs.trie.autoconfigure.annotation.SensitiveCheck;
import cn.clazs.trie.autoconfigure.exception.SensitiveWordException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.junit.jupiter.api.Assertions.assertThrows;

class SensitiveCheckAspectTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(AopAutoConfiguration.class, SensitiveWordAutoConfiguration.class))
            .withUserConfiguration(TestConfiguration.class);

    @Test
    void shouldBlockMethodWhenAnnotatedStringArgumentContainsSensitiveWord() {
        contextRunner.run(context -> {
            TestService testService = context.getBean(TestService.class);

            assertThrows(SensitiveWordException.class, () -> testService.publish("This is a bad message."));
        });
    }

    @Configuration
    static class TestConfiguration {

        @Bean
        TestService testService() {
            return new TestService();
        }
    }

    static class TestService {

        @SensitiveCheck
        public String publish(String text) {
            return text;
        }
    }
}
