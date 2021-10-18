package org.wjw.starter.limiter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan  //@wjw_note: 必须配置这个才会自动注册RateLimiterAspect类
@ConditionalOnClass(RateLimiterAspect.class)  //某个class位于类路径上，才会实例化一个Bean
public class RateLimiterAutoConfiguration {
}
