package cn.clazs.trie.demo;

import cn.clazs.trie.autoconfigure.SensitiveWordTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Demo 控制器，用于演示 starter 的接入方式
 */
@RestController
public class TrieWordfilterDemoController {

    private final SensitiveWordTemplate sensitiveWordTemplate;

    public TrieWordfilterDemoController(SensitiveWordTemplate sensitiveWordTemplate) {
        this.sensitiveWordTemplate = sensitiveWordTemplate;
    }

    @GetMapping("/demo/filter")
    public String filter(@RequestParam("text") String text) {
        return sensitiveWordTemplate.filter(text);
    }
}
