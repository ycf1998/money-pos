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
 * ${table.comment!} 服务接口
 *
 * @author ${author}
 * @since ${date}
 */
<#if kotlin>
interface ${table.serviceName} : ${superServiceClass}<${entity}>
<#else>
public interface ${table.serviceName} extends ${superServiceClass}<${entity}> {

    /**
     * 分页查询
     *
     * @param queryDTO 查询 DTO
     * @return 分页结果
     */
    PageVO<${table.entityName}VO> list(${table.entityName}PageQueryDTO queryDTO);

    /**
     * 添加
     *
     * @param addDTO 添加 DTO
     * @return 主键 ID
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
     * @param ids ID 集合
     */
    void delete(Collection<Long> ids);

}
</#if>
