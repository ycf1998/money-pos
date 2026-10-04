package com.money.oss.core;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.StringJoiner;

/**
 * 文件夹路径
 *
 * @author : money
 * @since : 1.0.0
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FolderPath {

    private String folderPath;

    public static FolderPathBuilder builder() {
        return new FolderPathBuilder();
    }

    /**
     * 构造器
     */
    public static class FolderPathBuilder {

        private final StringJoiner path = new StringJoiner("/", "", "/").setEmptyValue("");

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
}
