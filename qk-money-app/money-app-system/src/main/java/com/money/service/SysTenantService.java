package com.money.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.money.dto.SysTenantDTO;
import com.money.dto.query.SysTenantQueryDTO;
import com.money.entity.SysTenant;
import com.money.common.vo.PageVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author money
 * @since 2022-03-04
 */
public interface SysTenantService extends IService<SysTenant> {

    /**
     * 通过租户code获取租户id
     *
     * @param code 代码
     * @return {@link SysTenant}
     */
    SysTenant getTenantIdByCode(String code);

    /**
     * 查询租户列表
     *
     * @param queryDTO 查询dto
     * @return {@link PageVO}<{@link SysTenant}>
     */
    PageVO<SysTenant> list(SysTenantQueryDTO queryDTO);

    /**
     * 添加租户
     *
     * @param sysTenantDTO 系统租户dto
     * @param logo         标志
     */
    void add(SysTenantDTO sysTenantDTO, MultipartFile logo);

    /**
     * 更新租户
     *
     * @param sysTenantDTO 系统租户dto
     * @param logo         标志
     */
    void updateById(SysTenantDTO sysTenantDTO, MultipartFile logo);

    /**
     * 删除租户
     *
     * @param ids id
     */
    void deleteById(Set<Long> ids);
}
