package com.money.exception;

import com.money.common.exception.BaseException;
import com.money.constant.ErrorStatus;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 权限相关异常
 * @createTime : 2022-03-26 10:49:59
 */
public class PermissionRelatedException extends BaseException {

    private static final long serialVersionUID = -727927190481020688L;

    public PermissionRelatedException(ErrorStatus errorStatus) {
        super(errorStatus);
    }

}
