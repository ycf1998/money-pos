package com.money.dto.OmsOrder;

import com.money.dto.OmsOrderDetail.OmsOrderDetailVO;
import com.money.dto.UmsMember.UmsMemberVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
* <p>
* 订单明细表
* </p>
*
* @author money
* @since 2023-02-27
*/
@Data
@Schema(description = "订单明细表")
public class OrderDetailVO {

    /**
     * 会员
     */
    private UmsMemberVO member;

    /**
     * 订单
     */
    private OmsOrderVO order;

    /**
     * 订单详情
     */
    private List<OmsOrderDetailVO> orderDetail;

}
