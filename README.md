
# 设计模式

- 模板
  通用的能力逻辑放在抽象类中,子类支持定制个性化的能力
  
  
  
大的思路是底层使用Netty进行通信,屏蔽掉数据层的拆包粘包问题
API尽量使用自己定义的API屏蔽掉Netty的API,降低学习成本。


# 模块解释


- protocol   HTTP协议/自定义协议
- server     服务端
- client     客户端
- exception  异常处理
- channel    通道模型,对Netty通道进行增强
- config     为RPC风格通信提供更加简单的API
- signature  签名信息 [废弃,不使用]
- proxy      代理     [废弃]
- pool       池化工具




