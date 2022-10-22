package com.money.common.dto;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NonNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 排序请求接口
 * @createTime : 2022-10-01 11:19:58
 */
public interface ISortRequest {

    /**
     * 获取排序值 例如：createTime,desc;id,asc;
     *
     * @return {@link String}
     */
    String getSort();

    /**
     * 得到排序顺序
     *
     * @return {@link List}<{@link QueryRequest.SortingOrder}>
     */
    @JsonIgnore
    default List<QueryRequest.SortingOrder> getSortingOrder() {
        String sort = this.getSort();
        if (StrUtil.isNotBlank(sort)) {
            return Arrays.stream(sort.split(";")).map(propWithOrderByStr -> {
                String[] propWithOrderBy = propWithOrderByStr.split(",");
                QueryRequest.SortingOrder sortingOrder = new QueryRequest.SortingOrder();
                // 转下划线
                sortingOrder.setProp(StrUtil.toUnderlineCase(propWithOrderBy[0]));
                sortingOrder.setOrderBy(propWithOrderBy[1].toUpperCase());
                sortingOrder.setAsc("asc".equals(propWithOrderBy[1]));
                return sortingOrder;
            }).collect(Collectors.toList());
        }
        return null;
    }

    /**
     * 获取order by语句
     *
     * @return {@link String}
     */
    @NonNull
    @JsonIgnore
    default String getOrderBySql() {
        List<QueryRequest.SortingOrder> sortingOrderList = this.getSortingOrder();
        if (CollectionUtil.isNotEmpty(sortingOrderList)) {
            StringBuilder sb = new StringBuilder("ORDER BY");
            sortingOrderList.forEach(sortingOrder -> {
                sb.append(" ").append(sortingOrder.getProp()).append(" ").append(sortingOrder.getOrderBy()).append(",");
            });
            return sb.substring(0, sb.length() - 1);
        }
        return "";
    }

    @Data
    class SortingOrder {

        /**
         * 排序参数
         */
        private String prop;

        /**
         * 排序
         */
        private String orderBy;

        /**
         * 是asc
         */
        private boolean asc;

    }
}
