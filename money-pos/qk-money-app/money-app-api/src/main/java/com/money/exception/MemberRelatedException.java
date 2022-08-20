package com.money.exception;


import com.money.common.exception.BaseException;
import com.money.common.response.IStatus;
import com.money.common.response.Status;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 成员相关异常
 * @createTime : 2022-04-02 23:04:59
 */
public class MemberRelatedException extends BaseException {
    private static final long serialVersionUID = 5741823200494837396L;

    public MemberRelatedException(String message) {
        super(new Status(10000, message));
    }

    public MemberRelatedException(IStatus errorStatus) {
        super(errorStatus);
    }
}
