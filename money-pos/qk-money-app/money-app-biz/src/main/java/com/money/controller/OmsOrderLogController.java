package com.money.controller;

import com.money.dto.OmsOrderLog.OmsOrderLogVO;
import com.money.service.OmsOrderLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
 * @since 2023-02-27
 */
@Tag(name = "omsOrderLog", description = "订单操作日志")
@RestController
@RequestMapping("/oms/orderLog")
@RequiredArgsConstructor
public class OmsOrderLogController {

    private final OmsOrderLogService omsOrderLogService;

    @Operation(summary = "查询")
    @PreAuthorize("@rbac.hasPermission('omsOrder:list')")
    @GetMapping
    public List<OmsOrderLogVO> list(@RequestParam Long id) {
        return omsOrderLogService.list(id);
    }
}
