package cn.clazs.trie.demo;

import cn.clazs.trie.autoconfigure.exception.SensitiveWordException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.Map;

/**
 * Demo 工程的统一异常处理
 */
@RestControllerAdvice
public class DemoExceptionHandler {

    @ExceptionHandler(SensitiveWordException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleSensitiveWordException(SensitiveWordException exception) {
        return Collections.singletonMap("message", exception.getMessage());
    }
}
