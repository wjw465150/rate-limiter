# rate-limiter
使用Guava提供的RateLimiter实现SpringBoot项目中基于注解的限流库.

Guava提供的RateLimiter可以限制物理或逻辑资源的被访问速率，咋一听有点像java并发包下的Samephore，但是又不相同，RateLimiter控制的是速率，Samephore控制的是并发量。

RateLimiter的原理类似于令牌桶，它主要由许可发出的速率来定义，如果没有额外的配置，许可证将按每秒许可证规定的固定速度分配，许可将被平滑地分发，若请求超过permitsPerSecond则RateLimiter按照每秒 `1/permitsPerSecond` 的速率释放许可。

### 快速开始

#### 项目通过Maven的`pom.xml`引入。

```xml

<dependency>
    <groupId>com.github.wjw465150</groupId>
    <artifactId>rate-limiter-spring-boot-starter</artifactId>
    <version>1.2.0</version>
</dependency>

```

#### 或者通过Gradle的`build.gradle`引入

```gradle
// https://mvnrepository.com/artifact/com.github.wjw465150/aop-log
implementation group: 'com.github.wjw465150', name: rate-limiter-spring-boot-starter', version: '1.2.0'
```

#### 使用@RateLimit注解，进行限流

直接在类方法(作用于方法)上加上注解`@RateLimit`,进行限流

例如 :

```java
package TestRateLimitStarter;

import org.springframework.stereotype.Component;
import org.wjw.starter.limiter.RateLimit;

@Component
public class MyRateLimitTask {
  @RateLimit(permitsPerSecond=2)  //每秒允许2个,每次获取1个许可
  void doRunWork1(int i) {
    System.out.println("doRunWork1 call execute.." + i);
  }
  
  @RateLimit(permitsPerSecond=100,permits=2) //每秒允许100个,每次获取2个许可
  void doRunWork2(int i) {
    System.out.println("doRunWork2 call execute.." + i);
  }
  
}

```

