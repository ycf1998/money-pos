package com.money.oss.core;

import lombok.Getter;

import java.util.StringJoiner;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 文件夹路径
 * @createTime : 2022-01-01 16:47:50
 */
@Getter
public class FolderPath {

    private String folderPath = "";

    private FolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    public static FolderPathBuilder builder() {
        return new FolderPathBuilder();
    }

    /**
     * 构造器
     */
    public static class FolderPathBuilder {

        private final StringJoiner path = new StringJoiner("/", "", "/");

        public FolderPathBuilder cd(String path) {
            if (path.contains("/") || path.contains("\\")) {
                throw new RuntimeException("不需要添加文件分隔符");
            }
            this.path.add(path);
            return this;
        }

        public FolderPath build() {
            return new FolderPath(path.toString());
        }

    }

    public static void main(String[] args) {
        FolderPath image = FolderPath.builder().cd("image").cd("user01").build();
        String folderPath = image.getFolderPath();
        System.out.println(folderPath);
    }
}
