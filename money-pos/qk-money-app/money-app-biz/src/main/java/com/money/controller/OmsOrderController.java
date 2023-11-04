package com.money.controller;

import com.money.common.vo.PageVO;
import com.money.dto.OmsOrder.OmsOrderQueryDTO;
import com.money.dto.OmsOrder.OmsOrderVO;
import com.money.dto.OmsOrder.OrderCountVO;
import com.money.dto.OmsOrder.ReturnGoodsDTO;
import com.money.dto.OmsOrder.OrderDetailVO;
import com.money.service.OmsOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Set;
/**
 * <p>
 * 订单表 前端控制器
 * </p>
 *
 * @author money
 * @since 2023-02-27
 */
@Tag(name = "omsOrder", description = "订单表")
@RestController
@RequestMapping("/oms/order")
@RequiredArgsConstructor
public class OmsOrderController {

    private final OmsOrderService omsOrderService;

    @Operation(summary = "分页查询")
    @GetMapping
    @PreAuthorize("@rbac.hasPermission('omsOrder:list')")
    public PageVO<OmsOrderVO> list(@Validated OmsOrderQueryDTO queryDTO) {
        return omsOrderService.list(queryDTO);
    }

    @Operation(summary = "订单统计")
    @GetMapping("/count")
    @PreAuthorize("@rbac.hasPermission('omsOrder:list')")
    public OrderCountVO orderCount(LocalDateTime startTime, LocalDateTime endTime) {
        return omsOrderService.countOrderAndSales(startTime, endTime);
    }

    @Operation(summary = "订单详情")
    @GetMapping("/detail")
    @PreAuthorize("@rbac.hasPermission('omsOrder:list')")
    public OrderDetailVO orderDetail(@RequestParam Long id) {
        return omsOrderService.getOrderDetail(id);
    }

    @Operation(summary = "退单")
    @DeleteMapping("/returnOrder")
    @PreAuthorize("@rbac.hasPermission('omsOrder:edit')")
    public void returnOrder(@RequestBody Set<Long> ids) {
        omsOrderService.returnOrder(ids);
    }

    @Operation(summary = "退货")
    @DeleteMapping("/returnGoods")
    @PreAuthorize("@rbac.hasPermission('omsOrder:edit')")
    public void returnGoods(@Valid @RequestBody ReturnGoodsDTO returnGoodsDTO) {
        omsOrderService.returnGoods(returnGoodsDTO);
    }

}
