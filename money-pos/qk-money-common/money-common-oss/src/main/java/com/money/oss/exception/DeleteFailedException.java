package com.money.oss.exception;

import java.io.Serial;

/**
 * 删除失败异常
 *
 * @author : money
 * @since : 1.0.0
 */
public class DeleteFailedException extends Exception {

    @Serial
    private static final long serialVersionUID = 2752845463412351325L;

    public DeleteFailedException(Throwable cause) {
        super(cause);
    }
}
