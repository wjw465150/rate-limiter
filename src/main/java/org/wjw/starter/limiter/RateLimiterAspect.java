/*
 * author: @wjw
 * date:   2021年10月18日 下午4:46:16
 * note: 
 */
package org.wjw.starter.limiter;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PreDestroy;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import com.google.common.util.concurrent.RateLimiter;

/**
 * 限流切面类
 */
@Component
@Aspect  //作用是把当前类标识为一个切面供容器读取
//表示开启AOP代理自动配置
//设置@EnableAspectJAutoProxy(exposeProxy=true)表示通过aop框架暴露该代理对象，aopContext能够访问.
@EnableAspectJAutoProxy(exposeProxy = true)
public class RateLimiterAspect {
  private final Map<Integer, RateLimiter> _rateLimiterServices = new HashMap<>();

  private final ApplicationContext applicationContext;

  public RateLimiterAspect(@Autowired
  ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  /**
   * 选择一个限流器进行执行
   */
  private RateLimiter selectRateLimiterService(int permitsPerSecond) {
    RateLimiter limiter = _rateLimiterServices.get(permitsPerSecond);
    if (limiter == null) {
      synchronized (_rateLimiterServices) {
        limiter = _rateLimiterServices.get(permitsPerSecond);
        if (limiter == null) {
          limiter = RateLimiter.create(permitsPerSecond);
          _rateLimiterServices.put(permitsPerSecond, limiter);
        }
      }
    }

    return limiter;
  }

  @PreDestroy
  public void clear() {
    _rateLimiterServices.clear();
  }

  /**
   * 将会切 被RateLimit注解标记的方法
   */
  @Pointcut("@annotation(org.wjw.starter.limiter.RateLimit)")
  public void rateLimitPointCut() {
  }

  @Around("rateLimitPointCut()")
  public Object runAround(ProceedingJoinPoint point) throws Throwable {
    MethodSignature signature = (MethodSignature) point.getSignature();
    RateLimit       rateLimit = signature.getMethod().getAnnotation(RateLimit.class);
    if (rateLimit != null) {
      int permitsPerSecond = rateLimit.permitsPerSecond();
      int permits          = rateLimit.permits();

      RateLimiter rateLimiterObj = selectRateLimiterService(permitsPerSecond);
      rateLimiterObj.acquire(permits);
    }

    return point.proceed();
  }
}
