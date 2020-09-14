
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
- signature  签名信息  [废弃,不使用]
- proxy      代理     [废弃]
- pool       池化工具



# Quick Start 

快速构建RPC风格协议服务端

# 远景

1. 支持http/https通信
2. 支持rpc风格的请求和调用
3. 提供简单的API给用户快速构建

## 构建一个服务端

```
    /**
     * 如何快速创建一个服务端
     * 1. 序列化协议
     * 2. 是否池化
     * 3. 单线程还是多线程
     * 4. 小数据缓存等待还是直接发送
     * 配置信息
     *
     * @throws Exception
     */
    @Test
    public void serverTest() throws Exception {
        Installer.server(RpcUserRequest.class, RpcUserResponse.class)
                //这里接受客户端的请求,并返回一个相应
                .serverHandler((channel, rpcRequest) -> new RpcUserResponse("服务端返回: " + rpcRequest.message))
                .create().start(12306);
    }
```

## 构建一个客户端

```
 /**
     * 如何快速构建一个客户端
     * <p>
     * 长时间没有心跳,如何处理。自动重连还是放弃。
     *
     * @throws Exception
     */
    @Test
    public void clientTest() throws Exception {
        Client<RpcUserRequest, RpcUserResponse> client = Installer.client(RpcUserRequest.class, RpcUserResponse.class)
                .conncet("127.0.0.1", 12306);

        MojitoFuture<RpcUserResponse> mojitoFuture = client.sendAsync(new RpcUserRequest("关注微信公众号:程序猿升级课"));
        System.out.println(mojitoFuture.get());
    }    
```

# HTTP协议

快读构建http协议服务端

```
@Test
    public void testHttpServer() {

        HttpCodecFactory httpCodecFactory = new HttpCodecFactory(new SubServerHandler<HttpRequestFacade, HttpResponseFacade>() {
            @Override
            public HttpResponseFacade handler(EnhanceChannel channel, HttpRequestFacade request) throws RemotingException {
                Map<String, String> requestParams = request.getRequestParams();
                System.out.println("请求参数:" + requestParams);
                return HttpResponseFacade.ok();
            }
        });
        Server server = httpCodecFactory.getServer();
        server.start(8080);
    }
```