package cn.clazs.trie.autoconfigure;

import cn.clazs.trie.core.SensitiveWordFilter;
import cn.clazs.trie.core.SensitiveWordMatch;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * 提供给 Spring Boot 业务代码使用的模板组件
 */
public class SensitiveWordTemplate {

    private final SensitiveWordFilter filter;

    public SensitiveWordTemplate(SensitiveWordFilter filter) {
        this.filter = filter;
    }

    public void addWord(String word) {
        filter.addWord(word);
    }

    public void addWords(Collection<String> words) {
        filter.addWords(words);
    }

    public boolean contains(String text) {
        return filter.contains(text);
    }

    public String filter(String text) {
        return filter.filter(text);
    }

    public String findFirst(String text) {
        return filter.findFirst(text);
    }

    public Optional<SensitiveWordMatch> findFirstMatch(String text) {
        return filter.findFirstMatch(text);
    }

    public Set<String> findAll(String text) {
        return filter.findAll(text);
    }

    public List<SensitiveWordMatch> findAllMatches(String text) {
        return filter.findAllMatches(text);
    }
}
