package com.money.web.dto;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.money.web.util.ValidationUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.validation.ValidationException;
import javax.validation.constraints.Pattern;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 排序请求接口
 *
 * @author : money
 * @since : 1.0.0
 */
public interface ISortRequest {

    /**
     * 获取排序值 例如：createTime,desc;id,asc
     *
     * @return {@link String}
     */
    String getOrderBy();

    /**
     * 支持排序的字段映射
     *
     * @return {@link Map }<{@link String 排序参数名}, {@link String 用于拼接 SQL 的字段名}>
     */
    Map<String, String> sortKeyMap();

    /**
     * 获取排序选项
     *
     * @return {@link List}<{@link SortOption}>
     */
    @NonNull
    @JsonIgnore
    default Set<SortOption> getSortOptions() {
        String orderByStr = this.getOrderBy();
        Map<String, String> sortKeyMap = this.sortKeyMap();
        if (StrUtil.isBlank(orderByStr)) {
            return Collections.emptySet();
        }
        if (CollUtil.isEmpty(sortKeyMap)) {
            throw new ValidationException("Invalid order by expression");
        }
        return StrUtil.split(orderByStr, ";", true, true)
                .stream().map(keyWithOrderStr -> {
                    String[] keyWithOrder = keyWithOrderStr.split(",");
                    String key = sortKeyMap.getOrDefault(keyWithOrder[0], "#ERR");
                    String order = keyWithOrder[1].toUpperCase();
                    SortOption sortOption = new SortOption(key, order);
                    ValidationUtil.validateThrow(sortOption);
                    return sortOption;
                }).collect(Collectors.toSet());
    }

    /**
     * 获取 order by 语句
     *
     * @return {@link String}
     */
    @NonNull
    @JsonIgnore
    default String getOrderBySql() {
        return this.getSortOptions().stream().map(SortOption::toString)
                .collect(Collectors.joining(", ", "ORDER BY ", ""));
    }

    @Data
    @RequiredArgsConstructor
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    class SortOption {

        /**
         * 排序字段
         */
        @EqualsAndHashCode.Include
        @Pattern(regexp = "^(?!#ERR$).*$", message = "invalid sort key")
        private final String sortKey;

        /**
         * 排序顺序
         */
        @Pattern(regexp = "^(ASC|DESC)$", message = "invalid sort order")
        private final String sortOrder;

        @Override
        public String toString() {
            return sortKey + " " + sortOrder;
        }

    }
}