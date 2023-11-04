package com.money.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.money.entity.OmsOrderLog;

import java.util.List;

/**
 * <p>
 * 订单操作日志 服务类
 * </p>
 *
 * @author money
 * @since 2023-02-27
 */
public interface OmsOrderLogService extends IService<OmsOrderLog> {

    /**
     * 根据订单id查询
     *
     * @param orderId 订单id
     * @return {@link List}<{@link OmsOrderLog}>
     */
    List<OmsOrderLog> listByOrderId(Long orderId);

}
