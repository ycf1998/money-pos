package com.money.web.util;

import cn.hutool.extra.spring.SpringUtil;
import lombok.experimental.UtilityClass;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

/**
 * 校验工具
 *
 * @author : money
 * @since : 1.0.0
 */
@UtilityClass
public class ValidationUtil {

    /**
     * 校验并抛出异常
     *
     * @param t      校验对象
     * @param groups 组
     */
    public <T> void validateThrow(T t, Class<?>... groups) {
        Validator validator = SpringUtil.getBean(ValidatorFactory.class).getValidator();
        Set<ConstraintViolation<T>> validationErrors = validator.validate(t, groups);
        if (!validationErrors.isEmpty()) {
            throw new ConstraintViolationException(validationErrors);
        }
    }

    /**
     * 校验
     *
     * @param t      校验对象
     * @param groups 组
     * @return {@link ConstraintViolation }<{@link T }> 首个异常，无异常返回 null
     */
    public <T> ConstraintViolation<T> validate(T t, Class<?>... groups) {
        Validator validator = SpringUtil.getBean(ValidatorFactory.class).getValidator();
        Set<ConstraintViolation<T>> validationErrors = validator.validate(t, groups);
        return validationErrors.stream().findFirst().orElse(null);
    }

}
