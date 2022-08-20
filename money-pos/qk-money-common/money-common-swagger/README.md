# money-common-swagger(接口文档模块)

## 依赖

~~~xml
<!-- swagger -->
<dependency>
    <groupId>com.money</groupId>
    <artifactId>money-common-swagger</artifactId>
</dependency>
~~~

## 注解变更

swagger2 → swagger3

- `@Api` → `@Tag`
- `@ApiIgnore` → `@Parameter(hidden = true)` or `@Operation(hidden = true)` or `@Hidden`
- `@ApiImplicitParam` → `@Parameter`
- `@ApiImplicitParams` → `@Parameters`
- `@ApiModel` → `@Schema`
- `@ApiModelProperty(hidden = true)` → `@Schema(accessMode = READ_ONLY)`
- `@ApiModelProperty` → `@Schema`
- `@ApiOperation(value = "foo", notes = "bar")` → `@Operation(summary = "foo", description = "bar")`
- `@ApiParam` → `@Parameter`
- `@ApiResponse(code = 404, message = "foo")` → `@ApiResponse(responseCode = "404", description = "foo")`

## 相关配置

~~~yaml
# swagger配置，更多配置可参考官网https://springdoc.org/#properties
swagger:
  scan-package: com.money
  title: ${spring.application.name}
  description: ${spring.application.name}接口文档
  version: 1.0.0
  auth-header: ${money.security.token.header}
  contact:
    name: money
    url: www.money.com
    email: 374648769@qq.com
  external-documentation:
    url: https://github.com/ycf1998
    description: github
~~~

## 相关链接

[Springdoc 官网](https://springdoc.org/)
