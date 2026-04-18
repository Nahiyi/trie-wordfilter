package cn.clazs.trie.autoconfigure;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ConfigurationMetadataTest {

    @Test
    void shouldProvideAdditionalConfigurationMetadata() throws IOException {
        Enumeration<java.net.URL> resources = getClass().getClassLoader()
                .getResources("META-INF/additional-spring-configuration-metadata.json");

        boolean found = false;
        while (resources.hasMoreElements()) {
            java.net.URL resource = resources.nextElement();
            try (InputStream inputStream = resource.openStream()) {
                String metadata = new String(readAllBytes(inputStream), StandardCharsets.UTF_8);
                if (metadata.contains("\"name\": \"clazs.wordfilter.enabled\"")
                        && metadata.contains("\"name\": \"clazs.wordfilter.dict-path\"")
                        && metadata.contains("\"name\": \"clazs.wordfilter.replacement\"")) {
                    found = true;
                    break;
                }
            }
        }

        assertTrue(found);
    }

    private byte[] readAllBytes(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int length;
        java.io.ByteArrayOutputStream outputStream = new java.io.ByteArrayOutputStream();
        while ((length = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, length);
        }
        return outputStream.toByteArray();
    }
}
