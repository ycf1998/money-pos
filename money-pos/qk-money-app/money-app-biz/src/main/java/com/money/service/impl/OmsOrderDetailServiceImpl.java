package com.money.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.money.entity.OmsOrderDetail;
import com.money.mapper.OmsOrderDetailMapper;
import com.money.service.OmsOrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 订单明细表 服务实现类
 * </p>
 *
 * @author money
 * @since 2023-02-27
 */
@Service
@RequiredArgsConstructor
public class OmsOrderDetailServiceImpl extends ServiceImpl<OmsOrderDetailMapper, OmsOrderDetail> implements OmsOrderDetailService {

    @Override
    public List<OmsOrderDetail> listByOrderNo(String orderNo) {
        return this.lambdaQuery().eq(OmsOrderDetail::getOrderNo, orderNo).list();
    }
}
