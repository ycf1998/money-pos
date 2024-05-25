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
<#if chainModel>
import lombok.experimental.Accessors;
</#if>
</#if>

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
<#if springdoc>
@Schema(description = "${table.comment!}")
</#if>
<#if entitySerialVersionUID>
public class ${entity}VO implements Serializable {
<#else>
public class ${entity}VO {
</#if>

    private Long id;

<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list table.fields as field>
    <#if field.propertyName != "id" && field.propertyName != "sort" && field.propertyName != "tenantId">
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
