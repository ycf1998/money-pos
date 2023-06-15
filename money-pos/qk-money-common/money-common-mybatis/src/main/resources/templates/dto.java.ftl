package ${package.Other}.${table.entityName};

<#list table.importPackages as pkg>
<#if pkg!="com.baomidou.mybatisplus.annotation.TableName" && pkg!="com.money.mb.base.BaseEntity">
import ${pkg};
</#if>
</#list>
<#if entitySerialVersionUID>
import java.io.Serializable;
</#if>
<#if swagger>
import io.swagger.v3.oas.annotations.media.Schema;
</#if>
<#if entityLombokModel>
import lombok.Data;
<#if chainModel>
import lombok.experimental.Accessors;
</#if>
</#if>
import javax.validation.constraints.NotNull;
import com.money.common.dto.ValidGroup;

/**
* <p>
* ${table.comment!}
* </p>
*
* @author ${author}
* @since ${date}
*/
<#if entityLombokModel>
@Data
<#if chainModel>
@Accessors(chain = true)
</#if>
</#if>
<#if swagger>
@Schema(description = "${table.comment!}")
</#if>
<#if entitySerialVersionUID>
public class ${entity}DTO implements Serializable {
<#else>
public class ${entity}DTO {
</#if>
<#if entitySerialVersionUID>

    private static final long serialVersionUID = 1L;
</#if>

<#if superEntityClass??>
    @NotNull(groups = ValidGroup.Update.class)
    private Long id;
</#if>
<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list table.fields as field>
    <#if field.propertyName != "id" && field.propertyName != "sort" && field.propertyName != "tenantId">
    <#if field.keyFlag>
        <#assign keyPropertyName="${field.propertyName}"/>
    </#if>

    <#if field.comment!?length gt 0>
    <#if swagger>
    @Schema(description = "${field.comment}")
    <#else>
    /**
    * ${field.comment}
    */
    </#if>
    </#if>
    private ${field.propertyType} ${field.propertyName};
    </#if>
</#list>
<#------------  END 字段循环遍历  ---------->

}
