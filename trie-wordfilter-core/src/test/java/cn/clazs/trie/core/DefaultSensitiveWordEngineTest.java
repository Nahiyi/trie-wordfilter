package cn.clazs.trie.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class DefaultSensitiveWordEngineTest {

    @Test
    void containsReturnsTrueWhenTextIncludesSensitiveWord() {
        DefaultSensitiveWordEngine engine = new DefaultSensitiveWordEngine();
        engine.addWord("bad");

        Assertions.assertTrue(engine.contains("this is a bad example"));
        Assertions.assertFalse(engine.contains("this is a good example"));
    }

    @Test
    void filterUsesConfiguredReplacementCharacter() {
        WordFilterOptions options = new WordFilterOptions();
        options.setReplacement('#');

        DefaultSensitiveWordEngine engine = new DefaultSensitiveWordEngine(options);
        engine.addWord("bad");

        Assertions.assertEquals("this is a ### example", engine.filter("this is a bad example"));
    }

    @Test
    void findFirstReturnsStructuredMatch() {
        DefaultSensitiveWordEngine engine = new DefaultSensitiveWordEngine();
        engine.addWords(Arrays.asList("violence", "bad"));

        Optional<SensitiveWordMatch> match = engine.findFirst("there is violence here");

        Assertions.assertTrue(match.isPresent());
        Assertions.assertEquals("violence", match.get().getMatchedText());
        Assertions.assertEquals("violence", match.get().getNormalizedWord());
        Assertions.assertEquals(9, match.get().getStart());
        Assertions.assertEquals(16, match.get().getEnd());
    }

    @Test
    void findAllReturnsMatchesInEncounterOrder() {
        DefaultSensitiveWordEngine engine = new DefaultSensitiveWordEngine();
        engine.addWords(Arrays.asList("violence", "bad"));

        List<SensitiveWordMatch> matches = engine.findAll("violence and bad");

        Assertions.assertEquals(2, matches.size());
        Assertions.assertEquals("violence", matches.get(0).getMatchedText());
        Assertions.assertEquals("bad", matches.get(1).getMatchedText());
        Assertions.assertEquals(13, matches.get(1).getStart());
        Assertions.assertEquals(15, matches.get(1).getEnd());
    }

    @Test
    void optionsCanDisableIgnoreCaseMatching() {
        WordFilterOptions options = new WordFilterOptions();
        options.setIgnoreCase(false);

        DefaultSensitiveWordEngine engine = new DefaultSensitiveWordEngine(options);
        engine.addWord("bad");

        Assertions.assertFalse(engine.contains("BAD"));
        Assertions.assertTrue(engine.contains("bad"));
    }

    @Test
    void optionsCanDisableSymbolSkipping() {
        WordFilterOptions options = new WordFilterOptions();
        options.setSkipSymbols(false);

        DefaultSensitiveWordEngine engine = new DefaultSensitiveWordEngine(options);
        engine.addWord("bad");

        Assertions.assertFalse(engine.contains("b&a&d"));
        Assertions.assertEquals("b&a&d", engine.filter("b&a&d"));
    }

    @Test
    void optionsCanDisableMaxMatch() {
        WordFilterOptions options = new WordFilterOptions();
        options.setMaxMatch(false);

        DefaultSensitiveWordEngine engine = new DefaultSensitiveWordEngine(options);
        engine.addWords(Arrays.asList("上海", "上海滩"));

        Optional<SensitiveWordMatch> match = engine.findFirst("我们要去上海滩玩");

        Assertions.assertTrue(match.isPresent());
        Assertions.assertEquals("上海", match.get().getMatchedText());
        Assertions.assertEquals(4, match.get().getStart());
        Assertions.assertEquals(5, match.get().getEnd());
    }
}
