package com.money.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.money.common.exception.BaseException;
import com.money.common.vo.PageVO;
import com.money.dto.GmsBrand.GmsBrandDTO;
import com.money.dto.GmsBrand.GmsBrandQueryDTO;
import com.money.dto.GmsBrand.GmsBrandVO;
import com.money.dto.SelectVO;
import com.money.entity.GmsBrand;
import com.money.mapper.GmsBrandMapper;
import com.money.oss.OSSDelegate;
import com.money.oss.core.FileNameStrategy;
import com.money.oss.core.FolderPath;
import com.money.oss.local.LocalOSS;
import com.money.service.GmsBrandService;
import com.money.util.PageUtil;
import com.money.util.VOUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 商品品牌表 服务实现类
 * </p>
 *
 * @author money
 * @since 2023-02-27
 */
@Service
@RequiredArgsConstructor
public class GmsBrandServiceImpl extends ServiceImpl<GmsBrandMapper, GmsBrand> implements GmsBrandService {

    private final OSSDelegate<LocalOSS> localOSS;
    @Override
    public PageVO<GmsBrandVO> list(GmsBrandQueryDTO queryDTO) {
        Page<GmsBrand> page = this.lambdaQuery()
                .like(StrUtil.isNotBlank(queryDTO.getName()), GmsBrand::getName, queryDTO.getName())
                .last(StrUtil.isNotBlank(queryDTO.getSort()), queryDTO.getOrderBySql())
                .page(PageUtil.toPage(queryDTO, GmsBrand.class));
        return VOUtil.toPageVO(page, GmsBrandVO.class);
    }

    @Override
    public void add(GmsBrandDTO addDTO, MultipartFile logo) {
        boolean exists = this.lambdaQuery().eq(GmsBrand::getName, addDTO.getName()).exists();
        if (exists) {
            throw new BaseException("品牌已存在");
        }
        GmsBrand gmsBrand = new GmsBrand();
        BeanUtil.copyProperties(addDTO, gmsBrand);
        // 上传logo
        if (logo != null) {
            String logoUrl = localOSS.upload(logo, FolderPath.builder().cd("brand").build(), FileNameStrategy.TIMESTAMP);
            gmsBrand.setLogo(logoUrl);
        }
        this.save(gmsBrand);
    }

    @Override
    public void update(GmsBrandDTO updateDTO, MultipartFile logo) {
        boolean exists = this.lambdaQuery().ne(GmsBrand::getId, updateDTO.getId()).eq(GmsBrand::getName, updateDTO.getName()).exists();
        if (exists) {
            throw new BaseException("品牌已存在");
        }
        GmsBrand gmsBrand = this.getById(updateDTO.getId());
        BeanUtil.copyProperties(updateDTO, gmsBrand);
        // 上传logo
        if (logo != null) {
            localOSS.delete(gmsBrand.getLogo());
            String logoUrl = localOSS.upload(logo, FolderPath.builder().cd("brand").build(), FileNameStrategy.TIMESTAMP);
            gmsBrand.setLogo(logoUrl);
        }
        this.updateById(gmsBrand);
    }

    @Override
    public void delete(Set<Long> ids) {
        List<GmsBrand> gmsBrandList = this.listByIds(ids);
        this.removeByIds(ids);
        gmsBrandList.forEach(gmsBrand -> {
            if (StrUtil.isNotBlank(gmsBrand.getLogo())) {
                localOSS.delete(gmsBrand.getLogo());
            }
        });
    }

    @Override
    public List<SelectVO> getBrandSelect() {
        return this.list().stream().map(gmsBrand -> {
            SelectVO selectVO = new SelectVO();
            selectVO.setLabel(gmsBrand.getName());
            selectVO.setValue(gmsBrand.getId());
            return selectVO;
        }).collect(Collectors.toList());
    }

    @Override
    public void updateGoodsCount(Long id, int step) {
        this.lambdaUpdate().setSql("goods_count = goods_count + " + step).eq(GmsBrand::getId, id).update();
    }

}
