package com.hongcj.config.common;

import java.lang.annotation.*;

/**
 * @author: Hongcj
 * @Date: 2022/06/01 4:28 下午
 * @Description：动态刷新注解
 */
@Target(value = {ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RefreshScope {
}
