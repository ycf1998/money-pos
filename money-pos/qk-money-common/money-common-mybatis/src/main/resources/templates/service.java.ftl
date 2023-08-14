<#assign entityLower = table.entityName?lower_case>
package ${package.Service};

import ${package.Entity}.${entity};
import ${superServiceClassPackage};
import com.money.common.vo.PageVO;
import ${packageOther}.${entityLower}.${table.entityName}DTO;
import ${packageOther}.${entityLower}.${table.entityName}QueryDTO;
import ${packageOther}.${entityLower}.${table.entityName}VO;

import java.util.Collection;

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

    void delete(Collection<Long> ids);
}
</#if>
