package ${package.Service};

import ${package.Entity}.${entity};
import ${superServiceClassPackage};
import com.money.common.vo.PageVO;
import ${package.Other}.${table.entityName}.${table.entityName}DTO;
import ${package.Other}.${table.entityName}.${table.entityName}QueryDTO;
import ${package.Other}.${table.entityName}.${table.entityName}VO;

import java.util.Set;

/**
 * <p>
 * ${table.comment!} 服务类
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
<#if kotlin>
interface ${table.serviceName} : ${superServiceClass}<${entity}>
<#else>
public interface ${table.serviceName} extends ${superServiceClass}<${entity}> {

    PageVO<${table.entityName}VO> list(${table.entityName}QueryDTO queryDTO);

    void add(${table.entityName}DTO addDTO);

    void update(${table.entityName}DTO updateDTO);

    void delete(Set<Long> ids);
}
</#if>
