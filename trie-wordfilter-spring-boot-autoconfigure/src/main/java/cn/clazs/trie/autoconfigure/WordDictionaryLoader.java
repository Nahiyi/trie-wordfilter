package cn.clazs.trie.autoconfigure;

import cn.clazs.trie.core.WordDictionary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * 词库加载器
 */
public class WordDictionaryLoader {

    private static final String DEFAULT_DICT_PATH = "classpath:sensitive-words.txt";

    private final ResourceLoader resourceLoader;

    public WordDictionaryLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public WordDictionary load(String dictPath) {
        String location = StringUtils.hasText(dictPath) ? dictPath : DEFAULT_DICT_PATH;
        Resource resource = resourceLoader.getResource(location);
        WordDictionary dictionary = new WordDictionary();
        if (!resource.exists()) {
            return dictionary;
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String word = line.trim();
                if (!word.isEmpty() && !word.startsWith("#")) {
                    dictionary.addWord(word);
                }
            }
        } catch (IOException ex) {
            throw new IllegalStateException("加载敏感词词库失败: " + location, ex);
        }
        return dictionary;
    }
}
