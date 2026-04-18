package cn.clazs.trie.autoconfigure.aop;

import cn.clazs.trie.autoconfigure.SensitiveWordTemplate;
import cn.clazs.trie.autoconfigure.exception.SensitiveWordException;
import cn.clazs.trie.core.SensitiveWordMatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.util.Optional;

/**
 * 基于注解的敏感词校验切面
 */
@Aspect
public class SensitiveCheckAspect {

    private final SensitiveWordTemplate sensitiveWordTemplate;

    public SensitiveCheckAspect(SensitiveWordTemplate sensitiveWordTemplate) {
        this.sensitiveWordTemplate = sensitiveWordTemplate;
    }

    @Around("@annotation(cn.clazs.trie.autoconfigure.annotation.SensitiveCheck)")
    public Object checkSensitiveWords(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        if (args != null) {
            for (Object arg : args) {
                if (arg instanceof String) {
                    validateText((String) arg);
                }
            }
        }
        return joinPoint.proceed();
    }

    private void validateText(String text) {
        Optional<SensitiveWordMatch> match = sensitiveWordTemplate.findFirstMatch(text);
        if (match.isPresent()) {
            throw new SensitiveWordException("检测到敏感词: " + match.get().getNormalizedWord());
        }
    }
}
