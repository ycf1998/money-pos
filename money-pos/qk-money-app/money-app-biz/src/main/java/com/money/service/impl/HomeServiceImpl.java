package com.money.service.impl;

import com.money.dto.Home.HomeCountVO;
import com.money.dto.OmsOrder.OrderCountVO;
import com.money.service.GmsGoodsService;
import com.money.service.HomeService;
import com.money.service.OmsOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

@Service
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService {

    private final OmsOrderService omsOrderService;
    private final GmsGoodsService gmsGoodsService;

    @Override
    public HomeCountVO homeCount() {
        HomeCountVO homeCountVO = new HomeCountVO();
        // 今日统计
        LocalDateTime startTime = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endTime = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(0);
        OrderCountVO todayOrderCount = omsOrderService.countOrderAndSales(startTime, endTime);
        homeCountVO.setToday(todayOrderCount);
        // 本月统计
        startTime = startTime.with(TemporalAdjusters.firstDayOfMonth());
        endTime = startTime.with(TemporalAdjusters.lastDayOfMonth());
        OrderCountVO monthOrderCount = omsOrderService.countOrderAndSales(startTime, endTime);
        homeCountVO.setMonth(monthOrderCount);
        // 本年统计
        startTime = startTime.with(TemporalAdjusters.firstDayOfYear());
        endTime = startTime.with(TemporalAdjusters.lastDayOfYear());
        OrderCountVO yearOrderCount = omsOrderService.countOrderAndSales(startTime, endTime);
        homeCountVO.setYear(yearOrderCount);
        // 总计
        OrderCountVO allOrderCount = omsOrderService.countOrderAndSales(null, null);
        homeCountVO.setTotal(allOrderCount);
        // 库存价值
        homeCountVO.setInventoryValue(gmsGoodsService.getCurrentStockValue());
        return homeCountVO;
    }
}
