package com.money.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.money.common.exception.BaseException;
import com.money.constant.SysErrorStatus;
import com.money.dto.SysDictDetailDTO;
import com.money.entity.SysDict;
import com.money.entity.SysDictDetail;
import com.money.mapper.SysDictDetailMapper;
import com.money.service.SysDictDetailService;
import com.money.service.SysDictService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author money
 * @since 2022-03-30
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysDictDetailServiceImpl extends ServiceImpl<SysDictDetailMapper, SysDictDetail> implements SysDictDetailService {

    @Override
    public List<SysDictDetail> listByDict(String dict) {
        return this.lambdaQuery().eq(SysDictDetail::getDict, dict).orderByAsc(SysDictDetail::getSort).list();
    }

    @Override
    public void add(SysDictDetailDTO sysDictDetailDTO) {
        SysDict dict = SpringUtil.getBean(SysDictService.class).getByDictName(sysDictDetailDTO.getDict());
        if (dict == null) {
            throw new BaseException(SysErrorStatus.DATA_NOT_FOUND);
        }
        SysDictDetail sysDictDetail = new SysDictDetail();
        BeanUtil.copyProperties(sysDictDetailDTO, sysDictDetail);
        this.save(sysDictDetail);
    }

    @Override
    public void updateById(SysDictDetailDTO sysDictDetailDTO) {
        SysDictDetail sysDictDetail = new SysDictDetail();
        BeanUtil.copyProperties(sysDictDetailDTO, sysDictDetail);
        this.updateById(sysDictDetail);
    }

    @Override
    public void deleteById(Collection<Long> ids) {
        this.removeBatchByIds(ids);
    }

    @Override
    public void deleteByDict(Collection<String> dictList) {
        if (CollUtil.isNotEmpty(dictList)) {
            this.lambdaUpdate().in(SysDictDetail::getDict, dictList).remove();
        }
    }
}
