package com.money.exception;

import com.money.common.exception.BaseException;
import com.money.constant.ErrorStatus;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 角色相关异常
 * @createTime : 2022-03-06 11:22:16
 */
public class RoleRelatedException extends BaseException {

    private static final long serialVersionUID = -7020738738671919966L;

    public RoleRelatedException(ErrorStatus errorStatus) {
        super(errorStatus);
    }

}
