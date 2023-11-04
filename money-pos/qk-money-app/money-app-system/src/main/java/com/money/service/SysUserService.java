package com.money.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.money.common.vo.PageVO;
import com.money.dto.ChangePasswordDTO;
import com.money.dto.SysUserDTO;
import com.money.dto.UpdateProfileDTO;
import com.money.dto.query.SysUserQueryDTO;
import com.money.entity.SysUser;
import com.money.vo.SysUserVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

/**
 * 用户表(SysUser)表服务接口
 *
 * @author money
 * @since 2021-09-08 22:35:10
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 通过用户名获取用户
     *
     * @param username 用户名
     * @return {@link SysUser}
     */
    SysUser getByUsername(String username);

    // ============================================================

    /**
     * 上传头像
     *
     * @param username 用户名
     * @param file     文件
     * @return {@link String}
     */
    String uploadAvatar(String username, MultipartFile file);

    /**
     * 更新资料
     *
     * @param updateProfileDTO 更新资料dto
     * @param username         用户名
     */
    void updateProfile(String username, UpdateProfileDTO updateProfileDTO);

    /**
     * 更改密码
     *
     * @param changePasswordDTO 更改密码dto
     * @param username          用户名
     */
    void changePassword(String username, ChangePasswordDTO changePasswordDTO);

    // ============================================================

    /**
     * 查询用户列表
     *
     * @param queryDTO 查询dto
     * @return {@link PageVO}<{@link SysUserVO}>
     */
    PageVO<SysUserVO> list(SysUserQueryDTO queryDTO);

    /**
     * 添加用户
     *
     * @param sysUserDTO 系统用户dto
     */
    void add(SysUserDTO sysUserDTO);

    /**
     * 修改用户
     *
     * @param sysUserDTO 系统用户dto
     */
    void updateById(SysUserDTO sysUserDTO);

    /**
     * 删除用户
     *
     * @param ids id
     */
    void deleteById(Set<Long> ids);

}
