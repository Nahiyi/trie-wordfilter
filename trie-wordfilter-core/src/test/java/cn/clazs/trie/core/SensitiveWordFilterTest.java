package cn.clazs.trie.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

class SensitiveWordFilterTest {

    private SensitiveWordFilter filter;

    @BeforeEach
    void setUp() {
        filter = new SensitiveWordFilter();
        // Add some sensitive words
        filter.put("bad");
        filter.put("violence");
        filter.put("上海");
        filter.put("上海滩");
    }

    @Test
    void testBasicFilter() {
        String text = "This is a bad word.";
        String result = filter.filter(text);
        Assertions.assertEquals("This is a *** word.", result);
    }

    @Test
    void testIgnoreCase() {
        filter.setIgnoreCase(true);
        String text = "This is a BAD word.";
        String result = filter.filter(text);
        Assertions.assertEquals("This is a *** word.", result);
    }

    @Test
    void testIgnoreNoise() {
        String text = "This is a b&a&d word.";
        String result = filter.filter(text);
        Assertions.assertEquals("This is a ***** word.", result);
    }

    @Test
    void testMaxMatch() {
        String text = "我们要去上海滩玩。";
        // Should match "上海滩" (3 chars) not "上海" (2 chars)
        // "上海滩" -> "***"
        String result = filter.filter(text);
        Assertions.assertEquals("我们要去***玩。", result);
    }

    @Test
    void testFindFirst() {
        String text = "There is violence and bad things.";
        String first = filter.findFirst(text);
        Assertions.assertEquals("violence", first);
    }

    @Test
    void testFindAll() {
        String text = "There is violence and bad things.";
        Set<String> all = filter.findAll(text);
        Assertions.assertTrue(all.contains("violence"));
        Assertions.assertTrue(all.contains("bad"));
        Assertions.assertEquals(2, all.size());
    }
    
    @Test
    void testSymbolStart() {
        // Test noise at start
        String text = " &bad ";
        // "bad" is sensitive. 
        // " &bad " -> " &*** " because we don't skip leading noise at root
        String result = filter.filter(text);
        Assertions.assertEquals(" &*** ", result);
    }
    
    @Test
    void testSymbolInside() {
        String text = "v&i&o&l&e&n&c&e";
        String result = filter.filter(text);
        Assertions.assertTrue(result.contains("*"));
        Assertions.assertEquals("***************", result); // Replaces all chars including noise
    }

    @Test
    void testFormalApiNames() {
        SensitiveWordFilter anotherFilter = new SensitiveWordFilter();
        anotherFilter.addWord("test");
        anotherFilter.addWords(Set.of("hello", "world"));

        Assertions.assertEquals("****", anotherFilter.filter("test"));
        Assertions.assertTrue(anotherFilter.findAll("hello world").contains("hello"));
        Assertions.assertTrue(anotherFilter.findAll("hello world").contains("world"));
    }
}
