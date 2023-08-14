<#assign entityLower = table.entityName?lower_case>
package ${package.ServiceImpl};

import ${package.Entity}.${entity};
import ${package.Mapper}.${table.mapperName};
import ${package.Service}.${table.serviceName};
import ${superServiceImplClassPackage};
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.money.util.PageUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.money.common.vo.PageVO;
import ${packageOther}.${entityLower}.${table.entityName}DTO;
import ${packageOther}.${entityLower}.${table.entityName}QueryDTO;
import ${packageOther}.${entityLower}.${table.entityName}VO;

import java.util.Collection;

/**
 * <p>
 * ${table.comment!} 服务实现类
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@Service
@RequiredArgsConstructor
public class ${table.serviceImplName} extends ${superServiceImplClass}<${table.mapperName}, ${entity}> implements ${table.serviceName} {

    @Override
    public PageVO<${table.entityName}VO> list(${table.entityName}QueryDTO queryDTO) {
        Page<${table.entityName}> page = this.lambdaQuery()
                .last(StrUtil.isNotBlank(queryDTO.getOrderBy()), queryDTO.getOrderBySql())
                .page(PageUtil.toPage(queryDTO));
        return PageUtil.toPageVO(page, ${table.entityName}VO::new);
    }

    @Override
    public void add(${table.entityName}DTO addDTO) {
        ${table.entityName} ${table.entityName?uncap_first} = new ${table.entityName}();
        BeanUtil.copyProperties(addDTO, ${table.entityName?uncap_first});
        this.save(${table.entityName?uncap_first});
    }

    @Override
    public void update(${table.entityName}DTO updateDTO) {
        ${table.entityName} ${table.entityName?uncap_first} = new ${table.entityName}();
        BeanUtil.copyProperties(updateDTO, ${table.entityName?uncap_first});
        this.updateById(${table.entityName?uncap_first});
    }

    @Override
    public void delete(Collection<Long> ids) {
        this.removeByIds(ids);
    }

}