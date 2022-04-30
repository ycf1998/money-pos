package com.money.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.money.entity.OmsOrderLog;
import com.money.web.orderlog.OrderLogVO;

import java.util.List;

/**
 * <p>
 * 订单操作日志 服务类
 * </p>
 *
 * @author money
 * @since 2022-04-26
 */
public interface OmsOrderLogService extends IService<OmsOrderLog> {

    /**
     * 列表
     *
     * @param id id
     * @return {@link List}<{@link OrderLogVO}>
     */
    List<OrderLogVO> list(Long id);

}
