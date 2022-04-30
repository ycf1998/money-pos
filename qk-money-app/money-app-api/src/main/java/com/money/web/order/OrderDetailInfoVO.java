package com.money.web.order;

import com.money.web.member.MemberVO;
import lombok.Data;

import java.util.List;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 订单详细信息VO
 * @createTime : 2022-04-23 18:22:31
 */
@Data
public class OrderDetailInfoVO {

    /**
     * 会员
     */
    private MemberVO member;

    /**
     * 订单
     */
    private OrderVO order;

    /**
     * 订单详情
     */
    private List<OrderDetailVO> orderDetail;
}
