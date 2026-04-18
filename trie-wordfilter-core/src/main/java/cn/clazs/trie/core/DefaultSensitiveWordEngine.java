package cn.clazs.trie.core;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

/**
 * 默认的基于 Trie 的敏感词引擎实现
 */
public class DefaultSensitiveWordEngine implements SensitiveWordEngine {

    private final Trie trie;
    private final WordFilterOptions options;

    public DefaultSensitiveWordEngine() {
        this(new WordFilterOptions());
    }

    public DefaultSensitiveWordEngine(WordFilterOptions options) {
        this.trie = new Trie();
        this.options = options == null ? new WordFilterOptions() : options;
    }

    @Override
    public void addWord(String word) {
        if (word == null || word.isEmpty()) {
            return;
        }
        trie.insert(normalize(word));
    }

    @Override
    public void addWords(Collection<String> words) {
        if (words == null || words.isEmpty()) {
            return;
        }
        for (String word : words) {
            addWord(word);
        }
    }

    @Override
    public boolean contains(String text) {
        return findFirst(text).isPresent();
    }

    @Override
    public String filter(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        StringBuilder result = new StringBuilder(text);
        int index = 0;
        while (index < text.length()) {
            MatchCandidate candidate = matchAt(text, index);
            if (candidate == null) {
                index++;
                continue;
            }

            for (int i = candidate.start; i <= candidate.end; i++) {
                result.setCharAt(i, options.getReplacement());
            }
            index = candidate.end + 1;
        }
        return result.toString();
    }

    @Override
    public Optional<SensitiveWordMatch> findFirst(String text) {
        if (text == null || text.isEmpty()) {
            return Optional.empty();
        }

        for (int index = 0; index < text.length(); index++) {
            MatchCandidate candidate = matchAt(text, index);
            if (candidate != null) {
                return Optional.of(candidate.toMatch(text));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<SensitiveWordMatch> findAll(String text) {
        if (text == null || text.isEmpty()) {
            return Collections.emptyList();
        }

        List<SensitiveWordMatch> matches = new ArrayList<SensitiveWordMatch>();
        int index = 0;
        while (index < text.length()) {
            MatchCandidate candidate = matchAt(text, index);
            if (candidate == null) {
                index++;
                continue;
            }

            matches.add(candidate.toMatch(text));
            index = candidate.end + 1;
        }
        return matches;
    }

    private MatchCandidate matchAt(String text, int beginIndex) {
        TrieNode node = trie.getRoot();
        MatchCandidate bestCandidate = null;
        StringBuilder normalized = new StringBuilder();

        for (int i = beginIndex; i < text.length(); i++) {
            char current = text.charAt(i);

            if (shouldSkipSymbol(current, node)) {
                continue;
            }

            char normalizedChar = normalize(current);
            node = node.getChild(normalizedChar);
            if (node == null) {
                break;
            }

            normalized.append(normalizedChar);
            if (node.isEnd()) {
                bestCandidate = new MatchCandidate(beginIndex, i, normalized.toString());
                if (!options.isMaxMatch()) {
                    break;
                }
            }
        }

        return bestCandidate;
    }

    private boolean shouldSkipSymbol(char current, TrieNode node) {
        return options.isSkipSymbols() && node != trie.getRoot() && isSymbol(current);
    }

    private String normalize(String text) {
        if (!options.isIgnoreCase()) {
            return text;
        }
        return text.toLowerCase();
    }

    private char normalize(char current) {
        if (!options.isIgnoreCase()) {
            return current;
        }
        return Character.toLowerCase(current);
    }

    private boolean isSymbol(char c) {
        int ic = (int) c;
        return !Character.isLetterOrDigit(c) && (ic < 0x2E80 || ic > 0x9FFF);
    }

    private static final class MatchCandidate {
        private final int start;
        private final int end;
        private final String normalizedWord;

        private MatchCandidate(int start, int end, String normalizedWord) {
            this.start = start;
            this.end = end;
            this.normalizedWord = normalizedWord;
        }

        private SensitiveWordMatch toMatch(String text) {
            return new SensitiveWordMatch(text.substring(start, end + 1), normalizedWord, start, end);
        }
    }
}
