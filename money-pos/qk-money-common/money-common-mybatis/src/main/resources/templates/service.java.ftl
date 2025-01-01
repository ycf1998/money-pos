<#assign entityLower = table.entityName?lower_case>
package ${package.Service};

import ${package.Entity}.${entity};
import ${superServiceClassPackage};
import com.money.web.vo.PageVO;
import ${packageOther}.${entityLower}.${table.entityName}DTO;
import ${packageOther}.${entityLower}.${table.entityName}PageQueryDTO;
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

    /**
    * 列表
    *
    * @param queryDTO 查询 DTO
    * @return {@link PageVO }<{@link DemoVO }>
    */
    PageVO<${table.entityName}VO> list(${table.entityName}PageQueryDTO queryDTO);

    /**
    * 新增
    *
    * @param addDTO 新增 DTO
    * @return id
    */
    Long add(${table.entityName}DTO addDTO);

    /**
    * 修改
    *
    * @param updateDTO 修改 DTO
    */
    void update(${table.entityName}DTO updateDTO);

    /**
    * 删除
    *
    * @param ids IDS
    */
    void delete(Collection<Long> ids);

}
</#if>
