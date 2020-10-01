
[![Build Status](https://travis-ci.org/lxchinesszz/mojito.svg?branch=master)](https://travis-ci.org/github/lxchinesszz/mojito)


Welcome to the mojito wiki! 🎉

[Mojito Framework](https://mojito.springlearn.cn/)
  
mojito的定位是通信层框架,其本质是基于Netty进行二次封装,提供更加简单的API,方便开发者进行调用。 如果你要写一个通信类的组件，但是又不希望引入web容器，或者rpc之类的框架。此时mojito就是最佳选择,因为它提供非常简单API可以快速的构建通信模块,代码量缺只有一点点的样子。当然如果你对Netty比较熟悉,也可以直接使用Netty进行开发。
[更多关于Mojito的定位](https://github.com/lxchinesszz/mojito/wiki/%E5%85%B3%E4%BA%8E-about)

## 一、设计思路 🚀

### 1. 架构图
![](https://img.springlearn.cn/blog/learn_1600333949000.png)

### 2. 核心类

![](https://camo.githubusercontent.com/2da1ed6b9d072d67ac1c3cd881294580ded538de/68747470733a2f2f696d672e737072696e676c6561726e2e636e2f626c6f672f6c6561726e5f313630303935333735363030302e706e67)

### 3. 使用示例

- [Protocol协议篇设计思路](https://github.com/lxchinesszz/mojito/wiki/Protocol%E5%8D%8F%E8%AE%AE%E7%AF%87%E8%AE%BE%E8%AE%A1%E6%80%9D%E8%B7%AF)
- [HTTP协议使用示例](https://github.com/lxchinesszz/mojito/wiki/HTTP%E5%8D%8F%E8%AE%AE%E4%BD%BF%E7%94%A8%E7%A4%BA%E4%BE%8B)
- [RPC协议使用示例](https://github.com/lxchinesszz/mojito/wiki/RPC%E5%8D%8F%E8%AE%AE%E4%BD%BF%E7%94%A8%E7%A4%BA%E4%BE%8B)
- [关于](https://github.com/lxchinesszz/mojito/wiki/%E5%85%B3%E4%BA%8E-about)


## 二、设计模式

- 模板-通用的能力逻辑放在抽象类中,子类支持定制个性化的能力
- SPI-可扩展
- 门面
- 适配器
- 监听器  
  


## 三、模块解释

- protocol   HTTP协议/自定义协议
- server     服务端
- client     客户端
- exception  异常处理
- channel    通道模型,对Netty通道进行增强
- config     为RPC风格通信提供更加简单的API
- signature  签名信息  [废弃,不使用]
- proxy      代理     [废弃]
- pool       池化工具



## 四、快速使用


## beta版本

### Gradle

`compile group: 'com.hanframework', name: 'mojito', version: '1.0.1-RELEASE'
`

### Maven

```
<dependency>
    <groupId>com.hanframework</groupId>
    <artifactId>mojito</artifactId>
    <version>1.0.1-RELEASE</version>
</dependency>

```


## 写在最后

本项目正在开发阶段，由于码主白天要上班，只有晚上、周末能挤点时间来敲敲代码，所以进度可能比较慢，文档、注释也不齐全。 
各位大侠就将就着看，但随着时间的推移。文档，注释，启动说明等码主我一定会补全的。 