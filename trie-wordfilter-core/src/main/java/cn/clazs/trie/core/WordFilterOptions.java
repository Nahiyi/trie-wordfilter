package cn.clazs.trie.core;

/**
 * 敏感词匹配与过滤时使用的运行参数
 */
public class WordFilterOptions {

    private boolean ignoreCase = true;
    private boolean skipSymbols = true;
    private boolean maxMatch = true;
    private char replacement = '*';

    public boolean isIgnoreCase() {
        return ignoreCase;
    }

    public void setIgnoreCase(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
    }

    public boolean isSkipSymbols() {
        return skipSymbols;
    }

    public void setSkipSymbols(boolean skipSymbols) {
        this.skipSymbols = skipSymbols;
    }

    public boolean isMaxMatch() {
        return maxMatch;
    }

    public void setMaxMatch(boolean maxMatch) {
        this.maxMatch = maxMatch;
    }

    public char getReplacement() {
        return replacement;
    }

    public void setReplacement(char replacement) {
        this.replacement = replacement;
    }
}
