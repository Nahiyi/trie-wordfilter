package cn.clazs.trie.core;

/**
 * 敏感词命中结果的结构化信息
 */
public class SensitiveWordMatch {

    private final String matchedText;
    private final String normalizedWord;
    private final int start;
    private final int end;

    public SensitiveWordMatch(String matchedText, String normalizedWord, int start, int end) {
        this.matchedText = matchedText;
        this.normalizedWord = normalizedWord;
        this.start = start;
        this.end = end;
    }

    public String getMatchedText() {
        return matchedText;
    }

    public String getNormalizedWord() {
        return normalizedWord;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }
}
