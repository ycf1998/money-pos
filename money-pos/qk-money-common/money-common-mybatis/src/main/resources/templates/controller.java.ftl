<#assign entityLower = table.entityName?lower_case>
<#assign entityUncap = table.entityName?uncap_first>
<#assign serviceVar = table.serviceName?uncap_first>
package ${package.Controller};

<#if springdoc>
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
</#if>
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
<#if preAuthorize>
import org.springframework.security.access.prepost.PreAuthorize;
</#if>
import com.money.common.dto.ValidGroup;
import com.money.common.vo.PageVO;
import ${package.Service}.${table.serviceName};
import ${packageOther}.${entityLower}.${table.entityName}DTO;
import ${packageOther}.${entityLower}.${table.entityName}QueryDTO;
import ${packageOther}.${entityLower}.${table.entityName}VO;

import java.util.Set;

/**
 * <p>
 * ${table.comment!} 前端控制器
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
<#if springdoc>
@Tag(name = "${entityUncap}", description = "${table.comment!}")
</#if>
@RestController
@RequestMapping("<#if package.ModuleName?? && package.ModuleName != "">/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>")
@RequiredArgsConstructor
<#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
<#else>
public class ${table.controllerName} {
</#if>

    private final ${table.serviceName} ${serviceVar};

    <#if springdoc>
    @Operation(summary = "分页查询")
    </#if>
    @GetMapping
    <#if preAuthorize>
    @PreAuthorize("@rbac.hasPermission('${entityUncap}:list')")
    </#if>
    public PageVO<${table.entityName}VO> list(@Validated ${table.entityName}QueryDTO queryDTO) {
        return ${serviceVar}.list(queryDTO);
    }

    <#if springdoc>
    @Operation(summary = "添加")
    </#if>
    @PostMapping
    <#if preAuthorize>
    @PreAuthorize("@rbac.hasPermission('${entityUncap}:add')")
    </#if>
    public void add(@Validated(ValidGroup.Save.class) @RequestBody ${table.entityName}DTO addDTO) {
        ${serviceVar}.add(addDTO);
    }

    <#if springdoc>
    @Operation(summary = "修改")
    </#if>
    @PutMapping
    <#if preAuthorize>
    @PreAuthorize("@rbac.hasPermission('${entityUncap}:edit')")
    </#if>
    public void update(@Validated(ValidGroup.Update.class) @RequestBody ${table.entityName}DTO updateDTO) {
        ${serviceVar}.update(updateDTO);
    }

    <#if springdoc>
    @Operation(summary = "删除")
    </#if>
    @DeleteMapping
    <#if preAuthorize>
    @PreAuthorize("@rbac.hasPermission('${entityUncap}:del')")
    </#if>
    public void delete(@RequestBody Set<Long> ids) {
        ${serviceVar}.delete(ids);
    }

}