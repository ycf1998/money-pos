package com.money.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.money.web.vo.PageVO;
import com.money.dto.OmsOrder.*;
import com.money.entity.OmsOrder;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author money
 * @since 2023-02-27
 */
public interface OmsOrderService extends IService<OmsOrder> {

    PageVO<OmsOrderVO> list(OmsOrderQueryDTO queryDTO);

    /**
     * 订单统计
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return {@link OrderCountVO}
     */
    OrderCountVO countOrderAndSales(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取订单详情
     *
     * @param id id
     * @return {@link OrderDetailVO}
     */
    OrderDetailVO getOrderDetail(Long id);

    /**
     * 退单
     *
     * @param ids id
     */
    void returnOrder(Set<Long> ids);

    /**
     * 退货
     *
     * @param returnGoodsDTO 返回商品dto
     */
    void returnGoods(ReturnGoodsDTO returnGoodsDTO);

}
