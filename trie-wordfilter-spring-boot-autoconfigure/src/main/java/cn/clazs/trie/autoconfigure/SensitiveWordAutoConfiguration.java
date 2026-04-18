package cn.clazs.trie.autoconfigure;

import cn.clazs.trie.autoconfigure.aop.SensitiveCheckAspect;
import cn.clazs.trie.core.SensitiveWordFilter;
import cn.clazs.trie.core.WordDictionary;
import cn.clazs.trie.core.WordFilterOptions;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ResourceLoader;

/**
 * 组件自动配置
 */
@AutoConfiguration
@EnableConfigurationProperties(SensitiveWordProperties.class)
@ConditionalOnProperty(prefix = "clazs.wordfilter", name = "enabled", havingValue = "true", matchIfMissing = true)
public class SensitiveWordAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public WordFilterOptions wordFilterOptions(SensitiveWordProperties properties) {
        WordFilterOptions options = new WordFilterOptions();
        options.setIgnoreCase(properties.isIgnoreCase());
        options.setSkipSymbols(properties.isSkipSymbols());
        options.setMaxMatch(properties.isMaxMatch());
        options.setReplacement(properties.getReplacement());
        return options;
    }

    @Bean
    @ConditionalOnMissingBean
    public WordDictionaryLoader wordDictionaryLoader(ResourceLoader resourceLoader) {
        return new WordDictionaryLoader(resourceLoader);
    }

    @Bean
    @ConditionalOnMissingBean
    public WordDictionary wordDictionary(WordDictionaryLoader loader, SensitiveWordProperties properties) {
        return loader.load(properties.getDictPath());
    }

    @Bean
    @ConditionalOnMissingBean
    public SensitiveWordFilter sensitiveWordFilter(WordFilterOptions options, WordDictionary dictionary) {
        SensitiveWordFilter filter = new SensitiveWordFilter(options);
        filter.addWords(dictionary);
        return filter;
    }

    @Bean
    @ConditionalOnMissingBean
    public SensitiveWordTemplate sensitiveWordTemplate(SensitiveWordFilter filter) {
        return new SensitiveWordTemplate(filter);
    }

    @Bean
    @ConditionalOnMissingBean
    public SensitiveCheckAspect sensitiveCheckAspect(SensitiveWordTemplate template) {
        return new SensitiveCheckAspect(template);
    }
}
