<#assign entityLower = table.entityName?lower_case>
package ${packageOther}.${entityLower};

<#list table.importPackages as pkg>
<#if pkg!="com.baomidou.mybatisplus.annotation.TableName" && pkg!="com.money.mb.base.BaseEntity"
&& pkg!="com.baomidou.mybatisplus.annotation.IdType" && pkg!="com.baomidou.mybatisplus.annotation.TableId">
import ${pkg};
</#if>
</#list>
<#if springdoc>
import io.swagger.v3.oas.annotations.media.Schema;
</#if>
<#if entityLombokModel>
import lombok.Data;
import lombok.EqualsAndHashCode;
</#if>
<#if chainModel>
import lombok.experimental.Accessors;
</#if>
import com.money.common.dto.QueryRequest;

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
@EqualsAndHashCode(callSuper = true)
</#if>
<#if chainModel>
@Accessors(chain = true)
</#if>
<#if springdoc>
@Schema(description = "${table.comment!}")
</#if>
public class ${entity}QueryDTO extends QueryRequest {

<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list table.fields as field>
    <#if field.propertyName != "sort" && field.propertyName != "tenantId">
    <#if field.comment!?length gt 0>
    <#if springdoc>
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
