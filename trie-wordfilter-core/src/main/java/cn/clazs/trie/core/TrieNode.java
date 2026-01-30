package cn.clazs.trie.core;

import java.util.HashMap;
import java.util.Map;

/**
 * Trie 树的节点
 * <p>
 * 使用 HashMap 支持所有 Unicode 字符（中文、表情符号等）
 * </p>
 */
public class TrieNode {

    /**
     * 是否是词的末尾
     */
    private boolean isEnd;

    /**
     * 子节点哈希表；字符 ---> 子节点
     *
     * <p>拿到下一个字符，即可O(1)从哈希表找到对应路径的子节点
     *
     * <p>与力扣208的核心区别就在于
     * 力扣题保障是26个小写字母组成的词，所以直接写死26叉树，加上使用不同子方向本身就蕴含了“当前字符”的信息（所以用数组就够了）
     * 但是本处，字符数量是不确定的，所以必须建立一个“Map映射”，来找到当前字符，是哪一个叉的子节点
     */
    private final Map<Character, TrieNode> children;

    public TrieNode() {
        this.isEnd = false;
        this.children = new HashMap<>();
    }

    public boolean isEnd() {
        return isEnd;
    }

    public void setEnd(boolean end) {
        isEnd = end;
    }

    public TrieNode getChild(Character c) {
        return children.get(c);
    }

    public void addChild(Character c, TrieNode node) {
        children.put(c, node);
    }

    public boolean hasChild(Character c) {
        return children.containsKey(c);
    }
}
