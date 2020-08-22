
# 设计模式

- 模板
  通用的能力逻辑放在抽象类中,子类支持定制个性化的能力
  
  
  
大的思路是底层使用Netty进行通信,但是API尽量使用自己定义的API,降低开发成本。


- signature  签名信息
- server     服务daunt
- client     客户端
- proxy      代理
- protocol   定制的协议
- handler    逻辑处理内
- exception  异常处理
- channel    通道模型,数据信息  



1. 协议层只处理二进制数据与模型转换
