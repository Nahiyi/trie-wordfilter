package cn.clazs.trie.autoconfigure.exception;

/**
 * 敏感词命中后的运行时异常
 */
public class SensitiveWordException extends RuntimeException {

    public SensitiveWordException(String message) {
        super(message);
    }
}
