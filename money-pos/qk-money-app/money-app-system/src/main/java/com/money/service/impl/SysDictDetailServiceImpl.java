package com.money.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.money.common.exception.BaseException;
import com.money.constant.ErrorStatus;
import com.money.dto.SysDictDetailDTO;
import com.money.entity.SysDictDetail;
import com.money.mapper.SysDictDetailMapper;
import com.money.service.SysDictDetailService;
import org.springframework.stereotype.Service;

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
public class SysDictDetailServiceImpl extends ServiceImpl<SysDictDetailMapper, SysDictDetail> implements SysDictDetailService {

    @Override
    public List<SysDictDetail> listByDict(String dict) {
        return this.lambdaQuery().eq(SysDictDetail::getDict, dict).orderByAsc(SysDictDetail::getSort).list();
    }

    @Override
    public void add(SysDictDetailDTO sysDictDetailDTO) {
        // 字典标签唯一
        boolean exists = this.lambdaQuery()
                .eq(SysDictDetail::getDict, sysDictDetailDTO.getDict())
                .eq(SysDictDetail::getLabel, sysDictDetailDTO.getLabel())
                .exists();
        if (exists) {
            throw new BaseException(ErrorStatus.DATA_ALREADY_EXIST, "字典标签");
        }
        SysDictDetail sysDictDetail = new SysDictDetail();
        BeanUtil.copyProperties(sysDictDetailDTO, sysDictDetail);
        this.save(sysDictDetail);
    }

    @Override
    public void updateById(SysDictDetailDTO sysDictDetailDTO) {
        if (StrUtil.isNotBlank(sysDictDetailDTO.getLabel())) {
            SysDictDetail byId = this.getById(sysDictDetailDTO.getId());
            // 字典标签唯一
            boolean exists = this.lambdaQuery()
                    .ne(SysDictDetail::getDict, byId.getDict())
                    .eq(SysDictDetail::getLabel, sysDictDetailDTO.getLabel())
                    .exists();
            if (exists) {
                throw new BaseException(ErrorStatus.DATA_ALREADY_EXIST, "字典标签");
            }
        }
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
