package com.money.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.money.dto.SysDictDetailDTO;
import com.money.entity.SysDictDetail;

import java.util.List;
import java.util.Set;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author money
 * @since 2022-03-30
 */
public interface SysDictDetailService extends IService<SysDictDetail> {

    /**
     * 查询字典详情列表
     *
     * @param dict dict
     * @return {@link List}<{@link SysDictDetail}>
     */
    List<SysDictDetail> list(String dict);

    /**
     * 添加字典详情
     *
     * @param sysDictDetailDTO
     */
    void add(SysDictDetailDTO sysDictDetailDTO);

    /**
     * 更新字典详情
     *
     * @param sysDictDetailDTO
     */
    void updateById(SysDictDetailDTO sysDictDetailDTO);

    /**
     * 删除字典详情
     *
     * @param ids id
     */
    void deleteById(Set<Long> ids);


}
