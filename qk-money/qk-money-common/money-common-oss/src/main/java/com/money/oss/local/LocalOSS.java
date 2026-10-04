package com.money.oss.local;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.money.oss.core.OSSInterface;
import com.money.oss.exception.DeleteFailedException;
import com.money.oss.exception.UploadFailedException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

/**
 * 本地对象存储服务
 *
 * @author : money
 * @since : 1.0.0
 */
@Slf4j
@RequiredArgsConstructor
public class LocalOSS implements OSSInterface {

    private final LocalOSSConfig config;

    @Override
    public String upload(@NonNull MultipartFile file, @NonNull String uri) throws UploadFailedException {
        String originalFilename = file.getOriginalFilename();
        String finalPath = StrUtil.appendIfMissing(config.getBucket(), "/") + uri;
        log.info("【本地 OSS】文件 {} 上传就绪，准备上传至 {}。", originalFilename, finalPath);
        File dest = new File(finalPath);
        FileUtil.mkdir(dest.getParentFile());
        try {
            file.transferTo(dest);
            log.info("【本地 OSS】文件 {} 上传成功！[{}]", originalFilename, uri);
        } catch (IOException e) {
            log.error("【本地 OSS】文件 {} 上传失败！", originalFilename, e);
            throw new UploadFailedException(e);
        }
        return uri;
    }

    @Override
    public void delete(@NonNull String uri) throws DeleteFailedException {
        // 外部链接不需要本地删除（判断是否包含 ://）
        if (uri.contains("://")) {
            log.info("【本地 OSS】跳过外部链接删除：{}", uri);
            return;
        }
        log.info("【本地 OSS】删除文件 {}", uri);
        try {
            String finalPath = StrUtil.appendIfMissing(config.getBucket(), "/") + uri;
            Files.deleteIfExists(Paths.get(finalPath));
        } catch (IOException | InvalidPathException e) {
            log.error("【本地 OSS】文件 {} 删除失败", uri, e);
            throw new DeleteFailedException(e);
        }
    }

}
