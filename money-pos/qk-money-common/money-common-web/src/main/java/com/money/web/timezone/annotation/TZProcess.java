package com.money.web.timezone.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 时区处理
 * <pre>
 * 标注在类上表示类里的方法需要进行时区处理
 * </pre>
 *
 * @author : money
 * @since : 1.0.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TZProcess {
}
