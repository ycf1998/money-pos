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
import lombok.EqualsAndHashCode;
<#if chainModel>
import lombok.experimental.Accessors;
</#if>
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
<#if chainModel>
@Accessors(chain = true)
</#if>
</#if>
<#if swagger>
@Schema(description = "${table.comment!}")
</#if>
<#if entitySerialVersionUID>
public class ${entity}QueryDTO extends QueryRequest implements Serializable {
<#else>
public class ${entity}QueryDTO extends QueryRequest {
</#if>
<#if entitySerialVersionUID>

    private static final long serialVersionUID = 1L;
</#if>
<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list table.fields as field>
    <#if field.propertyName != "sort" && field.propertyName != "tenantId">
    <#if field.keyFlag>
        <#assign keyPropertyName="${field.propertyName}"/>
    </#if>

    <#if field.comment!?length gt 0>
    <#if swagger>
    @Schema(description="${field.comment}")
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
