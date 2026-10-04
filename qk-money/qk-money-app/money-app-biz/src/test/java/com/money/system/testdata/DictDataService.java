package com.money.system.testdata;

import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.money.entity.SysDict;
import com.money.entity.SysDictDetail;
import com.money.mapper.SysDictMapper;
import com.money.mapper.SysDictDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 字典测试数据服务
 */
@Component
@Scope("prototype")
public class DictDataService {

    @Autowired
    private SysDictMapper dictMapper;

    @Autowired
    private SysDictDetailMapper dictDetailMapper;

    // 记录的测试数据 ID
    private final List<Long> createdDictIds = new ArrayList<>();
    private final List<Long> createdDetailIds = new ArrayList<>();

    /**
     * 创建测试字典
     */
    public SysDict createDict(String dictName, String dictDesc) {
        SysDict dict = new SysDict();
        dict.setDictName(dictName);
        dict.setDictDesc(dictDesc);
        dictMapper.insert(dict);
        createdDictIds.add(dict.getId());
        return dict;
    }

    /**
     * 创建测试字典详情
     */
    public SysDictDetail createDictDetail(String dict, String value, String cnDesc) {
        SysDictDetail detail = new SysDictDetail();
        detail.setDict(dict);
        detail.setValue(value);
        detail.setCnDesc(cnDesc);
        detail.setEnDesc("English: " + cnDesc);
        detail.setHidden(false);
        dictDetailMapper.insert(detail);
        createdDetailIds.add(detail.getId());
        return detail;
    }

    /**
     * 清理创建的测试数据
     */
    public void cleanup() {
        try {
            // 1. 删除字典详情（记录的 ID）
            if (!createdDetailIds.isEmpty()) {
                new LambdaUpdateChainWrapper<>(dictDetailMapper)
                        .in(SysDictDetail::getId, createdDetailIds)
                        .remove();
            }

            // 2. 删除字典详情（兜底：清理 test_ 前缀的字典关联）
            new LambdaUpdateChainWrapper<>(dictDetailMapper)
                    .inSql(SysDictDetail::getDict,
                            "SELECT dict_name FROM sys_dict WHERE dict_name LIKE 'test_%'")
                    .remove();

            // 3. 删除字典（记录的 ID）
            if (!createdDictIds.isEmpty()) {
                new LambdaUpdateChainWrapper<>(dictMapper)
                        .in(SysDict::getId, createdDictIds)
                        .remove();
            }

            // 4. 删除字典（兜底：清理 test_ 前缀的字典）
            new LambdaUpdateChainWrapper<>(dictMapper)
                    .likeRight(SysDict::getDictName, "test_")
                    .remove();
        } catch (Exception e) {
            System.err.println("[DictDataService] 清理测试数据失败：" + e.getMessage());
        } finally {
            createdDictIds.clear();
            createdDetailIds.clear();
        }
    }
}
