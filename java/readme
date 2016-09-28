
该类是:TestInterfaceDoc.java
    运行该类需要修改:  web包前缀   和   web路径
    String entityPrePath = "dnn.web";
    String filePath = "/home/ike/java/java/feedback/java/src/main/java/dnn/web";

注意:
    使用该接口方法生成接口文档必须导入第三方插件:
    compile "org.springframework:spring-webmvc:4.3.3.RELEASE"
    compile "javassist:javassist:3.12.1.GA"

注释要求:
/**
     * 活动分开地
     * #exclude  i
     * #include *
     * #version 1.2  (这是必须的)
     * #return
     */

解释:
     注释里面不可以使用分号(;) 且开头使用#
     * 第一行或几行用来描述该方法,开头可以不用#
     #exclude :表示排除某个参数,使用参数名 ,*表示排除所有
     #include :表示选中某个参数,使用参数名 ,*表示选中所有 ,默认include优先exclude
     * #version :方法的版本号,没写的话是不可以调用的 ,前端显示最新版本的
     #return :方法返回值描述


执行该接口方法返回的内容有:
    class :类名
    methodName :方法名
    returnType :方法返回类型
    return  :方法返回描述
    demo    :GET -> http://localhost:8080/custom/procing/
    paramTypeList :需要的参数和参数类型
    paramExclude : 排除的参数名
    paramInclude : 需要的参数名
    desc :用户的描述
    若传入的参数是用户自定义的实体,且该实体使用了相关注解,则会有如下返回:
    entityParamAttr :包括(attributeName:属性名,attributeDetail:属性描述)

类描述:#classDes
   /**
   *
   * #classDes 这是xx类
   *
   */

包描述:
   在该包下键一个package-info.java文件,里面包含
   /**
   *
   * #packageInfo 这是一个xx包
   *
   */

方法上一定要写注解@MethodVersionAnnotation(version ="对应注释中的版本号")



