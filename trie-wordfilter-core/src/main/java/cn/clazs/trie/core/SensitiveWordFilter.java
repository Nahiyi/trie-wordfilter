package cn.clazs.trie.core;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * 基于 Trie 的敏感词过滤器
 *
 * <p>当前支持的能力包括：</p>
 * <ul>
 *     <li>基于 Trie 的高性能匹配</li>
 *     <li>忽略大小写</li>
 *     <li>跳过干扰字符</li>
 *     <li>最大匹配原则</li>
 * </ul>
 */
public class SensitiveWordFilter {

    private final WordFilterOptions options;
    private final DefaultSensitiveWordEngine engine;

    public SensitiveWordFilter() {
        this.options = new WordFilterOptions();
        this.engine = new DefaultSensitiveWordEngine(options);
    }

    /**
     * 设置是否忽略大小写
     *
     * <p>默认值为 true</p>
     *
     * @param ignoreCase 是否忽略大小写
     */
    public void setIgnoreCase(boolean ignoreCase) {
        options.setIgnoreCase(ignoreCase);
    }

    /**
     * 设置敏感词替换字符
     *
     * <p>默认值为 '*'</p>
     *
     * @param replacement 替换字符
     */
    public void setReplacement(char replacement) {
        options.setReplacement(replacement);
    }

    /**
     * 设置是否跳过匹配过程中的干扰字符
     *
     * <p>默认值为 true</p>
     *
     * @param skipSymbols 是否跳过干扰字符
     */
    public void setSkipSymbols(boolean skipSymbols) {
        options.setSkipSymbols(skipSymbols);
    }

    /**
     * 设置是否启用最大匹配原则
     *
     * <p>默认值为 true</p>
     *
     * @param maxMatch 是否启用最大匹配
     */
    public void setMaxMatch(boolean maxMatch) {
        options.setMaxMatch(maxMatch);
    }

    /**
     * 向过滤器中添加一个敏感词
     *
     * @param word 敏感词
     */
    public void put(String word) {
        engine.addWord(word);
    }

    /**
     * 向过滤器中添加一个敏感词
     *
     * <p>这是面向正式对外 API 的命名，等价于 {@link #put(String)}</p>
     *
     * @param word 敏感词
     */
    public void addWord(String word) {
        put(word);
    }

    /**
     * 批量向过滤器中添加敏感词
     *
     * @param words 敏感词集合
     */
    public void putAll(Set<String> words) {
        engine.addWords(words);
    }

    /**
     * 批量向过滤器中添加敏感词
     *
     * <p>这是面向正式对外 API 的命名，等价于 {@link #putAll(Set)}</p>
     *
     * @param words 敏感词集合
     */
    public void addWords(Set<String> words) {
        putAll(words);
    }

    /**
     * 过滤文本中的敏感词
     *
     * @param text 输入文本
     * @return 过滤后的文本
     */
    public String filter(String text) {
        return engine.filter(text);
    }

    /**
     * 判断文本中是否包含敏感词
     *
     * @param text 输入文本
     * @return 如果命中敏感词则返回 true
     */
    public boolean contains(String text) {
        return engine.contains(text);
    }

    /**
     * 查找文本中命中的第一个敏感词
     *
     * @param text 输入文本
     * @return 命中的第一个敏感词；如果未命中则返回 null
     */
    public String findFirst(String text) {
        return engine.findFirst(text).map(SensitiveWordMatch::getMatchedText).orElse(null);
    }

    /**
     * 查找文本中命中的第一个结构化结果
     *
     * @param text 输入文本
     * @return 第一个命中的结构化结果；如果未命中则返回空
     */
    public Optional<SensitiveWordMatch> findFirstMatch(String text) {
        return engine.findFirst(text);
    }

    /**
     * 查找文本中命中的全部敏感词
     *
     * @param text 输入文本
     * @return 命中的敏感词集合
     */
    public Set<String> findAll(String text) {
        List<SensitiveWordMatch> matches = engine.findAll(text);
        Set<String> sensitiveWords = new LinkedHashSet<String>();
        for (SensitiveWordMatch match : matches) {
            sensitiveWords.add(match.getMatchedText());
        }
        return sensitiveWords;
    }

    /**
     * 查找文本中命中的全部结构化结果
     *
     * @param text 输入文本
     * @return 命中的全部结构化结果
     */
    public List<SensitiveWordMatch> findAllMatches(String text) {
        return engine.findAll(text);
    }
}
