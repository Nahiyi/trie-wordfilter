package cn.clazs.trie.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Iterator;

class WordDictionaryTest {

    @Test
    void preservesInsertionOrderAndDeduplicatesWords() {
        WordDictionary dictionary = new WordDictionary();
        dictionary.addWord("hello");
        dictionary.addWord("world");
        dictionary.addWord("hello");

        Assertions.assertEquals(2, dictionary.size());
        Iterator<String> iterator = dictionary.getWords().iterator();
        Assertions.assertEquals("hello", iterator.next());
        Assertions.assertEquals("world", iterator.next());
    }

    @Test
    void canBeCreatedFromCollection() {
        WordDictionary dictionary = WordDictionary.of(Arrays.asList("alpha", "beta", "alpha"));

        Assertions.assertEquals(2, dictionary.size());
        Assertions.assertTrue(dictionary.getWords().contains("alpha"));
        Assertions.assertTrue(dictionary.getWords().contains("beta"));
    }
}
