package cn.clazs.trie.core;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * 敏感词引擎的稳定对外接口
 */
public interface SensitiveWordEngine {

    void addWord(String word);

    void addWords(Collection<String> words);

    boolean contains(String text);

    String filter(String text);

    Optional<SensitiveWordMatch> findFirst(String text);

    List<SensitiveWordMatch> findAll(String text);
}
