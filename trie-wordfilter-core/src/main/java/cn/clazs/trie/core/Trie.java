package cn.clazs.trie.core;

/**
 * 标准 Trie（前缀树）实现
 *
 * <p>支持完整 Unicode 字符集</p>
 */
public class Trie {

    private final TrieNode root;

    public Trie() {
        this.root = new TrieNode();
    }

    public TrieNode getRoot() {
        return root;
    }

    /**
     * 向 Trie 中插入一个词
     *
     * @param word 待插入的词
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
            node = node.getChild(c);
        }
        node.setEnd(true);
    }

    /**
     * 判断某个词是否完整存在于 Trie 中
     *
     * @param word 待查询的词
     * @return 如果该词存在则返回 true
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
     * 判断是否存在以指定前缀开头的词
     *
     * @param prefix 待查询的前缀
     * @return 如果存在此前缀则返回 true
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
