/*
 * author: @wjw
 * date:   2021年10月18日 下午5:39:25
 * note: 
 */
package org.wjw.starter.limiter;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义限流注解.
 */
@Inherited
@Documented
@Target({ ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {
  
  /**
   * 每秒允许的许可数(不能为空).
   *
   * @return 每秒允许的许可数
   */
  int permitsPerSecond();
  
  /**
   * Permits, 获得许可证的数量,默认是1个.
   *
   * @return 获得的许可证的数量
   */
  int permits() default 1;
}
