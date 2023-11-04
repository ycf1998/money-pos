package com.money.vo;

import com.money.entity.SysRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(description = "系统用户VO")
public class SysUserVO implements Serializable {
    private static final long serialVersionUID = -7742761485313399985L;

    private Long id;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "手机号码")
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "可用状态：0-禁用；1-启用")
    private Boolean enabled;

    @Schema(description = "最后登录时间")
    private LocalDateTime lastTime;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "角色信息")
    private List<SysRole> roles;
}
