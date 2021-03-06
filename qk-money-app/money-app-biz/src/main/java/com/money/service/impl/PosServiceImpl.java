package com.money.service.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.money.common.exception.BaseException;
import com.money.constant.OrderStatusEnum;
import com.money.entity.*;
import com.money.service.*;
import com.money.util.VOUtil;
import com.money.web.order.OrderDetailDTO;
import com.money.web.order.OrderVO;
import com.money.web.pos.PosGoodsVO;
import com.money.web.pos.PosMemberVO;
import com.money.web.pos.SettleAccountsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PosServiceImpl implements PosService {

    private final UmsMemberService umsMemberService;
    private final GmsGoodsService gmsGoodsService;
    private final OmsOrderService omsOrderService;
    private final OmsOrderDetailService omsOrderDetailService;
    private final OmsOrderLogService omsOrderLogService;

    @Override
    public List<PosGoodsVO> listGoods(String barcode) {
        List<GmsGoods> gmsGoodsList = gmsGoodsService.lambdaQuery().like(StrUtil.isNotBlank(barcode), GmsGoods::getBarcode, barcode).list();
        return VOUtil.toVO(gmsGoodsList, PosGoodsVO.class);
    }

    @Override
    public List<PosMemberVO> listMember(String member) {
        List<UmsMember> memberList = umsMemberService.lambdaQuery().eq(UmsMember::getDeleted, false)
                .like(StrUtil.isNotBlank(member), UmsMember::getName, member).list();
        return VOUtil.toVO(memberList, PosMemberVO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderVO settleAccounts(SettleAccountsDTO settleAccountsDTO) {
        String orderNo = getOrderNo();
        OmsOrder order = new OmsOrder();
        order.setOrderNo(orderNo);
        // ????????????
        List<OrderDetailDTO> orderDetailDTOS = settleAccountsDTO.getOrderDetail();
        List<OmsOrderDetail> orderDetails = orderDetailDTOS.stream().map(dto -> {
            GmsGoods goods = gmsGoodsService.getById(dto.getGoodsId());
            OmsOrderDetail detail = new OmsOrderDetail();
            detail.setOrderNo(orderNo);
            detail.setStatus(OrderStatusEnum.PAID.name());
            detail.setGoodsId(goods.getId());
            detail.setGoodsBarcode(goods.getBarcode());
            detail.setGoodsName(goods.getName());
            detail.setSalePrice(goods.getSalePrice());
            detail.setPurchasePrice(goods.getPurchasePrice());
            detail.setVipPrice(goods.getVipPrice());
            detail.setCoupon(goods.getCoupon());

            detail.setGoodsPrice(dto.getGoodsPrice());
            detail.setQuantity(dto.getQuantity());
            return detail;
        }).collect(Collectors.toList());
        this.aggOrder(order, orderDetails);
        // ????????????
        order.setVip(false);
        Long memberId = settleAccountsDTO.getMember();
        UmsMember member = umsMemberService.getById(memberId);
        if (member != null) {
            order.setMember(member.getName());
            order.setMemberId(memberId);
            order.setVip(true);
            order.setContact(member.getPhone());
            order.setProvince(member.getProvince());
            order.setCity(member.getCity());
            order.setDistrict(member.getDistrict());
            order.setAddress(member.getAddress());
            // ???????????????
            BigDecimal couponAmount = order.getCouponAmount();
            BigDecimal consumeCoupon = member.getCoupon().subtract(couponAmount);
            if (consumeCoupon.compareTo(BigDecimal.ZERO) < 0) {
                throw new BaseException("???????????????");
            }
        } else if (memberId != null) {
            throw new BaseException("??????????????????");
        }
        order.setStatus(OrderStatusEnum.PAID.name());
        order.setPaymentTime(LocalDateTime.now());

        // ????????????
        omsOrderService.save(order);
        omsOrderDetailService.saveBatch(orderDetails);
        // ?????????
        orderDetails.forEach(omsOrderDetail -> gmsGoodsService.sell(omsOrderDetail.getGoodsId(), omsOrderDetail.getQuantity()));
        // ????????????
        if (member != null) {
            umsMemberService.consume(member.getId(), order.getPayAmount(), order.getCouponAmount());
        }
        // ????????????
        OmsOrderLog log = new OmsOrderLog();
        log.setOrderId(order.getId());
        log.setDescription("????????????");
        OmsOrderLog log2 = new OmsOrderLog();
        log2.setOrderId(order.getId());
        log2.setDescription("????????????");
        omsOrderLogService.saveBatch(ListUtil.of(log, log2));
        return VOUtil.toVO(order, OrderVO.class);
    }

    /**
     * ????????????
     *
     * @param order        ??????
     * @param orderDetails ????????????
     */
    private void aggOrder(OmsOrder order, List<OmsOrderDetail> orderDetails) {
        // ??????
        BigDecimal costAmount = BigDecimal.ZERO;
        // ??????
        BigDecimal totalAmount = BigDecimal.ZERO;
        // ?????????
        BigDecimal payAmount = BigDecimal.ZERO;
        // ??????
        BigDecimal couponAmount = BigDecimal.ZERO;
        for (OmsOrderDetail orderDetail : orderDetails) {
            BigDecimal quantity = new BigDecimal(orderDetail.getQuantity());
            BigDecimal purchasePrice = orderDetail.getPurchasePrice().multiply(quantity);
            BigDecimal salePrice = orderDetail.getSalePrice().multiply(quantity);
            BigDecimal coupon = orderDetail.getCoupon().multiply(quantity);
            BigDecimal goodsPrice = orderDetail.getGoodsPrice().multiply(quantity);
            costAmount = costAmount.add(purchasePrice);
            totalAmount = totalAmount.add(salePrice);
            payAmount = payAmount.add(goodsPrice);
            couponAmount = couponAmount.add(coupon);
        }
        order.setCostAmount(costAmount);
        order.setTotalAmount(totalAmount);
        order.setPayAmount(payAmount);
        order.setCouponAmount(couponAmount);
        order.setFinalSalesAmount(payAmount);
    }

    /**
     * ????????????
     *
     * @return {@link String}
     */
    private synchronized String getOrderNo() {
        // 15???
        String date = LocalDateTime.now().format(DatePattern.PURE_DATETIME_FORMATTER);
        // 1???
        String random = RandomUtil.randomNumbers(1);
        return date + random;
    }

}
