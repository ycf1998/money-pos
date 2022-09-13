package com.money.service;

import com.money.dto.order.OrderVO;
import com.money.dto.pos.PosGoodsVO;
import com.money.dto.pos.PosMemberVO;
import com.money.dto.pos.SettleAccountsDTO;

import java.util.List;

/**
 * @author : money
 * @version : 1.0.0
 * @description : pos服务
 * @createTime : 2022-04-14 22:08:07
 */
public interface PosService {

    /**
     * 查询商品
     *
     * @param barcode 条形码
     * @return {@link List}<{@link PosGoodsVO}>
     */
    List<PosGoodsVO> listGoods(String barcode);

    /**
     * 查询成员
     *
     * @param member 成员
     * @return {@link List}<{@link PosMemberVO}>
     */
    List<PosMemberVO> listMember(String member);

    /**
     * 结算
     *
     * @param settleAccountsDTO 结算dto
     * @return {@link Long}
     */
    OrderVO settleAccounts(SettleAccountsDTO settleAccountsDTO);
}
