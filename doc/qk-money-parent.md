# 父工程
​		项目的依赖清单，仅有一个POM文件。所有引用的第三方依赖必须写在这个工程的POM文件中，并规定版本。其他工程的POM不允许显示的设置<version>属性！

## 强依赖
项目强依赖了两个库，分别是`Lombok` 和`Hutool`。`Hutool`是一个丰富的工具类库，需要过目下它的工具，尽量避免重复造轮子。
~~~xml
<dependencies>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
    </dependency>
    <!-- 工具集 -->
    <dependency>
        <groupId>cn.hutool</groupId>
        <artifactId>hutool-all</artifactId>
    </dependency>
</dependencies>
~~~

# 延伸阅读
[Lombok](https://www.projectlombok.org/features/all)

[Hutool](https://www.hutool.cn/docs/#/)