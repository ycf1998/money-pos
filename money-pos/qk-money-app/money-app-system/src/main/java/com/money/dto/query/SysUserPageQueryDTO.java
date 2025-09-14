package com.money.dto.query;

import com.money.web.dto.PageQueryRequest;
import com.money.web.util.MoneyCommUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 系统用户查询dto
 * @createTime : 2022-03-05 12:21:04
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysUserPageQueryDTO extends PageQueryRequest {

    @Schema(description = "用户名/昵称")
    private String name;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "可用状态")
    private Boolean enabled;

    @Override
    public Map<String, String> sortKeyMap() {
        return MoneyCommUtil.sortFieldMap("createTime", "updateTime", "lastTime");
    }
}
