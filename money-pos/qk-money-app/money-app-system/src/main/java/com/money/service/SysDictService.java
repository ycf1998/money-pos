package com.money.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.money.dto.SysDictDTO;
import com.money.dto.query.SysDictQueryDTO;
import com.money.entity.SysDict;
import com.money.common.vo.PageVO;

import java.util.Set;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author money
 * @since 2022-03-05
 */
public interface SysDictService extends IService<SysDict> {

    /**
     * 查询字典列表
     *
     * @param queryDTO 查询dto
     * @return {@link PageVO}<{@link SysDict}>
     */
    PageVO<SysDict> list(SysDictQueryDTO queryDTO);

    /**
     * 添加字典
     *
     * @param sysDictDTO 系统字典dto
     */
    void add(SysDictDTO sysDictDTO);

    /**
     * 更新字典
     *
     * @param sysDictDTO 系统字典dto
     */
    void updateById(SysDictDTO sysDictDTO);

    /**
     * 删除字典
     *
     * @param ids id
     */
    void deleteById(Set<Long> ids);
}
