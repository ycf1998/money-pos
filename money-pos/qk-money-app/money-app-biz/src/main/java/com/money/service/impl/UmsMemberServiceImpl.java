package com.money.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.money.common.exception.BaseException;
import com.money.entity.UmsMember;
import com.money.mapper.UmsMemberMapper;
import com.money.service.UmsMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.money.util.PageUtil;
import com.money.util.VOUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.money.common.vo.PageVO;
import com.money.dto.UmsMember.UmsMemberDTO;
import com.money.dto.UmsMember.UmsMemberQueryDTO;
import com.money.dto.UmsMember.UmsMemberVO;

import java.math.BigDecimal;
import java.util.Set;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author money
 * @since 2023-02-27
 */
@Service
@RequiredArgsConstructor
public class UmsMemberServiceImpl extends ServiceImpl<UmsMemberMapper, UmsMember> implements UmsMemberService {

    @Override
    public PageVO<UmsMemberVO> list(UmsMemberQueryDTO queryDTO) {
        Page<UmsMember> page = this.lambdaQuery()
                .eq(StrUtil.isNotBlank(queryDTO.getCode()), UmsMember::getCode, queryDTO.getCode())
                .like(StrUtil.isNotBlank(queryDTO.getName()), UmsMember::getName, queryDTO.getName())
                .like(StrUtil.isNotBlank(queryDTO.getPhone()), UmsMember::getPhone, queryDTO.getPhone())
                .eq(StrUtil.isNotBlank(queryDTO.getType()), UmsMember::getType, queryDTO.getType())
                .eq(UmsMember::getDeleted, false)
                .orderByDesc(StrUtil.isBlank(queryDTO.getSort()), UmsMember::getUpdateTime)
                .last(StrUtil.isNotBlank(queryDTO.getSort()), queryDTO.getOrderBySql())
                .page(PageUtil.toPage(queryDTO));
        return VOUtil.toPageVO(page, UmsMemberVO.class);
    }

    @Override
    public void add(UmsMemberDTO addDTO) {
        boolean exists = this.lambdaQuery().eq(UmsMember::getPhone, addDTO.getPhone()).exists();
        if (exists) {
            throw new BaseException("手机号码已存在");
        }
        UmsMember umsMember = new UmsMember();
        BeanUtil.copyProperties(addDTO, umsMember);
        // 分配会员号
        umsMember.setCode(RandomUtil.randomNumbers(8));
        this.save(umsMember);
    }

    @Override
    public void update(UmsMemberDTO updateDTO) {
        boolean exists = this.lambdaQuery().ne(UmsMember::getId, updateDTO.getId()).eq(UmsMember::getPhone, updateDTO.getPhone()).exists();
        if (exists) {
            throw new BaseException("手机号码已存在");
        }
        UmsMember umsMember = new UmsMember();
        BeanUtil.copyProperties(updateDTO, umsMember);
        this.updateById(umsMember);
    }

    @Override
    public void delete(Set<Long> ids) {
        this.lambdaUpdate().set(UmsMember::getDeleted, true).in(UmsMember::getId, ids).update();
    }

    @Override
    public void consume(Long id, BigDecimal amount, BigDecimal coupon) {
        // 加总消费金额 加消费抵用券 减当前抵用券 消费次数加一
        this.lambdaUpdate().setSql("consume_amount = consume_amount + " + amount +
                ", consume_coupon = consume_coupon + " + coupon +
                ", coupon = coupon - " + coupon +
                ", consume_times = consume_times + 1").eq(UmsMember::getId, id).update();
    }

    @Override
    public void rebate(Long id, BigDecimal amount, BigDecimal coupon, boolean increaseCancelTimes) {
        // 减总消费金额 减费抵用券 退还当前抵用券 取消次数加一
        this.lambdaUpdate().setSql("consume_amount = consume_amount - " + amount +
                        ", consume_coupon = consume_coupon - " + coupon +
                        ", coupon = coupon + " + coupon)
                .setSql(increaseCancelTimes, "cancel_times = cancel_times + 1").eq(UmsMember::getId, id).update();
    }

}
