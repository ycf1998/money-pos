package com.money.controller;


import com.money.common.vo.PageVO;
import com.money.service.OmsOrderService;
import com.money.dto.order.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Set;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OmsOrderController {

    private final OmsOrderService omsOrderService;

    @GetMapping
    @PreAuthorize("@rbac.hasPermission('order:list')")
    public PageVO<OrderVO> list(@Validated OrderQueryDTO queryDTO) {
        return omsOrderService.list(queryDTO);
    }

    @GetMapping("/count")
    @PreAuthorize("@rbac.hasPermission('order:list')")
    public OrderCountVO orderCount(LocalDateTime startTime, LocalDateTime endTime) {
        return omsOrderService.countOrderAndSales(startTime, endTime);
    }

    @GetMapping("/detail")
    @PreAuthorize("@rbac.hasPermission('order:list')")
    public OrderDetailInfoVO getDetail(@RequestParam Long id) {
        return omsOrderService.getDetail(id);
    }

    @DeleteMapping("/returnOrder")
    @PreAuthorize("@rbac.hasPermission('order:edit')")
    public void returnOrder(@RequestBody Set<Long> ids) {
        omsOrderService.returnOrder(ids);
    }

    @DeleteMapping("/returnGoods")
    @PreAuthorize("@rbac.hasPermission('order:edit')")
    public void returnGoods(@Valid @RequestBody ReturnGoodsDTO returnGoodsDTO) {
        omsOrderService.returnGoods(returnGoodsDTO);
    }
}
