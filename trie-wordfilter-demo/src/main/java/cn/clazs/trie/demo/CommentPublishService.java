package cn.clazs.trie.demo;

import cn.clazs.trie.autoconfigure.annotation.SensitiveCheck;
import org.springframework.stereotype.Service;

/**
 * 演示业务发布场景的服务类
 */
@Service
public class CommentPublishService {

    @SensitiveCheck
    public String publish(String text) {
        return "发布成功: " + text;
    }
}
