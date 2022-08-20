package com.money.controller;

import com.money.service.OmsOrderLogService;
import com.money.web.orderlog.OrderLogVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 订单操作日志 前端控制器
 * </p>
 *
 * @author money
 * @since 2022-04-26
 */
@RestController
@RequestMapping("/orderLog")
@RequiredArgsConstructor
public class OmsOrderLogController {

    private final OmsOrderLogService omsOrderLogService;

    @GetMapping
    @PreAuthorize("@rbac.hasPermission('order:list')")
    public List<OrderLogVO> list(@RequestParam Long id) {
        return omsOrderLogService.list(id);
    }
}
