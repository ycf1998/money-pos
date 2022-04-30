package com.money.oss.core;

import com.money.oss.exception.DeleteFailedException;
import com.money.oss.exception.UploadFailedException;
import lombok.NonNull;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 对象存储服务接口
 * @createTime : 2021-12-28 21:48:49
 */
public interface OSSInterface {

    /**
     * 上传
     *
     * @param file        文件
     * @param uri 文件地址
     * @return {@link String}
     */
    String upload(@NonNull MultipartFile file, @NonNull String uri) throws UploadFailedException;

    /**
     * 删除
     *
     * @param uri uri
     */
    void delete(@NonNull String uri) throws DeleteFailedException;

}
