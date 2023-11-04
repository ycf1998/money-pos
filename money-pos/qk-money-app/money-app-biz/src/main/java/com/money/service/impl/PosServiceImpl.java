package com.money.service.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.money.common.exception.BaseException;
import com.money.common.util.BeanMapUtil;
import com.money.constant.OrderStatusEnum;
import com.money.dto.OmsOrder.OmsOrderVO;
import com.money.dto.OmsOrderDetail.OmsOrderDetailDTO;
import com.money.dto.Pos.PosGoodsVO;
import com.money.dto.Pos.PosMemberVO;
import com.money.dto.Pos.SettleAccountsDTO;
import com.money.entity.GmsGoods;
import com.money.entity.OmsOrder;
import com.money.entity.OmsOrderDetail;
import com.money.entity.OmsOrderLog;
import com.money.entity.UmsMember;
import com.money.service.GmsGoodsService;
import com.money.service.OmsOrderDetailService;
import com.money.service.OmsOrderLogService;
import com.money.service.OmsOrderService;
import com.money.service.PosService;
import com.money.service.UmsMemberService;
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
        return BeanMapUtil.to(gmsGoodsList, PosGoodsVO::new);
    }

    @Override
    public List<PosMemberVO> listMember(String member) {
        List<UmsMember> memberList = umsMemberService.lambdaQuery().eq(UmsMember::getDeleted, false)
                .like(StrUtil.isNotBlank(member), UmsMember::getName, member).list();
        return BeanMapUtil.to(memberList, PosMemberVO::new);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OmsOrderVO settleAccounts(SettleAccountsDTO settleAccountsDTO) {
        String orderNo = getOrderNo();
        OmsOrder order = new OmsOrder();
        order.setOrderNo(orderNo);
        // 核算订单
        List<OmsOrderDetailDTO> orderDetailDTOS = settleAccountsDTO.getOrderDetail();
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
        // 会员处理
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
            // 核算抵用券
            BigDecimal couponAmount = order.getCouponAmount();
            BigDecimal consumeCoupon = member.getCoupon().subtract(couponAmount);
            if (consumeCoupon.compareTo(BigDecimal.ZERO) < 0) {
                throw new BaseException("抵扣券不足");
            }
        } else if (memberId != null) {
            throw new BaseException("未找到该会员");
        }
        order.setStatus(OrderStatusEnum.PAID.name());
        order.setPaymentTime(LocalDateTime.now());

        // 保存订单
        omsOrderService.save(order);
        omsOrderDetailService.saveBatch(orderDetails);
        // 扣库存
        orderDetails.forEach(omsOrderDetail -> gmsGoodsService.sell(omsOrderDetail.getGoodsId(), omsOrderDetail.getQuantity()));
        // 会员消费
        if (member != null) {
            umsMemberService.consume(member.getId(), order.getPayAmount(), order.getCouponAmount());
        }
        // 订单日志
        OmsOrderLog log = new OmsOrderLog();
        log.setOrderId(order.getId());
        log.setDescription("完成订单");
        omsOrderLogService.saveBatch(ListUtil.of(log));
        return BeanMapUtil.to(order, OmsOrderVO::new);
    }

    /**
     * 核算订单
     *
     * @param order        订单
     * @param orderDetails 订单细节
     */
    private void aggOrder(OmsOrder order, List<OmsOrderDetail> orderDetails) {
        // 成本
        BigDecimal costAmount = BigDecimal.ZERO;
        // 总价
        BigDecimal totalAmount = BigDecimal.ZERO;
        // 实付款
        BigDecimal payAmount = BigDecimal.ZERO;
        // 用券
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
     * 得到订单
     *
     * @return {@link String}
     */
    private synchronized String getOrderNo() {
        // 15位
        String date = LocalDateTime.now().format(DatePattern.PURE_DATETIME_FORMATTER);
        // 1位
        String random = RandomUtil.randomNumbers(1);
        return date + random;
    }

}
