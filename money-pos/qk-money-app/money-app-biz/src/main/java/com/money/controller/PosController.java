package com.money.controller;


import com.money.service.PosService;
import com.money.web.order.OrderVO;
import com.money.web.pos.PosGoodsVO;
import com.money.web.pos.PosMemberVO;
import com.money.web.pos.SettleAccountsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author : money
 * @version : 1.0.0
 * @description : pos机控制器
 * @createTime : 2022-04-14 22:05:13
 */
@RestController
@RequestMapping("/pos")
@RequiredArgsConstructor
public class PosController {

    private final PosService posService;

    @GetMapping("/goods")
    public List<PosGoodsVO> listGoods(String barcode) {
        return posService.listGoods(barcode);
    }

    @GetMapping("/members")
    public List<PosMemberVO> listMember(String member) {
        return posService.listMember(member);
    }

    @PostMapping("settleAccounts")
    public OrderVO settleAccounts(@Validated @RequestBody SettleAccountsDTO settleAccountsDTO) {
        return posService.settleAccounts(settleAccountsDTO);
    }
}
