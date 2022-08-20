package com.money.web.member;

import com.money.common.dto.QueryRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class MemberQueryDTO extends QueryRequest {
    private static final long serialVersionUID = -916611678532903199L;

    /**
     * 卡号
     */
    private String code;

    /**
     * 会员名称
     */
    private String name;

    /**
     * 会员类型
     */
    private String type;

    /**
     * 联系电话
     */
    private String phone;
}
