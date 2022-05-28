package com.money.exception;

import com.money.common.exception.BaseException;
import com.money.constant.ErrorStatus;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 用户相关异常
 * @createTime : 2022-03-05 00:56:38
 */
public class UserRelatedException extends BaseException {

    private static final long serialVersionUID = -727927190481020688L;

    public UserRelatedException(ErrorStatus errorStatus) {
        super(errorStatus);
    }

}
