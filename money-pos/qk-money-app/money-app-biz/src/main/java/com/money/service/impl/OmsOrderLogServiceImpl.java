package com.money.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.money.dto.OmsOrderLog.OmsOrderLogVO;
import com.money.entity.OmsOrderLog;
import com.money.mapper.OmsOrderLogMapper;
import com.money.service.OmsOrderLogService;
import com.money.util.VOUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 订单操作日志 服务实现类
 * </p>
 *
 * @author money
 * @since 2023-02-27
 */
@Service
@RequiredArgsConstructor
public class OmsOrderLogServiceImpl extends ServiceImpl<OmsOrderLogMapper, OmsOrderLog> implements OmsOrderLogService {

    @Override
    public List<OmsOrderLogVO> list(Long orderId) {
        return VOUtil.toVO(this.lambdaQuery().eq(OmsOrderLog::getOrderId, orderId).orderByDesc(OmsOrderLog::getCreateTime).list(), OmsOrderLogVO.class);
    }
}
