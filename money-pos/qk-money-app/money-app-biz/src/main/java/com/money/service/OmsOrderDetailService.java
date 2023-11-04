package com.money.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.money.entity.OmsOrderDetail;

import java.util.List;

/**
 * <p>
 * 订单明细表 服务类
 * </p>
 *
 * @author money
 * @since 2023-02-27
 */
public interface OmsOrderDetailService extends IService<OmsOrderDetail> {

    /**
     * 获取订单详情
     *
     * @param orderNo 订单号
     * @return {@link List}<{@link OmsOrderDetail}>
     */
    List<OmsOrderDetail> listByOrderNo(String orderNo);

}
