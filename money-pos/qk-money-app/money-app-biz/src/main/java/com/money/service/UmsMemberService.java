package com.money.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.money.common.vo.PageVO;
import com.money.entity.UmsMember;
import com.money.dto.member.MemberDTO;
import com.money.dto.member.MemberQueryDTO;
import com.money.dto.member.MemberVO;

import java.math.BigDecimal;
import java.util.Set;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author money
 * @since 2022-04-01
 */
public interface UmsMemberService extends IService<UmsMember> {

    /**
     * 查询会员列表
     *
     * @param queryDTO 查询dto
     * @return {@link PageVO}<{@link MemberVO}>
     */
    PageVO<MemberVO> list(MemberQueryDTO queryDTO);

    /**
     * 添加会员
     *
     * @param memberDTO 成员dto
     * @return {@link Long}
     */
    Long add(MemberDTO memberDTO);

    /**
     * 更新会员
     *
     * @param memberDTO 成员dto
     */
    void update(MemberDTO memberDTO);

    /**
     * 删除会员
     *
     * @param ids id
     */
    void delete(Set<Long> ids);

    /**
     * 消费
     *
     * @param id     id
     * @param amount 量
     * @param coupon 优惠券
     */
    void consume(Long id, BigDecimal amount, BigDecimal coupon);

    /**
     * 退税
     * 退款
     *
     * @param id                  id
     * @param amount              量
     * @param coupon              优惠券
     * @param increaseCancelTimes 增加退单次数
     */
    void rebate(Long id, BigDecimal amount, BigDecimal coupon, boolean increaseCancelTimes);
}
