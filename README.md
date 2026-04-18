# Trie Wordfilter Starter

基于 Trie（前缀树）实现的 Spring Boot 敏感词过滤 Starter。

这个项目最初来自 Trie 刷题思路，但目标不是算法练习，而是做成一个真正可接入、可复用、可扩展的敏感词过滤中间件。当前已经具备：

- 纯 Java 的敏感词匹配内核
- Spring Boot Starter 自动装配
- 默认词库加载
- 自定义词库路径
- 直接注入 `SensitiveWordTemplate` 使用
- `@SensitiveCheck` 注解阻断
- Demo 工程与集成测试

## 项目结构

当前仓库拆分为 4 个模块：

- `trie-wordfilter-core`
  纯 Java 核心算法模块，负责 Trie、匹配、过滤、结构化命中结果等能力。
- `trie-wordfilter-spring-boot-autoconfigure`
  自动配置模块，负责 `ConfigurationProperties`、默认词库加载、Bean 注册。
- `trie-wordfilter-spring-boot-starter`
  对外提供的一站式依赖入口。
- `trie-wordfilter-demo`
  Demo 工程，用于验证 starter 的真实接入方式。

## 当前能力

### 核心能力

- 忽略大小写
- 跳过干扰字符
- 最大匹配原则
- 返回结构化命中结果
- 支持默认词库与自定义词库

### Starter 能力

- `clazs.wordfilter.*` 配置绑定
- `enabled=false` 时关闭自动装配
- 默认加载 `classpath:sensitive-words.txt`
- `dict-path` 支持自定义词库路径
- 启动时词库不存在会直接失败，避免静默失效
- 支持 `@SensitiveCheck` 注解拦截字符串参数

## 快速开始

### 1. 引入依赖

后续发布后，业务项目只需要引入 starter：

```xml
<dependency>
    <groupId>cn.clazs.trie-wordfilter</groupId>
    <artifactId>trie-wordfilter-spring-boot-starter</artifactId>
    <version>${version}</version>
</dependency>
```

如需先安装到本地 Maven 仓库，可以在仓库根目录执行：

```bash
mvn install
```

然后在本地业务项目里引用：

```xml
<dependency>
    <groupId>cn.clazs.trie-wordfilter</groupId>
    <artifactId>trie-wordfilter-spring-boot-starter</artifactId>
    <version>0.2.0</version>
</dependency>
```

### 2. 直接注入模板组件

```java
@Service
public class CommentService {

    private final SensitiveWordTemplate sensitiveWordTemplate;

    public CommentService(SensitiveWordTemplate sensitiveWordTemplate) {
        this.sensitiveWordTemplate = sensitiveWordTemplate;
    }

    public String publish(String text) {
        return sensitiveWordTemplate.filter(text);
    }
}
```

### 3. 常用调用方式

```java
boolean contains = sensitiveWordTemplate.contains("This is a bad message.");
String filtered = sensitiveWordTemplate.filter("This is a bad message.");
String first = sensitiveWordTemplate.findFirst("This is a bad message.");
Set<String> all = sensitiveWordTemplate.findAll("This is a bad and violence message.");
Optional<SensitiveWordMatch> firstMatch = sensitiveWordTemplate.findFirstMatch("This is a bad message.");
List<SensitiveWordMatch> allMatches = sensitiveWordTemplate.findAllMatches("This is a bad and violence message.");
```

## 配置说明

配置前缀：

```yaml
clazs:
  wordfilter:
    enabled: true
    annotation-enabled: true
    dict-path: classpath:sensitive-words.txt
    replacement: "*"
    ignore-case: true
    skip-symbols: true
    max-match: true
```

配置项说明：

- `clazs.wordfilter.enabled`
  是否启用 Starter，默认 `true`
- `clazs.wordfilter.annotation-enabled`
  是否启用 `@SensitiveCheck` 注解增强，默认 `true`
- `clazs.wordfilter.dict-path`
  自定义词库路径，默认使用内置 `classpath:sensitive-words.txt`
- `clazs.wordfilter.replacement`
  过滤时使用的替换字符，默认 `*`
- `clazs.wordfilter.ignore-case`
  是否忽略大小写，默认 `true`
- `clazs.wordfilter.skip-symbols`
  是否跳过干扰字符，默认 `true`
- `clazs.wordfilter.max-match`
  是否启用最大匹配原则，默认 `true`

## 自定义词库示例

例如你可以在业务项目中配置：

```yaml
clazs:
  wordfilter:
    dict-path: classpath:my-sensitive-words.txt
```

如果这个资源不存在，应用会在启动阶段直接失败，避免出现“应用启动了，但敏感词过滤其实没生效”的隐蔽问题。

## 注解增强

当前已经支持第一版注解增强能力：

- 只拦截带 `@SensitiveCheck` 的方法
- 只检查方法直接传入的 `String` 参数
- 命中敏感词后抛出 `SensitiveWordException`
- 可通过 `clazs.wordfilter.annotation-enabled=false` 单独关闭注解增强

使用示例：

```java
import cn.clazs.trie.autoconfigure.annotation.SensitiveCheck;

@Service
public class CommentPublishService {

    @SensitiveCheck
    public String publish(String text) {
        return text;
    }
}
```

当 `text` 中包含敏感词时，方法会在执行前被阻断。

## Demo 工程

当前 demo 模块提供了一个最小 HTTP 示例：

```http
GET /demo/filter?text=This is a bad message.
```

返回：

```text
This is a *** message.
```

还提供了一个更贴近业务发布场景的注解增强示例：

```http
POST /demo/publish?text=normal content
```

返回：

```text
发布成功: normal content
```

如果请求文本命中敏感词：

```http
POST /demo/publish?text=This is a bad message.
```

返回：

```json
{"message":"检测到敏感词: bad"}
```

对应控制器在：

- [TrieWordfilterDemoController.java](trie-wordfilter-demo/src/main/java/cn/clazs/trie/demo/TrieWordfilterDemoController.java)

## 当前阶段

当前项目已经完成首个正式版本 `0.2.0`。当前这版已经具备：

- 可独立使用的 Trie 敏感词过滤内核
- 可直接接入的 Spring Boot Starter
- 默认词库与自定义词库加载
- 模板组件与注解增强两种使用方式
- 完整的 demo 与测试覆盖

下一阶段计划：

- 更丰富的注解策略
- DTO 字段级校验与替换
- 更丰富的词库加载扩展点

## 本地验证

在仓库根目录执行：

```bash
mvn test
```

当前已覆盖：

- 核心算法测试
- 自动配置测试
- demo 接入测试
- 注解增强开关的端到端测试
