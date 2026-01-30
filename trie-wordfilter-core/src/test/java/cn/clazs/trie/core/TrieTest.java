package cn.clazs.trie.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TrieTest {

    @Test
    public void testBasicTrie() {
        Trie trie = new Trie();
        trie.insert("hello");
        trie.insert("world");
        trie.insert("你好");
        trie.insert("🎈🤔");
        trie.insert("🎯🌐");

        Assertions.assertTrue(trie.contains("hello"));
        Assertions.assertTrue(trie.contains("world"));
        Assertions.assertTrue(trie.contains("你好"));
        Assertions.assertTrue(trie.contains("🎈🤔"));

        Assertions.assertTrue(trie.startsWith("🎯"));
        Assertions.assertTrue(trie.startsWith("🎯🌐"));

        Assertions.assertFalse(trie.contains("hell"));
        Assertions.assertTrue(trie.startsWith("hell"));
        Assertions.assertFalse(trie.contains("helloo"));
        Assertions.assertFalse(trie.contains("你"));
        Assertions.assertTrue(trie.startsWith("你"));
    }
}
