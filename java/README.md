
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

     /**
          * 查看所有分页工单
          * #nameDes  方法功能名
          * #example  用于写传参例子,用大括号括起来,如:{status:'UNTREATED',detectionNum:'2016ST2',filename:'**\*.[png]'}
          * #exclude  i
          * #include  *
          * #version 1.0  (这是必须的)
          * #return   返回例子{"count":2,"rows":[{"chemicalCell":{"sampleName":"此而可忍发"},"copyNum":3,"createTime":"2016-09-12T11:26:30.906","customerName":"刘霞","detectionInfo":{"basis":"符合如而非","disposeType":"PICKUP"},"detectionNo":1,"detectionNum":"2016ST1","feedbackStatus":"UNTREATED","finalCopyField":1,"id":"57d6206661d0c878ffe2380a","invoiceInfo":{"name":"好多次而非"},"operatorStatus":"MANAGERCONFIRM","userId":"57cfdf3179d23f8f570f49fd"},{"chemicalCell":{"sampleName":"等军工而"},"copyNum":1,"createTime":"2016-09-12T11:28:18.932","customerName":"刘霞","detectionInfo":{"basis":"觉得偶地位","disposeType":"PICKUP"},"detectionNo":2,"detectionNum":"2016ST2","feedbackStatus":"UNTREATED","finalCopyField":1,"id":"57d620d761d0c878ffe2380b","invoiceInfo":{"name":"发热头诶"},"operatorStatus":"INIT","userId":"57cfdf3179d23f8f570f49fd"}]},"errno":0}
          */

解释:
     注释里面开头使用#
     * 第一行或几行用来描述该方法,开头可以不用#
     *#exclude :表示排除某个参数,使用参数名 ;*表示排除所有 ;名字间用逗号","隔开
     *#include :表示选中某个参数,使用参数名 ;*表示选中所有 ;名字间用逗号","隔开;默认include优先exclude
     *#version :方法的版本号,没写的话是不可以调用的 ,前端显示最新版本的
     *#return :方法返回值描述


执行该接口方法返回的内容有:
    class :类名
    methodName :方法名
    returnType :方法返回类型
    return  :方法返回例子
    nameDes  :方法功能名
    example  :用于写传参例子
    demo    :GET -> http://localhost:8080/custom/procing/
    paramTypeList :需要的参数和参数类型
    paramExclude : 排除的参数名
    paramInclude : 需要的参数名
    desc :用户的描述
    若传入的参数是用户自定义的实体,且该实体使用了相关注解,则会有如下返回:
    xxObjectentityParamAttr :包括(attributeName:属性名,attributeDetail:属性描述)
    ....

类描述:#classDes
   /**
   *
   * #classNameDes 类中文名
   * #classDes 这是xx类,用于描述
   *
   */

包描述:
   在该包下键一个package-info.java文件,里面包含
   /**
   *
   * #packageName 包中文名
   * #packageInfo 这是一个xx包
   *
   */

方法上一定要写注解@MethodVersionAnnotation(version ="对应注释中的版本号")



