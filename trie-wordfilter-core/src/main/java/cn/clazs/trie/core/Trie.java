package cn.clazs.trie.core;

/**
 * 标准Trie（前缀树）实现
 * <p>
 * 支持完整的Unicode字符集
 */
public class Trie {

    private final TrieNode root;

    public Trie() {
        this.root = new TrieNode();
    }

    /**
     * 在 trie 中插入一个词
     *
     * @param word 插入的词
     */
    public void insert(String word) {
        if (word == null || word.isEmpty()) {
            return;
        }
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            if (!node.hasChild(c)) {
                node.addChild(c, new TrieNode());
            }
            // 向下一层遍历
            node = node.getChild(c);
        }
        // 最终遍历完毕标记终点
        node.setEnd(true);
    }

    /**
     * 如果单词在 trie 中，则返回为true
     *
     * @param word 待查找的词
     */
    public boolean contains(String word) {
        if (word == null || word.isEmpty()) {
            return false;
        }
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            if (!node.hasChild(c)) {
                return false;
            }
            node = node.getChild(c);
        }
        return node.isEnd();
    }

    /**
     * 如果 trie 中存在任何以该前缀开头的单词，则返回 true
     *
     * @param prefix 待查找的前缀
     */
    public boolean startsWith(String prefix) {
        if (prefix == null || prefix.isEmpty()) {
            return false;
        }
        TrieNode node = root;
        for (char c : prefix.toCharArray()) {
            if (!node.hasChild(c)) {
                return false;
            }
            node = node.getChild(c);
        }
        return true;
    }
}
