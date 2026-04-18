package cn.clazs.trie.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
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
        anotherFilter.addWords(new LinkedHashSet<String>(Arrays.asList("hello", "world")));

        Assertions.assertEquals("****", anotherFilter.filter("test"));
        Assertions.assertTrue(anotherFilter.contains("test case"));
        Assertions.assertTrue(anotherFilter.findAll("hello world").contains("hello"));
        Assertions.assertTrue(anotherFilter.findAll("hello world").contains("world"));
    }

    @Test
    void testDisableSkipSymbols() {
        SensitiveWordFilter anotherFilter = new SensitiveWordFilter();
        anotherFilter.addWord("bad");
        anotherFilter.setSkipSymbols(false);

        Assertions.assertFalse(anotherFilter.contains("b&a&d"));
        Assertions.assertEquals("b&a&d", anotherFilter.filter("b&a&d"));
    }

    @Test
    void testDisableMaxMatch() {
        SensitiveWordFilter anotherFilter = new SensitiveWordFilter();
        anotherFilter.addWords(new LinkedHashSet<String>(Arrays.asList("上海", "上海滩")));
        anotherFilter.setMaxMatch(false);

        Assertions.assertEquals("我们要去**滩玩。", anotherFilter.filter("我们要去上海滩玩。"));
        Assertions.assertEquals("上海", anotherFilter.findFirst("我们要去上海滩玩。"));
    }

    @Test
    void testFindFirstMatch() {
        Optional<SensitiveWordMatch> match = filter.findFirstMatch("There is bad news.");

        Assertions.assertTrue(match.isPresent());
        Assertions.assertEquals("bad", match.get().getMatchedText());
        Assertions.assertEquals(9, match.get().getStart());
        Assertions.assertEquals(11, match.get().getEnd());
    }

    @Test
    void testFindAllMatchesRetainsMultipleOccurrences() {
        SensitiveWordFilter anotherFilter = new SensitiveWordFilter();
        anotherFilter.addWord("bad");

        List<SensitiveWordMatch> matches = anotherFilter.findAllMatches("bad and bad");

        Assertions.assertEquals(2, matches.size());
        Assertions.assertEquals(0, matches.get(0).getStart());
        Assertions.assertEquals(8, matches.get(1).getStart());
    }

    @Test
    void testNullAndEmptyInputs() {
        Assertions.assertFalse(filter.contains(null));
        Assertions.assertFalse(filter.contains(""));
        Assertions.assertNull(filter.filter(null));
        Assertions.assertEquals("", filter.filter(""));
        Assertions.assertNull(filter.findFirst(null));
        Assertions.assertFalse(filter.findFirstMatch(null).isPresent());
        Assertions.assertTrue(filter.findAll("").isEmpty());
        Assertions.assertTrue(filter.findAllMatches("").isEmpty());
    }

    @Test
    void testDuplicateWordsDoNotBreakMatching() {
        SensitiveWordFilter anotherFilter = new SensitiveWordFilter();
        anotherFilter.addWord("bad");
        anotherFilter.addWord("bad");

        Assertions.assertTrue(anotherFilter.contains("bad"));
        Assertions.assertEquals("bad", anotherFilter.findFirst("bad"));
        Assertions.assertEquals(1, anotherFilter.findAll("bad").size());
    }
}
