package com.money.testdata;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.money.entity.GmsGoods;
import com.money.entity.OmsOrder;
import com.money.entity.OmsOrderDetail;
import com.money.entity.UmsMember;
import com.money.mapper.GmsGoodsMapper;
import com.money.mapper.OmsOrderDetailMapper;
import com.money.mapper.OmsOrderMapper;
import com.money.mapper.UmsMemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * 订单测试数据服务 - 负责订单数据的创建和清理
 */
@Component
@Scope("prototype")
public class OrderDataService {

    @Autowired
    private OmsOrderMapper orderMapper;

    @Autowired
    private OmsOrderDetailMapper orderDetailMapper;

    @Autowired
    private GmsGoodsMapper goodsMapper;

    @Autowired
    private UmsMemberMapper memberMapper;

    // 记录的测试订单 ID
    private final List<Long> createdOrderIds = new ArrayList<>();
    private final List<Long> createdOrderDetailIds = new ArrayList<>();

    /**
     * 创建测试订单（自动记录 ID，支持自定义）
     */
    public OmsOrder createOrder(String suffix, Consumer<OmsOrder> customizer) {
        OmsOrder order = new OmsOrder();
        order.setOrderNo("TEST_ORDER_" + System.currentTimeMillis() % 100000 + "_" + suffix);
        order.setMember("测试会员_" + suffix);
        order.setMemberId(1L);
        order.setVip(false);
        order.setStatus("PAID");
        order.setContact("13900000000");
        order.setProvince("广东省");
        order.setCity("深圳市");
        order.setDistrict("南山区");
        order.setAddress("科技园路" + suffix + "号");
        order.setCostAmount(new BigDecimal("100.00"));
        order.setTotalAmount(new BigDecimal("200.00"));
        order.setPayAmount(new BigDecimal("190.00"));
        order.setCouponAmount(new BigDecimal("10.00"));
        order.setFinalSalesAmount(new BigDecimal("190.00"));
        order.setRemark("测试订单_" + suffix);
        order.setPaymentTime(LocalDateTime.now().minusDays(1));
        order.setCompletionTime(LocalDateTime.now());
        order.setTenantId(100L);

        if (customizer != null) {
            customizer.accept(order);
        }

        orderMapper.insert(order);
        createdOrderIds.add(order.getId());
        return order;
    }

    /**
     * 创建测试订单（默认配置）
     */
    public OmsOrder createOrder(String suffix) {
        return createOrder(suffix, null);
    }

    /**
     * 创建带订单详情的完整订单（用于退货等测试）
     * 
     * @param suffix 后缀
     * @param goods 商品
     * @param member 会员
     * @param quantity 数量
     * @return 订单详情
     */
    public OmsOrderDetail createOrderWithDetail(String suffix, GmsGoods goods, UmsMember member, Integer quantity) {
        // 创建订单
        String orderNo = "TEST_ORDER_" + System.currentTimeMillis() % 100000 + "_" + suffix;
        OmsOrder order = new OmsOrder();
        order.setOrderNo(orderNo);
        order.setMember(member != null ? member.getName() : "测试会员_" + suffix);
        order.setMemberId(member != null ? member.getId() : 1L);
        order.setVip(member != null);
        order.setStatus("PAID");
        order.setContact(member != null ? member.getPhone() : "13900000000");
        order.setProvince(member != null ? member.getProvince() : "广东省");
        order.setCity(member != null ? member.getCity() : "深圳市");
        order.setDistrict(member != null ? member.getDistrict() : "南山区");
        order.setAddress(member != null ? member.getAddress() : "科技园路" + suffix + "号");
        
        BigDecimal qty = new BigDecimal(quantity);
        order.setCostAmount(goods.getPurchasePrice().multiply(qty));
        order.setTotalAmount(goods.getSalePrice().multiply(qty));
        order.setPayAmount(goods.getSalePrice().multiply(qty));
        order.setCouponAmount(goods.getCoupon().multiply(qty));
        order.setFinalSalesAmount(goods.getSalePrice().multiply(qty));
        order.setPaymentTime(LocalDateTime.now());
        order.setTenantId(100L);
        orderMapper.insert(order);
        createdOrderIds.add(order.getId());

        // 创建订单详情
        OmsOrderDetail detail = new OmsOrderDetail();
        detail.setOrderNo(orderNo);
        detail.setStatus("PAID");
        detail.setGoodsId(goods.getId());
        detail.setGoodsBarcode(goods.getBarcode());
        detail.setGoodsName(goods.getName());
        detail.setGoodsPrice(goods.getSalePrice());
        detail.setQuantity(quantity);
        detail.setSalePrice(goods.getSalePrice());
        detail.setPurchasePrice(goods.getPurchasePrice());
        detail.setVipPrice(goods.getVipPrice());
        detail.setCoupon(goods.getCoupon());
        detail.setReturnQuantity(0);
        detail.setTenantId(100L);
        orderDetailMapper.insert(detail);
        createdOrderDetailIds.add(detail.getId());

        return detail;
    }

    /**
     * 清理创建的测试订单数据
     */
    public void cleanup() {
        try {
            // 1. 删除订单详情
            if (!createdOrderDetailIds.isEmpty()) {
                new LambdaUpdateChainWrapper<>(orderDetailMapper)
                        .in(OmsOrderDetail::getId, createdOrderDetailIds)
                        .remove();
            }

            // 2. 删除订单
            if (!createdOrderIds.isEmpty()) {
                new LambdaUpdateChainWrapper<>(orderMapper)
                        .in(OmsOrder::getId, createdOrderIds)
                        .remove();
            }

            // 3. 兜底清理：所有 TEST_ORDER_ 前缀的订单和详情
            new LambdaUpdateChainWrapper<>(orderDetailMapper)
                    .inSql(OmsOrderDetail::getOrderNo,
                            "SELECT order_no FROM oms_order WHERE order_no LIKE 'TEST_ORDER_%'")
                    .remove();
            new LambdaUpdateChainWrapper<>(orderMapper)
                    .likeRight(OmsOrder::getOrderNo, "TEST_ORDER_")
                    .remove();
        } catch (Exception e) {
            System.err.println("[OrderDataService] 清理测试订单失败：" + e.getMessage());
        } finally {
            createdOrderIds.clear();
            createdOrderDetailIds.clear();
        }
    }
}
