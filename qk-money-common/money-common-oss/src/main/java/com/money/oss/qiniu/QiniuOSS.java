package com.money.oss.qiniu;

import cn.hutool.json.JSONUtil;
import com.money.oss.core.OSSInterface;
import com.money.oss.exception.DeleteFailedException;
import com.money.oss.exception.UploadFailedException;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.DownloadUrl;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 七牛云对象存储服务
 * @createTime : 2022-01-01 16:47:20
 */
@Slf4j
@RequiredArgsConstructor
public class QiniuOSS implements OSSInterface {

    private final QiniuOSSConfig config;

    /**
     * 获取上传令牌
     *
     * @return {@link String}
     */
    private String getUploadToken() {
        Auth auth = Auth.create(config.getAccessKey(), config.getSecretKey());
        return auth.uploadToken(config.getBucket(), null, config.getTokenExpire(), new StringMap(config.getPolicy()));
    }

    @Override
    public String upload(@NonNull MultipartFile file, @NonNull String uri) throws UploadFailedException {
        String originalFilename = file.getOriginalFilename();
        // 构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(QiniuRegion.getRegion(config.getRegion()));
        // 配置参数，参考类注释
        cfg.useHttpsDomains = false;
        // 构造上传管理者
        UploadManager uploadManager = new UploadManager(cfg);
        // 获取上传令牌
        log.info("【七牛云OSS】获取上传令牌。");
        String uploadToken = this.getUploadToken();
        log.info("【七牛云OSS】获取上传令牌成功，令牌 {}。", uploadToken);
        try {
            // 上传
            log.info("【七牛云OSS】文件 {} 上传就绪，准备上传至 {} 空间。", originalFilename, config.getBucket());
            Response response = uploadManager.put(file.getInputStream(), uri, uploadToken, null, null);
            DefaultPutRet ret = JSONUtil.toBean(response.bodyString(), DefaultPutRet.class);
            log.info("【七牛云OSS】文件 {} 上传成功！[{}]", originalFilename, ret.key);
            return ret.key;
        } catch (IOException e) {
            log.error("【七牛云OSS】文件 {} 上传失败。", originalFilename, e);
            throw new UploadFailedException();
        }
    }

    @Override
    public void delete(@NonNull String uri) throws DeleteFailedException {
        log.info("【七牛云OSS】删除文件 {}", uri);
        // 构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(QiniuRegion.getRegion(config.getRegion()));
        // 配置参数，参考类注释
        cfg.useHttpsDomains = false;
        // 构造空间管理者
        BucketManager bucketManager = new BucketManager(Auth.create(config.getAccessKey(), config.getSecretKey()), cfg);
        try {
            bucketManager.delete(config.getBucket(), uri);
        } catch (QiniuException ex) {
            log.error("【七牛云OSS】文件 {} 删除失败", uri);
            throw new DeleteFailedException();
        }
    }

    /**
     * 获取下载地址
     *
     * @param uri uri
     * @return {@link String}
     */
    public String getDownloadUrl(String uri) {
        try {
            return new DownloadUrl(config.getDomain(), config.isUseHttps(), uri).buildURL();
        } catch (QiniuException e) {
            log.error("【七牛云OSS】获取 {} 下载地址失败。", uri, e);
            throw new RuntimeException(e);
        }
    }
}
