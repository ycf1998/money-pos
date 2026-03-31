package com.money.testdata;

import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.money.entity.OmsOrder;
import com.money.entity.OmsOrderDetail;
import com.money.entity.OmsOrderLog;
import com.money.entity.UmsMember;
import com.money.mapper.OmsOrderDetailMapper;
import com.money.mapper.OmsOrderLogMapper;
import com.money.mapper.OmsOrderMapper;
import com.money.mapper.UmsMemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 收银测试数据服务 - 负责收银产生的订单等数据的清理
 * 
 * <p>收款接口会修改的数据：
 * 1. oms_order - 创建订单
 * 2. oms_order_detail - 创建订单明细
 * 3. oms_order_log - 创建订单日志
 * 4. gms_goods - 扣减库存
 * 5. ums_member - 修改会员抵用券和消费统计
 * </p>
 */
@Component
@Scope("prototype")
public class PosDataService {

    @Autowired
    private OmsOrderMapper orderMapper;

    @Autowired
    private OmsOrderDetailMapper orderDetailMapper;

    @Autowired
    private OmsOrderLogMapper orderLogMapper;

    @Autowired
    private UmsMemberMapper memberMapper;

    // 记录的测试数据 ID
    private final List<Long> createdOrderIds = new ArrayList<>();
    private final List<Long> createdOrderDetailIds = new ArrayList<>();
    private final List<Long> createdOrderLogIds = new ArrayList<>();
    private final List<Long> modifiedMemberIds = new ArrayList<>();

    /**
     * 记录收款产生的订单数据（用于后续清理）
     */
    public void recordOrderData(Long orderId) {
        if (orderId != null) {
            createdOrderIds.add(orderId);
        }
    }

    /**
     * 记录修改的会员 ID（用于恢复数据）
     */
    public void recordModifiedMember(Long memberId) {
        if (memberId != null && !modifiedMemberIds.contains(memberId)) {
            modifiedMemberIds.add(memberId);
        }
    }

    /**
     * 清理创建的订单数据
     */
    private void cleanupOrders() {
        try {
            if (!createdOrderIds.isEmpty()) {
                // 先清理订单日志
                new LambdaUpdateChainWrapper<>(orderLogMapper)
                        .in(OmsOrderLog::getOrderId, createdOrderIds)
                        .remove();

                // 再清理订单明细
                new LambdaUpdateChainWrapper<>(orderDetailMapper)
                        .inSql(OmsOrderDetail::getOrderNo,
                                "SELECT order_no FROM oms_order WHERE id IN (" + String.join(",", createdOrderIds.stream().map(String::valueOf).toArray(String[]::new)) + ")")
                        .remove();

                // 最后清理订单
                new LambdaUpdateChainWrapper<>(orderMapper)
                        .in(OmsOrder::getId, createdOrderIds)
                        .remove();
            }

            // 兜底清理：所有测试相关订单（通过订单号前缀识别）
            // 测试环境的订单号格式：202604082016362（时间+随机数）
            // 这里依赖 OrderDataService 的兜底清理策略
        } catch (Exception e) {
            System.err.println("[PosDataService] 清理测试订单失败：" + e.getMessage());
        } finally {
            createdOrderIds.clear();
            createdOrderDetailIds.clear();
            createdOrderLogIds.clear();
        }
    }

    /**
     * 恢复会员数据（简单清理，实际应该在测试前备份数据）
     * 
     * <p>注意：会员数据的完全恢复比较困难，因为消费会修改多个字段。
     * 建议测试使用临时创建的会员数据，通过 MemberDataService 清理。</p>
     */
    private void restoreMembers() {
        // 会员数据由 MemberDataService 负责清理
        modifiedMemberIds.clear();
    }

    /**
     * 清理所有测试数据
     */
    public void cleanup() {
        cleanupOrders();
        restoreMembers();
    }
}
