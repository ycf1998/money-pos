package com.money.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Vue 路由器 VO
 *
 * @author : money
 * @since : 1.0.0
 */
@Data
public class VueRouterVO implements Serializable {
    private static final long serialVersionUID = -7741958692564536459L;

    @JsonIgnore
    private Long id;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String path;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String redirect;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String name;

    private Meta meta;

    private Boolean hidden;

    private Boolean iframe;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String component;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<VueRouterVO> children;

    @Data
    public static class Meta {
        private String title;

        private String icon;
    }

}
