package com.money.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.money.entity.OmsOrderLog;
import com.money.mapper.OmsOrderLogMapper;
import com.money.service.OmsOrderLogService;
import com.money.util.VOUtil;
import com.money.web.orderlog.OrderLogVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 订单操作日志 服务实现类
 * </p>
 *
 * @author money
 * @since 2022-04-26
 */
@Service
@RequiredArgsConstructor
public class OmsOrderLogServiceImpl extends ServiceImpl<OmsOrderLogMapper, OmsOrderLog> implements OmsOrderLogService {

    @Override
    public List<OrderLogVO> list(Long id) {
        return VOUtil.toVO(this.lambdaQuery().eq(OmsOrderLog::getOrderId, id).orderByDesc(OmsOrderLog::getCreateTime).list(), OrderLogVO.class);
    }
}
