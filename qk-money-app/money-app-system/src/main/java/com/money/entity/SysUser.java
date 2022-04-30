package com.money.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.money.mb.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author money
 * @since 2022-03-04
 */
@Getter
@Setter
@TableName("sys_user")
public class SysUser extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "密码")
    private String password;

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

    @Schema(description = "初次登录：0-不是；1-是")
    private Boolean initLogin;

    @Schema(description = "最后登录时间")
    private LocalDateTime lastTime;

    @Schema(description = "租户id")
    private Long tenantId;

    private Integer sort;


}
