package cn.clazs.trie.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 组件配置项Properties类
 */
@ConfigurationProperties(prefix = "clazs.wordfilter")
public class SensitiveWordProperties {

    /**
     * 是否启用敏感词过滤组件
     */
    private boolean enabled = true;

    /**
     * 是否启用注解增强切面
     */
    private boolean annotationEnabled = true;

    /**
     * 自定义词库路径，支持 classpath: 前缀
     */
    private String dictPath;

    /**
     * 命中敏感词后的替换字符
     */
    private char replacement = '*';

    /**
     * 是否忽略大小写
     */
    private boolean ignoreCase = true;

    /**
     * 是否跳过干扰字符
     */
    private boolean skipSymbols = true;

    /**
     * 是否启用最大匹配原则
     */
    private boolean maxMatch = true;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isAnnotationEnabled() {
        return annotationEnabled;
    }

    public void setAnnotationEnabled(boolean annotationEnabled) {
        this.annotationEnabled = annotationEnabled;
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
