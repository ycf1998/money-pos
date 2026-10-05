package com.money.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.money.web.exception.BaseException;
import com.money.web.vo.PageVO;
import com.money.dto.GmsBrand.GmsBrandDTO;
import com.money.dto.GmsBrand.GmsBrandQueryDTO;
import com.money.dto.GmsBrand.GmsBrandVO;
import com.money.dto.SelectVO;
import com.money.entity.GmsBrand;
import com.money.entity.GmsGoods;
import com.money.mapper.GmsBrandMapper;
import com.money.mapper.GmsGoodsMapper;
import com.money.oss.OSSDelegate;
import com.money.oss.core.FileNameStrategy;
import com.money.oss.core.FolderPath;
import com.money.oss.local.LocalOSS;
import com.money.service.GmsBrandService;
import com.money.util.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Map;
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
@Transactional(rollbackFor = Exception.class)
public class GmsBrandServiceImpl extends ServiceImpl<GmsBrandMapper, GmsBrand> implements GmsBrandService {

    private final OSSDelegate<LocalOSS> localOSS;
    private final GmsGoodsMapper gmsGoodsMapper;

    @Override
    public PageVO<GmsBrandVO> list(GmsBrandQueryDTO queryDTO) {
        Page<GmsBrand> page = this.lambdaQuery()
                .like(StrUtil.isNotBlank(queryDTO.getName()), GmsBrand::getName, queryDTO.getName())
                .last(StrUtil.isNotBlank(queryDTO.getOrderBy()), queryDTO.getOrderBySql())
                .page(PageUtil.toPage(queryDTO));
        // 商品数量实时统计，不落库
        List<Long> brandIds = page.getRecords().stream().map(GmsBrand::getId).collect(Collectors.toList());
        Map<Long, Long> goodsCount = brandIds.isEmpty() ? Collections.emptyMap()
                : gmsGoodsMapper.selectList(Wrappers.lambdaQuery(GmsGoods.class)
                        .select(GmsGoods::getBrandId)
                        .in(GmsGoods::getBrandId, brandIds))
                .stream().filter(goods -> goods.getBrandId() != null)
                .collect(Collectors.groupingBy(GmsGoods::getBrandId, Collectors.counting()));
        return PageUtil.toPageVO(page, gmsBrand -> {
            GmsBrandVO gmsBrandVO = new GmsBrandVO();
            BeanUtil.copyProperties(gmsBrand, gmsBrandVO);
            gmsBrandVO.setGoodsCount(goodsCount.getOrDefault(gmsBrand.getId(), 0L).intValue());
            return gmsBrandVO;
        });
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

}
