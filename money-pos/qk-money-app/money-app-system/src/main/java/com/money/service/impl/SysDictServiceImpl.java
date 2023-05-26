package com.money.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.money.common.exception.BaseException;
import com.money.common.vo.PageVO;
import com.money.constant.ErrorStatus;
import com.money.dto.SysDictDTO;
import com.money.dto.query.SysDictQueryDTO;
import com.money.entity.SysDict;
import com.money.mapper.SysDictMapper;
import com.money.service.SysDictDetailService;
import com.money.service.SysDictService;
import com.money.util.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author money
 * @since 2022-03-05
 */
@Service
@RequiredArgsConstructor
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements SysDictService {

    private final SysDictDetailService sysDictDetailService;

    @Override
    public PageVO<SysDict> list(SysDictQueryDTO queryDTO) {
        Page<SysDict> page = this.lambdaQuery()
                .like(StrUtil.isNotBlank(queryDTO.getNameOrDesc()), SysDict::getName, queryDTO.getNameOrDesc())
                .or(StrUtil.isNotBlank(queryDTO.getNameOrDesc()),
                        wrapper -> wrapper.like(StrUtil.isNotBlank(queryDTO.getNameOrDesc()), SysDict::getDescription, queryDTO.getNameOrDesc()))
                .orderByDesc(SysDict::getUpdateTime)
                .page(PageUtil.toPage(queryDTO));
        return PageUtil.toPageVO(page);
    }

    @Override
    public void add(SysDictDTO sysDictDTO) {
        // 字典唯一
        boolean exists = this.lambdaQuery().eq(SysDict::getName, sysDictDTO.getName()).exists();
        if (exists) {
            throw new BaseException(ErrorStatus.DATA_ALREADY_EXIST, "字典");
        }
        SysDict sysDict = new SysDict();
        BeanUtil.copyProperties(sysDictDTO, sysDict);
        this.save(sysDict);
    }

    @Override
    public void updateById(SysDictDTO sysDictDTO) {
        SysDict sysDict = new SysDict();
        BeanUtil.copyProperties(sysDictDTO, sysDict);
        // 字典名不允许修改
        sysDict.setName(null);
        this.updateById(sysDict);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Set<Long> ids) {
        List<String> dictList = this.listByIds(ids).stream().map(SysDict::getName).collect(Collectors.toList());
        this.removeBatchByIds(ids);
        sysDictDetailService.deleteByDict(dictList);
    }
}
