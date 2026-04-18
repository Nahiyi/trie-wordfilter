package cn.clazs.trie.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 组件配置项Properties类
 */
@ConfigurationProperties(prefix = "clazs.wordfilter")
public class SensitiveWordProperties {

    private boolean enabled = true;
    private String dictPath;
    private char replacement = '*';
    private boolean ignoreCase = true;
    private boolean skipSymbols = true;
    private boolean maxMatch = true;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getDictPath() {
        return dictPath;
    }

    public void setDictPath(String dictPath) {
        this.dictPath = dictPath;
    }

    public char getReplacement() {
        return replacement;
    }

    public void setReplacement(char replacement) {
        this.replacement = replacement;
    }

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
}
