package com.money.common.timezone.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 时区处理，标注在类上表示类里的方法需要进行时区处理
 * @createTime : 2022-05-13 21:58:46
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TZProcess {
}
