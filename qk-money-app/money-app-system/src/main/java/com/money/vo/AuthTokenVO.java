package com.money.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "身份验证令牌VO")
public class AuthTokenVO implements Serializable {

    private static final long serialVersionUID = 8004460365077526558L;
    /**
     * 访问令牌
     */
    @Schema(description = "访问令牌")
    private String accessToken;

    /**
     * 令牌类型
     */
    @Schema(description = "令牌类型")
    private String tokenType;

    /**
     * 刷新令牌
     */
    @Schema(description = "刷新令牌")
    private String refreshToken;

    /**
     * access token过期时间
     */
    @Schema(description = "access token过期时间")
    private long ttl;

    /**
     * refresh token过期时间
     */
    @Schema(description = "refresh token过期时间")
    private long refreshTtl;

}
