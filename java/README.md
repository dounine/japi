# japi (RESTFul API 生成框架)

## 客户端使用方法
gradle
```
compile group: 'com.dounine.japi', name: 'client', version: '${version}'
```
maven
```
<dependency>
    <groupId>com.dounine.japi</groupId>
    <artifactId>client</artifactId>
    <version>${version}</version>
</dependency>
```
main方法启动
```
JapiClient.setPrefixPath("/home/lake/github/japi/java/");//项目路径前缀
JapiClient.setpostfixPath("/src/main/java");//项目路径后缀

JapiClient.setProjectJavaPath("client");//主项目地扯
JapiClient.setActionReletivePath("com/dounine/japi/core/action");//相对主项目action包所在路径
JapiClient.setIncludeProjectJavaPath(new String[]{"api"});//主项目中关联的其它包路径
JapiClient.setIncludePackages(new String[]{"com.dounine.japi"});//关联的包,用于准确快速搜索
JapiClient.setUseCache(true);//不使用缓存则删除原来生成的目录文件 

IProject project = ProjectImpl.init();
JapiClientStorage japiClientStorage = JapiClientStorage.getInstance();
japiClientStorage.setProject(project);
japiClientStorage.autoSaveToDisk();//自动使用到本地磁盘==> 用户目录/.japi-client/
new JapiClientTransfer().autoTransfer(japiClientStorage);//文件传输到主服务器.
```
## springmvc 例子编写 
**action包下不能直接写MVC类,还需要一层结构包**
```
action/article
```
**上面的包中必需包含一个package-info.java文件,用于注释此包作用**
```
action/article/package-info.java
```
**内容必需包含包说明**
```
/**
 * 新闻
 * Created by huanghuanlai on 2017/2/24.
 */
package com.dounine.demo.action.article;
```
**Action类也必需包含说明**
```
/**
 * 新闻工具
 * Created by huanghuanlai on 2017/1/18.
 */
@RestController
@RequestMapping("{version}/article")
public class ArticleAction {
```
**方法请求例子**
```
/**
 * 获取热闹新闻
 * @param user 用户信息
 * @throws RuntimeException
 * @deprecated yes
 */
@ApiVersion(1)
@org.springframework.web.bind.annotation.GetMapping(value = "hots")
@ResponseBody
public Result hots(@Validated User user, BindingResult bindingResult) throws RuntimeException {
    //do something
    return null;
}
```
* 注释标签说明 
    * 常规标签
        * @return
            * 用于请求响应返回对象
            * demo
                * 返回对象类型的数据 @return class User
                * 返回基本类型的数据 String userName '默认值' '描述'
                * 返回`RESTFul`格式数据 {name:'testName',type:'string',defaultValue:'0',description:'this is des.'}
        * @param
            * 用于参数注释
            * demo
                * @param user 用户对象数据
        * @version
            * 用于标注方法版本号
            * demo
                * @version 1
    * 其它注释标签可自定义
        * 使用说明 
            * 编写`doc-tags.txt`文件,存放在resources目录中，或者放esources/japi/在目录     
            * 内容(标签后带点符号的都是单标签,后面只能跟一个字符串,除了@return常规标签除外)
              ```
                return. 返回值
                param 参数
                deprecated. 过时
                version. 版本
                customer 我是自定义标签 
                customer. 我是自定义单标签 
              ```
* 注解标签说明 
    * 常规注解
    * @ApiVersion(1)
        * 用于标注此请求的版本号是多少,与@version功能是一样的
        * 此注解的类上的@RequestMapping必需包含{version}替换的字符串 
