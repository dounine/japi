# japi (RESTFul API 生成框架)

## 客户端使用方法
### gradle
```
compile group: 'com.dounine.japi', name: 'client', version: '${version}'
```
### maven
```
<dependency>
    <groupId>com.dounine.japi</groupId>
    <artifactId>client</artifactId>
    <version>${version}</version>
</dependency>
```
## 文档服务端配置
这个项目是SpringMVC项目
### 文件说明
japi/java/server/src/main/resources/logo.png : 项目默认图标

japi/java/server/src/main/resources/users.properties : 用户管理

**cat users.properties**
```
japi=japi123 admin
user1=abc123
....
```
以上文件中的第一行为文档客户端传输使用的用户与密码，后面带admin标识,为了多个项目共用一个帐号不被踢出，所以建议不要使用其它帐号进行传输，以免传输断线。
## 运行
因为是Gradle项目，所以要用以下方式运行
```
gradle bootRun
```
## 文档客户端配置
客户端japi配置文件如下

**cat japi.properties**
```
japi.name=test
japi.uuid=43a600877430438596de3d330e4bd06e
japi.version=1.0.0
japi.author=lake
japi.description=this is project description.
japi.createTime=2017-02-23 10:44:44
japi.icon=/home/lake/github/japi/html/img/logo.png
japi.server=http://192.168.0.179:7778
japi.server.username=japi
japi.server.password=japi123
```
**配置文件说明**

japi.name : 项目名

japi.uuid : 第个项目对应的唯一编号,防止项目重名

japi.version : 版本号

japi.author : 作者

japi.description : 项目描述

japi.createTime : 项目创建日期

japi.icon : 项目logo

japi.server : server服务器地扯

japi.server.username : 传输用户名

japi.server.password : 传输密码

### 生成文档并传输
JapiCreateTest.java
```
    @Test
    public void testCreate(){
    JapiClient.setPrefixPath("/home/lake/github/japi/java/");//项目路径前缀
    JapiClient.setpostfixPath("/src/main/java");//项目路径后缀
    
    JapiClient.setProjectJavaPath("client");//主项目地扯
    JapiClient.setActionReletivePath("com/dounine/japi/core/action");//相对主项目action包所在路径
    JapiClient.setIncludeProjectJavaPath(new String[]{"api"});//主项目中关联的其它包路径
    JapiClient.setIncludePackages(new String[]{"com.dounine.japi"});//关联的包,用于准确快速搜索
    JapiClient.saveHistory(true);//是否保留本地历史版本
    JapiClient.setFlushServer(false);//强制同步本地与服务器所有的版本（会先删除服务器以前的历史版本）
     
    IProject project = ProjectImpl.init();
    JapiClientStorage japiClientStorage = JapiClientStorage.getInstance();
    japiClientStorage.setProject(project);
    japiClientStorage.autoSaveToDisk();//自动使用到本地磁盘==> 用户目录/.japi-client/
    new JapiClientTransfer().autoTransfer(japiClientStorage);//文件传输到主服务器.
}
```


## Demo例子
test-japi 项目目录结构如下
```
├── build.gradle
├── settings.gradle
└── src
    ├── main
    │   └── java
    │       └── com
    │           └── dounine
    │               └── test-japi
    │                   ├── action
    │                   │   ├── comuser
    │                   │   │   ├── ComUserAction.java
    │                   │   │   └── package-info.java
    │                   │   └── vipuser
    │                   │       ├── VipUserAction.java
    │                   │       └── package-info.java
    │                   ├── entity
    │                   │   └── User.java
    │                   └── rest
    │                       └── Result.java
    └── test
        ├── java
        │   └── JapiCreateTest
        └── resources
            └── japi.properties
```
**action的每一个子包中必需包含一个package-info.java文件,用于注释此包作用**
```
action/vipuser/package-info.java
```
**内容必需包含包说明**
```
/**
 * 用户集合功能
 * Created by lake on 2017/2/24.
 */
package com.dounine.demo.action.user;
```
**Action类也必需包含说明**
```
/**
 * VIP用户
 * Created by lake on 2017/1/18.
 */
@RestController
@RequestMapping("vipUser")
public class VipUserAction {
    ....
}
```
**方法请求例子**
```
/**
 * 获取用户列表
 * @param user 用户信息
 * @param customCon2 {des:"测试参数2",req:true,def:"love",con:"只能为字符串"}
 * @throws RuntimeException
 * @deprecated yes
 * @version v1
 */
@GetMapping(value = "v1/list")
public Result hots(@Validated({User.UserDEL.class}) User user,String customCon2, BindingResult bindingResult) throws RuntimeException {
    //包含分组验证时,User.UserDEL接口组必需跟@Validated({User.UserDEL.class})一模一样
    //例如: @NotNull(message = "用户名不能为空", groups = {User.UserDEL.class})
    return null;
}

/**
 * 获取用户列表
 * @param user 用户信息
 * @return class User
 * @version v2
 */
@GetMapping(value = "v2/list")
public Result hots(@Validated({User.UserDEL.class}) User user) throws RuntimeException {
    //返回值只能为接口或者类，遵守RESTFul规范
    //@return class User 与 Result 返回值优先使用注释，如果没有注释则使用 Result
    return null;
}

/**
 * 获取用户信息
 * @param id 用户编号
 * @return class User
 * @version v1
 */
@GetMapping(value = "v1/info/{id}")
public Result hots(@PathVariable String id) throws RuntimeException {
    //URL请求也必需遵守规范,只能为数字或者数字+字母+(-)不能包含下划线,首字母不能大写。
    return null;
}
```
## 返回类型为以下两种格式内容，其它类型不支持
Result.java 接口
```
package com.dounine.test-japi.rest;

/**
 * Created by lake on 2017/1/14.
 */
public interface Result<T> {

    /**
     * 状态码
     */
    int getCode();

    /**
     * 错误消息
     */
    String getMsg();

    /**
     * 返回数据
     */
    T getData();
}
```
ResultImpl.java 实现
```
package com.dounine.test-japi.rest;

/**
 * Created by lake on 2017/1/14.
 */
public class ResultImpl<T> implements Result<T> {
    /**
     * 状态码
     */
    private int code;
    /**
     * 错误消息
     */
    private String msg;
    /**
     * 返回数据
     */
    private T data;
    
    .....
}
```
enum 枚举类型注释必需为以下注释格式
```
public enum TestType {
    /**
     * 增加
     */
    ADD,
    /**
     * 删除
     */
    DEL(0) 或者 DEL("DEL")
}
```
User实体信息
```
/**
 * 用户类信息
 */
public class User {
    public interface UserADD {}//增加分组

    public interface UserDEL {}//删除分组

    public interface UserUPDATE {}//修改分组

    public interface UserPUT {}//提交分组

    /**
     * 用户名
     *
     * @req true
     * @def 默认值为admin123
     * @con 约束,只能为英文加数字
     */
 
    protected String username;
    
    /**
     * 用户密码
     */
    @NotBlank(message = "用户密码不能为空", groups = {User.UserADD.class})
    private String password;
    
    //...
}
```
jsr303 标签支持情况
```
AssertFalse
AssertTrue
Email
Length
Size
Max
Min
NotBlank
NotNull
Pattern
```
未列出的暂不支持（支持了大部分常用的）
## 标签说明

* 常规标签
    * @return
        * 用于请求响应返回对象
        * demo
            * 返回自定义对象类型的数据 
            @return class User
            * 返回`RESTFul`格式数据 {name:'testName',type:'string',defaultValue:'默认值',description:'这是注释'}
    * @param
        * 用于参数注释说明
        * demo
            * @param user 用户数据
    * @version
        * 用于标注方法版本号（必填）
        * demo
            * @version v1
* 其它注释标签可自定义
    * 使用说明 
        * 编写`doc-tags.txt`文件,存放在resources目录中，或者放在resources/japi/目录     
        * 内容(标签后带点(.)符号的都是单标签,后面只能跟一个字符串,除了@return常规标签除外)
          ```
            return. 返回值
            param 请求参数
            deprecated. 过时了没
            version. 版本
            customer 我是自定义标签 
            customer. 我是自定义单标签 
          ```
