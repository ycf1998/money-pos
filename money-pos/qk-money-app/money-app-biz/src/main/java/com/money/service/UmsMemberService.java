package com.money.service;

import com.money.entity.UmsMember;
import com.baomidou.mybatisplus.extension.service.IService;
import com.money.web.vo.PageVO;
import com.money.dto.UmsMember.UmsMemberDTO;
import com.money.dto.UmsMember.UmsMemberQueryDTO;
import com.money.dto.UmsMember.UmsMemberVO;

import java.math.BigDecimal;
import java.util.Set;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author money
 * @since 2023-02-27
 */
public interface UmsMemberService extends IService<UmsMember> {

    PageVO<UmsMemberVO> list(UmsMemberQueryDTO queryDTO);

    void add(UmsMemberDTO addDTO);

    void update(UmsMemberDTO updateDTO);

    void delete(Set<Long> ids);

    /**
     * 消费
     *
     * @param id     id
     * @param amount 消费金额
     * @param coupon 优惠券
     */
    void consume(Long id, BigDecimal amount, BigDecimal coupon);

    /**
     * 退款
     *
     * @param id                  id
     * @param amount              消费金额
     * @param coupon              优惠券
     * @param increaseCancelTimes 增加退单次数
     */
    void rebate(Long id, BigDecimal amount, BigDecimal coupon, boolean increaseCancelTimes);
}
