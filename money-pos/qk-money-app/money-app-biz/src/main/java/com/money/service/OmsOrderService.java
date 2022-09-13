package com.money.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.money.common.vo.PageVO;
import com.money.entity.OmsOrder;
import com.money.dto.order.*;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author money
 * @since 2022-04-10
 */
public interface OmsOrderService extends IService<OmsOrder> {

    /**
     * 查询订单列表
     *
     * @param queryDTO 查询dto
     * @return {@link PageVO}<{@link OrderVO}>
     */
    PageVO<OrderVO> list(OrderQueryDTO queryDTO);

    /**
     * 获取订单详情
     *
     * @param id id
     * @return {@link OrderDetailInfoVO}
     */
    OrderDetailInfoVO getDetail(Long id);

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

    /**
     * 订单统计
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return {@link OrderCountVO}
     */
    OrderCountVO countOrderAndSales(LocalDateTime startTime, LocalDateTime endTime);
}
