package com.money.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.money.common.vo.PageVO;
import com.money.constant.GoodsStatus;
import com.money.entity.GmsGoods;
import com.money.exception.GoodsRelatedException;
import com.money.mapper.GmsGoodsMapper;
import com.money.oss.OSSDelegate;
import com.money.oss.core.FileNameStrategy;
import com.money.oss.core.FolderPath;
import com.money.oss.local.LocalOSS;
import com.money.service.GmsBrandService;
import com.money.service.GmsGoodsCategoryService;
import com.money.service.GmsGoodsService;
import com.money.util.PageUtil;
import com.money.util.VOUtil;
import com.money.web.goods.GoodsDTO;
import com.money.web.goods.GoodsQueryDTO;
import com.money.web.goods.GoodsVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author money
 * @since 2022-04-04
 */
@Service
@RequiredArgsConstructor
public class GmsGoodsServiceImpl extends ServiceImpl<GmsGoodsMapper, GmsGoods> implements GmsGoodsService {

    private final OSSDelegate<LocalOSS> localOSS;
    private final GmsBrandService gmsBrandService;
    private final GmsGoodsCategoryService gmsGoodsCategoryService;

    @Override
    public PageVO<GoodsVO> list(GoodsQueryDTO queryDTO) {
        LambdaQueryChainWrapper<GmsGoods> queryChainWrapper = this.lambdaQuery();
        if (queryDTO.getCategoryId() != null) {
            List<Long> categoryIds = gmsGoodsCategoryService.getAllSubId(queryDTO.getCategoryId());
            queryChainWrapper.in(GmsGoods::getCategoryId, categoryIds);
        }
        Page<GmsGoods> page = queryChainWrapper
                .eq(ObjectUtil.isNotNull(queryDTO.getBrandId()), GmsGoods::getBrandId, queryDTO.getBrandId())
                .eq(ObjectUtil.isNotNull(queryDTO.getStatus()), GmsGoods::getStatus, queryDTO.getStatus())
                .like(StrUtil.isNotBlank(queryDTO.getBarcode()), GmsGoods::getBarcode, queryDTO.getBarcode())
                .like(StrUtil.isNotBlank(queryDTO.getName()), GmsGoods::getName, queryDTO.getName())
                .orderByDesc(StrUtil.isBlank(queryDTO.getSort()), GmsGoods::getUpdateTime)
                .last(StrUtil.isNotBlank(queryDTO.getSort()), queryDTO.getOrderBySql()).page(PageUtil.toPage(queryDTO, GmsGoods.class));
        return VOUtil.toPageVO(page, GoodsVO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(GoodsDTO goodsDTO, MultipartFile pic) {
        boolean exists = this.lambdaQuery().eq(GmsGoods::getBarcode, goodsDTO.getBarcode()).exists();
        if (exists) {
            throw new GoodsRelatedException("条码已存在");
        }
        GmsGoods gmsGoods = new GmsGoods();
        BeanUtil.copyProperties(goodsDTO, gmsGoods);
        // 上传图片
        if (pic != null) {
            String picUrl = localOSS.upload(pic, FolderPath.builder().cd("goods").build(), FileNameStrategy.TIMESTAMP);
            gmsGoods.setPic(picUrl);
        }
        this.save(gmsGoods);
        // 更新商品数量
        gmsBrandService.updateGoodsCount(gmsGoods.getBrandId(), 1);
        gmsGoodsCategoryService.updateGoodsCount(gmsGoods.getCategoryId(), 1);
        return gmsGoods.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(GoodsDTO goodsDTO, MultipartFile pic) {
        boolean exists = this.lambdaQuery().ne(GmsGoods::getId, goodsDTO.getId()).eq(GmsGoods::getBarcode, goodsDTO.getBarcode()).exists();
        if (exists) {
            throw new GoodsRelatedException("条码已存在");
        }
        GmsGoods gmsGoods = this.getById(goodsDTO.getId());
        // 更新商品数量
        if (!gmsGoods.getBrandId().equals(goodsDTO.getBrandId())) {
            gmsBrandService.updateGoodsCount(goodsDTO.getBrandId(), 1);
            gmsBrandService.updateGoodsCount(gmsGoods.getBrandId(), -1);
        }
        if (!gmsGoods.getCategoryId().equals(goodsDTO.getCategoryId())) {
            gmsGoodsCategoryService.updateGoodsCount(goodsDTO.getCategoryId(), 1);
            gmsGoodsCategoryService.updateGoodsCount(gmsGoods.getCategoryId(), -1);
        }
        BeanUtil.copyProperties(goodsDTO, gmsGoods);
        // 调整状态
        if (GoodsStatus.SOLD_OUT.name().equals(gmsGoods.getStatus()) && goodsDTO.getStock() > 0) {
            gmsGoods.setStatus(GoodsStatus.SALE.name());
        }
        if (GoodsStatus.SALE.name().equals(gmsGoods.getStatus()) && goodsDTO.getStock() <= 0) {
            gmsGoods.setStatus(GoodsStatus.SOLD_OUT.name());
        }
        // 上传图片
        if (pic != null) {
            String picUrl = localOSS.upload(pic, FolderPath.builder().cd("goods").build(), FileNameStrategy.TIMESTAMP);
            gmsGoods.setPic(picUrl);
        }
        this.updateById(gmsGoods);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        // 更新商品数量
        this.listByIds(ids).forEach(gmsGoods -> {
            gmsBrandService.updateGoodsCount(gmsGoods.getBrandId(), -1);
        });
        this.removeByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sell(Long goodsId, Integer quantity) {
        if (quantity > 0) {
            this.lambdaUpdate().setSql("stock = stock - " + quantity + ", sales = sales + " + quantity)
                    .eq(GmsGoods::getId, goodsId)
                    .update();
            this.lambdaUpdate().set(GmsGoods::getStatus, GoodsStatus.SOLD_OUT)
                    .eq(GmsGoods::getStatus, GoodsStatus.SALE)
                    .le(GmsGoods::getStock, 0).update();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStock(Long id, Integer quantity) {
        if (quantity != 0) {
            this.lambdaUpdate().setSql("stock = stock + " + quantity)
                    .eq(GmsGoods::getId, id).update();
            this.lambdaUpdate().set(GmsGoods::getStatus, GoodsStatus.SOLD_OUT)
                    .eq(GmsGoods::getStatus, GoodsStatus.SALE)
                    .le(GmsGoods::getStock, 0).update();
            this.lambdaUpdate().set(GmsGoods::getStatus, GoodsStatus.SALE)
                    .eq(GmsGoods::getStatus, GoodsStatus.SOLD_OUT)
                    .gt(GmsGoods::getStock, 0).update();
        }
    }

    @Override
    public BigDecimal getCurrentStockValue() {
        List<GmsGoods> gmsGoods = this.lambdaQuery().select(GmsGoods::getStock, GmsGoods::getPurchasePrice).gt(GmsGoods::getStock, 0).list();
        List<BigDecimal> sumList = gmsGoods.stream()
                .map(gmsGood -> gmsGood.getPurchasePrice().multiply(new BigDecimal(gmsGood.getStock()))).collect(Collectors.toList());
        BigDecimal sum = BigDecimal.ZERO;
        for (BigDecimal var : sumList) {
            sum = sum.add(var);
        }
        return sum;
    }
}
