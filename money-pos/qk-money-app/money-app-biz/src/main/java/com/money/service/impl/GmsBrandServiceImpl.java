package com.money.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.money.common.vo.PageVO;
import com.money.entity.GmsBrand;
import com.money.exception.BrandRelatedException;
import com.money.mapper.GmsBrandMapper;
import com.money.oss.OSSDelegate;
import com.money.oss.core.FileNameStrategy;
import com.money.oss.core.FolderPath;
import com.money.oss.local.LocalOSS;
import com.money.service.GmsBrandService;
import com.money.util.PageUtil;
import com.money.util.VOUtil;
import com.money.dto.SelectVO;
import com.money.dto.brand.BrandDTO;
import com.money.dto.brand.BrandQueryDTO;
import com.money.dto.brand.BrandVO;
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
 * @since 2022-04-04
 */
@Service
@RequiredArgsConstructor
public class GmsBrandServiceImpl extends ServiceImpl<GmsBrandMapper, GmsBrand> implements GmsBrandService {

    private final OSSDelegate<LocalOSS> localOSS;

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
    public PageVO<BrandVO> list(BrandQueryDTO queryDTO) {
        Page<GmsBrand> page = this.lambdaQuery()
                .eq(StrUtil.isNotBlank(queryDTO.getName()), GmsBrand::getName, queryDTO.getName())
                .page(PageUtil.toPage(queryDTO, GmsBrand.class));
        return VOUtil.toPageVO(page, BrandVO.class);
    }

    @Override
    public Long add(BrandDTO brandDTO, MultipartFile logo) {
        boolean exists = this.lambdaQuery().eq(GmsBrand::getName, brandDTO.getName()).exists();
        if (exists) {
            throw new BrandRelatedException("品牌已存在");
        }
        GmsBrand gmsBrand = new GmsBrand();
        BeanUtil.copyProperties(brandDTO, gmsBrand);
        // 上传logo
        if (logo != null) {
            String logoUrl = localOSS.upload(logo, FolderPath.builder().cd("brand").build(), FileNameStrategy.TIMESTAMP);
            gmsBrand.setLogo(logoUrl);
        }
        this.save(gmsBrand);
        return gmsBrand.getId();
    }

    @Override
    public void update(BrandDTO brandDTO, MultipartFile logo) {
        boolean exists = this.lambdaQuery().ne(GmsBrand::getId, brandDTO.getId()).eq(GmsBrand::getName, brandDTO.getName()).exists();
        if (exists) {
            throw new BrandRelatedException("品牌已存在");
        }
        GmsBrand gmsBrand = new GmsBrand();
        BeanUtil.copyProperties(brandDTO, gmsBrand);
        // 上传logo
        if (logo != null) {
            String logoUrl = localOSS.upload(logo, FolderPath.builder().cd("brand").build(), FileNameStrategy.TIMESTAMP);
            gmsBrand.setLogo(logoUrl);
        }
        this.updateById(gmsBrand);
    }

    @Override
    public void delete(Set<Long> ids) {
        this.removeByIds(ids);
    }

    @Override
    public void updateGoodsCount(Long brandId, int step) {
        this.lambdaUpdate().setSql("goods_count = goods_count + " + step).eq(GmsBrand::getId, brandId).update();
    }
}
