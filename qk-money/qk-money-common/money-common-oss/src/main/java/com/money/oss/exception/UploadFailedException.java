package com.money.oss.exception;

import java.io.Serial;

/**
 * 上传失败异常
 *
 * @author : money
 * @since : 1.0.0
 */
public class UploadFailedException extends Exception {

    @Serial
    private static final long serialVersionUID = -1262418407208247745L;

    public UploadFailedException(Throwable cause) {
        super(cause);
    }
}
