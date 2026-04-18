package cn.clazs.trie.core;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 敏感词词库对象。
 *
 * <p>负责维护一组去重且保持插入顺序的敏感词。</p>
 */
public class WordDictionary {

    private final Set<String> words = new LinkedHashSet<String>();

    public static WordDictionary of(Collection<String> words) {
        WordDictionary dictionary = new WordDictionary();
        dictionary.addWords(words);
        return dictionary;
    }

    public void addWord(String word) {
        if (word == null || word.isEmpty()) {
            return;
        }
        words.add(word);
    }

    public void addWords(Collection<String> words) {
        if (words == null || words.isEmpty()) {
            return;
        }
        for (String word : words) {
            addWord(word);
        }
    }

    public Set<String> getWords() {
        return Collections.unmodifiableSet(words);
    }

    public int size() {
        return words.size();
    }

    public boolean isEmpty() {
        return words.isEmpty();
    }
}
