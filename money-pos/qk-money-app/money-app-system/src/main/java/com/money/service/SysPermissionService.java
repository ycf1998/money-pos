package com.money.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.money.dto.SysPermissionDTO;
import com.money.dto.query.SysPermissionQueryDTO;
import com.money.entity.SysPermission;
import com.money.vo.SysPermissionVO;

import java.util.List;
import java.util.Set;

/**
 * 资源权限表(SysPermission)表服务接口
 *
 * @author money
 * @since 2021-09-08 22:35:10
 */
public interface SysPermissionService extends IService<SysPermission> {

    /**
     * 根据角色获取权限
     *
     * @param roleIds 权限id
     * @return {@link List}<{@link SysPermission}>
     */
    List<SysPermission> getByRole(List<Long> roleIds);

    /**
     * 查询权限列表
     *
     * @param queryDTO 查询dto
     * @return {@link List}<{@link SysPermissionVO}>
     */
    List<SysPermissionVO> list(SysPermissionQueryDTO queryDTO);

    /**
     * 添加权限
     *
     * @param sysPermissionDTO 系统权限dto
     */
    void add(SysPermissionDTO sysPermissionDTO);

    /**
     * 修改权限
     *
     * @param sysPermissionDTO 系统权限dto
     */
    void updateById(SysPermissionDTO sysPermissionDTO);

    /**
     * 删除权限
     *
     * @param ids ids
     */
    void deleteById(Set<Long> ids);

    /**
     * 返回所有子节点ID，包含自身ID
     *
     * @return {@link List}<{@link Long}>
     */
    List<Long> getAllSubIds(Long id);

}
