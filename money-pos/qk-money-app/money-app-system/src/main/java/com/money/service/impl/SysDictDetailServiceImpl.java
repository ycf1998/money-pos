package com.money.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.money.common.exception.BaseException;
import com.money.constant.ErrorStatus;
import com.money.dto.SysDictDetailDTO;
import com.money.entity.SysDictDetail;
import com.money.mapper.SysDictDetailMapper;
import com.money.service.SysDictDetailService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author money
 * @since 2022-03-30
 */
@Service
public class SysDictDetailServiceImpl extends ServiceImpl<SysDictDetailMapper, SysDictDetail> implements SysDictDetailService {

    @Override
    public List<SysDictDetail> list(String dict) {
        return this.lambdaQuery().eq(SysDictDetail::getDict, dict).orderByAsc(SysDictDetail::getSort).list();
    }

    @Override
    public void add(SysDictDetailDTO sysDictDetailDTO) {
        // 字典标签唯一
        boolean exists = this.lambdaQuery().eq(SysDictDetail::getLabel, sysDictDetailDTO.getLabel()).eq(SysDictDetail::getDict, sysDictDetailDTO.getDict()).exists();
        if (exists) {
            throw new BaseException(ErrorStatus.DICT_LABEL_ALREADY_EXIST);
        }
        SysDictDetail sysDictDetail = new SysDictDetail();
        BeanUtil.copyProperties(sysDictDetailDTO, sysDictDetail);
        this.save(sysDictDetail);
    }

    @Override
    public void updateById(SysDictDetailDTO sysDictDetailDTO) {
        // 字典标签唯一
        boolean exists = this.lambdaQuery()
                .ne(SysDictDetail::getId, sysDictDetailDTO.getId())
                .eq(SysDictDetail::getLabel, sysDictDetailDTO.getLabel())
                .eq(SysDictDetail::getDict, sysDictDetailDTO.getDict()).exists();
        if (exists) {
            throw new BaseException(ErrorStatus.DICT_LABEL_ALREADY_EXIST);
        }
        SysDictDetail sysDictDetail = new SysDictDetail();
        BeanUtil.copyProperties(sysDictDetailDTO, sysDictDetail);
        this.updateById(sysDictDetail);
    }

    @Override
    public void deleteById(Set<Long> ids) {
        this.removeBatchByIds(ids);
    }
}
