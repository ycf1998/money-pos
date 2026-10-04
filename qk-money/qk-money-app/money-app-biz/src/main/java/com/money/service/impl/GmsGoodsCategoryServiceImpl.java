package com.money.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.money.web.exception.BaseException;
import com.money.dto.GmsGoodsCategory.GmsGoodsCategoryDTO;
import com.money.dto.SelectVO;
import com.money.dto.TreeNodeVO;
import com.money.entity.GmsGoodsCategory;
import com.money.mapper.GmsGoodsCategoryMapper;
import com.money.oss.OSSDelegate;
import com.money.oss.core.FileNameStrategy;
import com.money.oss.core.FolderPath;
import com.money.oss.local.LocalOSS;
import com.money.service.GmsGoodsCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 商品分类表 服务实现类
 * </p>
 *
 * @author money
 * @since 2023-02-27
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class GmsGoodsCategoryServiceImpl extends ServiceImpl<GmsGoodsCategoryMapper, GmsGoodsCategory> implements GmsGoodsCategoryService {

    private final OSSDelegate<LocalOSS> localOSS;

    @Override
    public void add(GmsGoodsCategoryDTO addDTO, MultipartFile icon) {
        boolean exists = this.lambdaQuery().eq(GmsGoodsCategory::getName, addDTO.getName()).exists();
        if (exists) {
            throw new BaseException("商品类别已存在");
        }
        GmsGoodsCategory gmsGoodsCategory = new GmsGoodsCategory();
        BeanUtil.copyProperties(addDTO, gmsGoodsCategory);
        // 上传icon
        if (icon != null) {
            String iconUrl = localOSS.upload(icon, FolderPath.builder().cd("goods").cd("category").build(), FileNameStrategy.TIMESTAMP);
            gmsGoodsCategory.setIcon(iconUrl);
        }
        this.save(gmsGoodsCategory);
    }

    @Override
    public void update(GmsGoodsCategoryDTO updateDTO, MultipartFile icon) {
        boolean exists = this.lambdaQuery().ne(GmsGoodsCategory::getId, updateDTO.getId()).eq(GmsGoodsCategory::getName, updateDTO.getName()).exists();
        if (exists) {
            throw new BaseException("商品类别已存在");
        }
        GmsGoodsCategory gmsGoodsCategory = this.getById(updateDTO.getId());
        BeanUtil.copyProperties(updateDTO, gmsGoodsCategory);
        // 上传icon
        if (icon != null) {
            localOSS.delete(gmsGoodsCategory.getIcon());
            String iconUrl = localOSS.upload(icon, FolderPath.builder().cd("goods").cd("category").build(), FileNameStrategy.TIMESTAMP);
            gmsGoodsCategory.setIcon(iconUrl);
        }
        this.updateById(gmsGoodsCategory);
    }
    @Override
    public void delete(Set<Long> ids) {
        List<GmsGoodsCategory> gmsGoodsCategoryList = this.listByIds(ids);
        this.removeByIds(ids);
        gmsGoodsCategoryList.forEach(gmsGoodsCategory -> {
            if (StrUtil.isNotBlank(gmsGoodsCategory.getIcon())) {
                localOSS.delete(gmsGoodsCategory.getIcon());
            }
        });
    }

    @Override
    public List<SelectVO> getGoodsCategorySelect() {
        return this.list().stream().map(gmsGoodsCategory -> {
            SelectVO selectVO = new SelectVO();
            selectVO.setLabel(gmsGoodsCategory.getName());
            selectVO.setValue(gmsGoodsCategory.getId());
            return selectVO;
        }).collect(Collectors.toList());
    }

    @Override
    public TreeNodeVO tree() {
        TreeNodeVO root = new TreeNodeVO();
        root.setId(0L);
        root.setPid(-1L);
        root.setName("全部分类");
        root.setChildren(this.getTree(root.getId()));
        return root;
    }

    private List<TreeNodeVO> getTree(Long pid) {
        List<GmsGoodsCategory> children = this.lambdaQuery().eq(GmsGoodsCategory::getPid, pid).list();
        List<TreeNodeVO> treeNodeVOList = children.stream().map(gmsGoodsCategory -> {
            TreeNodeVO treeNodeVO = new TreeNodeVO();
            treeNodeVO.setId(gmsGoodsCategory.getId());
            treeNodeVO.setPid(pid);
            treeNodeVO.setName(gmsGoodsCategory.getName());
            treeNodeVO.setIcon(gmsGoodsCategory.getIcon());
            return treeNodeVO;
        }).collect(Collectors.toList());
        treeNodeVOList.forEach(vo -> {
            vo.setChildren(getTree(vo.getId()));
        });
        return treeNodeVOList;
    }

    @Override
    public List<Long> getAllSubId(Long pid) {
        List<Long> allSubIds = new ArrayList<>();
        allSubIds.add(pid);
        recursionFillSubIds(pid, allSubIds);
        return allSubIds;
    }

    private void recursionFillSubIds(Long id, List<Long> subIds) {
        List<GmsGoodsCategory> amsAppCategories = this.lambdaQuery().select(GmsGoodsCategory::getId).eq(GmsGoodsCategory::getPid, id).list();
        amsAppCategories.forEach(gmsGoodsCategory -> {
            Long subId = gmsGoodsCategory.getId();
            subIds.add(subId);
            recursionFillSubIds(subId, subIds);
        });
    }

    @Override
    public void updateGoodsCount(Long categoryId, int step) {
        this.lambdaUpdate().setSql("goods_count = goods_count + " + step).eq(GmsGoodsCategory::getId, categoryId).update();
    }

}
