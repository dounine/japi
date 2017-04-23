# JAPI
## SpringMVC RESTFul文档生成(不依赖注解,完全读取注释)

这个项目是为 `SpringMVC` 框架而做的，因为 `SpringMVC` 做为现在大众用得相对比较多的一个MVC框架，所以就优先用它孵化出了这个项目`JAPI`

## 说明
这个项目需要依赖以下两个子项目,两个项目 [java](https://github.com/dounine/japi/tree/master/java) [node](https://github.com/dounine/japi/tree/master/node) 具体使用说明可自行查看。

### 第一个：java

java项目又分着两个子模块

* client
这个模块是用来生成RESTFul文档的核心，我们要生成文档用它就行了。
* server
这个模块是用来管理各个client传上来的模块文档，与node前端页面对接的。

### 第二个：node
* node
这个子项目是用来显示文档页面
