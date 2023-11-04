package com.money.dto.Pos;

import com.money.dto.OmsOrderDetail.OmsOrderDetailDTO;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 结算dto
 * @createTime : 2022-04-19 23:58:51
 */
@Data
public class SettleAccountsDTO {

    /**
     * 会员
     */
    private Long member;

    /**
     * 订单详情
     */
    @NotEmpty
    private List<OmsOrderDetailDTO> orderDetail;
}
