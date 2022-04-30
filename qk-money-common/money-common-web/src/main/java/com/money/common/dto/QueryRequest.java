package com.money.common.dto;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NonNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 列表查询请求
 * @createTime : 2022-03-05 12:19:01
 */
@Data
public class QueryRequest extends PageRequest {

    private static final long serialVersionUID = 556891735255292270L;

    @Schema(description = "排序 createTime,desc;id,asc;")
    private String sort;

    /**
     * 得到排序顺序
     *
     * @return {@link List}<{@link SortingOrder}>
     */
    @JsonIgnore
    public List<SortingOrder> getSortingOrder() {
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
    public String getOrderBySql() {
        List<SortingOrder> sortingOrderList = getSortingOrder();
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
    public static class SortingOrder {

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
