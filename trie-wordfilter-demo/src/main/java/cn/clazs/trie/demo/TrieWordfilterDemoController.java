package cn.clazs.trie.demo;

import cn.clazs.trie.autoconfigure.SensitiveWordTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Demo 控制器，用于演示 starter 的接入方式
 */
@RestController
public class TrieWordfilterDemoController {

    private final SensitiveWordTemplate sensitiveWordTemplate;
    private final CommentPublishService commentPublishService;

    public TrieWordfilterDemoController(SensitiveWordTemplate sensitiveWordTemplate,
                                        CommentPublishService commentPublishService) {
        this.sensitiveWordTemplate = sensitiveWordTemplate;
        this.commentPublishService = commentPublishService;
    }

    @GetMapping("/demo/filter")
    public String filter(@RequestParam("text") String text) {
        return sensitiveWordTemplate.filter(text);
    }

    @PostMapping("/demo/publish")
    public String publish(@RequestParam("text") String text) {
        return commentPublishService.publish(text);
    }
}
