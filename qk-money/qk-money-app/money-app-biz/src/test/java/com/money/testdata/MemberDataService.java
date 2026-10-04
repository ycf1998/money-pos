package com.money.testdata;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.money.entity.UmsMember;
import com.money.mapper.UmsMemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * 会员测试数据服务 - 负责会员数据的创建和清理
 */
@Component
@Scope("prototype")
public class MemberDataService {

    @Autowired
    private UmsMemberMapper memberMapper;

    // 记录的测试会员 ID
    private final List<Long> createdMemberIds = new ArrayList<>();

    // 测试会员默认配置

    /**
     * 创建测试会员（自动记录 ID，支持自定义）
     */
    public UmsMember createMember(String suffix, Consumer<UmsMember> customizer) {
        UmsMember member = new UmsMember();
        member.setCode("TEST_" + RandomUtil.randomString(16));
        member.setName("测试会员_" + suffix);
        member.setType("VIP");
        member.setPhone("139" + String.format("%08d", System.currentTimeMillis() % 100000000));
        member.setProvince("广东省");
        member.setCity("深圳市");
        member.setDistrict("南山区");
        member.setAddress("科技园路" + suffix + "号");
        member.setCoupon(RandomUtil.randomBigDecimal(new BigDecimal("100"), new BigDecimal("1000")));
        member.setConsumeAmount(RandomUtil.randomBigDecimal(new BigDecimal("0"), new BigDecimal("5000")));
        member.setConsumeCoupon(RandomUtil.randomBigDecimal(new BigDecimal("0"), new BigDecimal("200")));
        member.setConsumeTimes(RandomUtil.randomInt(0, 50));
        member.setCancelTimes(RandomUtil.randomInt(0, 10));
        member.setRemark("测试备注_" + suffix);

        if (customizer != null) {
            customizer.accept(member);
        }

        memberMapper.insert(member);
        createdMemberIds.add(member.getId());
        return member;
    }

    /**
     * 创建测试会员（默认配置）
     */
    public UmsMember createMember(String suffix) {
        return createMember(suffix, null);
    }

    /**
     * 清理创建的测试会员数据
     *
     * <p>清理策略：
     * 1. 先清理记录的会员 ID（精确清理）
     * 2. 再清理所有 TEST_CODE_ 前缀的残留数据（兜底清理）
     * </p>
     */
    public void cleanup() {
        try {
            // 1. 删除会员（记录的会员 ID）
            if (!createdMemberIds.isEmpty()) {
                new LambdaUpdateChainWrapper<>(memberMapper)
                        .in(UmsMember::getId, createdMemberIds)
                        .remove();
            }

            // 2. 删除会员（兜底：清理所有 TEST_CODE_ 前缀的会员）
            new LambdaUpdateChainWrapper<>(memberMapper)
                    .likeRight(UmsMember::getCode, "TEST_CODE_")
                    .remove();
        } catch (Exception e) {
            System.err.println("[MemberDataService] 清理测试会员失败：" + e.getMessage());
        } finally {
            createdMemberIds.clear();
        }
    }
}
