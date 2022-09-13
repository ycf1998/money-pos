package ${package.Controller};

import lombok.RequiredArgsConstructor;
import java.util.Set;
import org.springframework.web.bind.annotation.*;
import com.money.common.vo.PageVO;
import com.money.common.dto.ValidGroup;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;

import ${package.Service}.${table.serviceName};
import ${package.Other}.${table.entityName}.${table.entityName}DTO;
import ${package.Other}.${table.entityName}.${table.entityName}QueryDTO;
import ${package.Other}.${table.entityName}.${table.entityName}VO;

<#if swagger>
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
</#if>
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>
<#assign entityUncap = table.entityName?uncap_first>
<#assign serviceVar = table.serviceName?uncap_first>
/**
 * <p>
 * ${table.comment!} 前端控制器
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
<#if swagger>
@Tag(name = "${entityUncap}", description = "${table.comment!}")
</#if>
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
@RequestMapping("<#if package.ModuleName?? && package.ModuleName != "">/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>")
<#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
@RequiredArgsConstructor
<#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
<#else>
public class ${table.controllerName} {
</#if>

    private final ${table.serviceName} ${serviceVar};

<#if swagger>
    @Operation(summary = "分页查询")
</#if>
    @GetMapping
    <#if preAuthorize>
    @PreAuthorize("@rbac.hasPermission('${entityUncap}:list')")
    </#if>
    public PageVO<${table.entityName}VO> list(@Validated ${table.entityName}QueryDTO queryDTO) {
        return ${serviceVar}.list(queryDTO);
    }

    <#if swagger>
    @Operation(summary = "添加")
    </#if>
    @PostMapping
    <#if preAuthorize>
    @PreAuthorize("@rbac.hasPermission('${entityUncap}:add')")
    </#if>
    public void add(@Validated(ValidGroup.Save.class) @RequestBody ${table.entityName}DTO addDTO) {
        ${serviceVar}.add(addDTO);
    }

    <#if swagger>
    @Operation(summary = "修改")
    </#if>
    @PutMapping
    <#if preAuthorize>
    @PreAuthorize("@rbac.hasPermission('${entityUncap}:edit')")
    </#if>
    public void update(@Validated(ValidGroup.Update.class) @RequestBody ${table.entityName}DTO updateDTO) {
        ${serviceVar}.update(updateDTO);
    }

    <#if swagger>
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
</#if>
