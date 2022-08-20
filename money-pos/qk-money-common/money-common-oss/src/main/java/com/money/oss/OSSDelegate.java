package com.money.oss;

import cn.hutool.core.util.StrUtil;
import com.money.oss.core.FileNameStrategy;
import com.money.oss.core.FolderPath;
import com.money.oss.core.OSSInterface;
import com.money.oss.exception.DeleteFailedException;
import com.money.oss.exception.UploadFailedException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 对象存储服务委托
 * @createTime : 2021-12-28 22:04:19
 */
@Slf4j
@RequiredArgsConstructor
public class OSSDelegate<T extends OSSInterface> {

    private final T oss;

    /**
     * 获取供应商
     *
     * @return {@link T}
     */
    public T getProvider() {
        return oss;
    }

    /**
     * 默认上传
     *
     * @param file 文件
     * @return {@link String}
     */
    public String upload(@NonNull MultipartFile file) {
        return upload(file, FolderPath.builder().build(), FileNameStrategy.ORIGINAL_WITH_TIMESTAMP);
    }

    /**
     * 上传
     *
     * @param file             文件
     * @param fileNameStrategy 文件名称策略 （原文件名, 文件类型）
     * @return {@link String}
     */
    public String upload(@NonNull MultipartFile file, FileNameStrategy fileNameStrategy) {
        return upload(file, FolderPath.builder().build(), fileNameStrategy);
    }

    /**
     * 上传到指定文件夹内
     *
     * @param file             文件
     * @param folderPath       文件夹路径（在oss.properties的目标空间下）
     * @param fileNameStrategy 文件名称策略（原文件名, 文件类型）
     * @return {@link String}
     */
    public String upload(@NonNull MultipartFile file, @NonNull FolderPath folderPath, FileNameStrategy fileNameStrategy) {
        String originalFilename = file.getOriginalFilename();
        String rawFileName = StrUtil.subBefore(originalFilename, ".", true);
        String fileType = StrUtil.subAfter(originalFilename, ".", true);
        String uri;
        try {
            uri = oss.upload(file, folderPath.getFolderPath() + fileNameStrategy.apply(rawFileName, fileType));
        } catch (UploadFailedException e) {
            throw new RuntimeException(e);
        }
        return uri;
    }


    /**
     * 删除
     *
     * @param uri uri
     */
    public boolean delete(String uri) {
        if (StrUtil.isNotBlank(uri)) {
            try {
                oss.delete(uri);
            } catch (DeleteFailedException e) {
                return false;
            }
        }
        return true;
    }
}
