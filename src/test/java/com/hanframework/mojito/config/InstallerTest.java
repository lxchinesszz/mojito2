package com.hanframework.mojito.config;

import com.hanframework.mojito.client.Client;
import com.hanframework.mojito.future.MojitoFuture;
import com.hanframework.mojito.protocol.Factory;
import com.hanframework.mojito.protocol.mojito.model.RpcRequest;
import com.hanframework.mojito.protocol.mojito.model.RpcResponse;
import org.junit.Test;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;


/**
 * 点对点模式
 * - 支持同步和异步
 *
 * @author liuxin
 * 2020-08-23 21:34
 */
public class InstallerTest implements Serializable {


    @Test
    public void clientTest() throws Exception {
        Factory<RpcRequest, RpcResponse> codecFactory = Installer.modules(RpcRequest.class, RpcResponse.class).create();
        //构建客户端并链接
        Client<RpcRequest, RpcResponse> client = codecFactory.getClient("127.0.0.1", 12306);
        MojitoFuture<RpcResponse> async = client.sendAsync(new RpcRequest());
        //异步获取数据,最多等待5秒
        System.out.println(async.get(5, TimeUnit.SECONDS));
        //关闭连接
        client.close();
    }


    @Test
    public void serverTest() {
        Installer.server(RpcRequest.class, RpcResponse.class).serverHandler((channel, rpcRequest) -> {
            RpcResponse response = new RpcResponse();
            response.setMessage("hello2");
            return response;
        }).create().start(12306);
    }


    @Test
    public void mqTest() {
        //1. 服务端放一个路由键。当有人往指定的routeKey消息。
        //2. 服务端将消息推送给订阅的人,然后将这个消息的状态指定为已发送状态,等待消费端来确定是否消费成功,如果成功就将消息状态改为已处理，然后移出。
        //3. 如何消费端没有消费成功,或者没有给通知。 定时之后将消息状态改为重试状态。


        //4. 消息的处理逻辑抽离出来。后面可以实现通过机器方式读取,类似kafka方式
    }


    @Test
    public void testRpcServer() {
        Installer.ServerCreator<RpcRequest, RpcResponse> serverCreator = Installer.server(RpcRequest.class, RpcResponse.class)
                .serverHandler((channel, request) -> {
                    RpcResponse response = new RpcResponse();
                    try {
                        //1. 拿到要执行的类
                        Class<?> serviceType = request.getServiceType();
                        //2. 拿到要执行类的方法
                        Method method = serviceType.getMethod(request.getMethodName(), request.getArgsType());
                        Constructor<?> constructor = serviceType.getConstructor();
                        Object instance = constructor.newInstance();
                        //3. 反射执行结果
                        Object invoke = method.invoke(instance, request.getArgs());
                        response.setSuccess(true);
                        response.setResult(invoke);
                    } catch (Exception e) {
                        e.printStackTrace();
                        response.setSuccess(false);
                    }
                    return response;
                });
        serverCreator.create().start(8084);
    }


    @Test
    public void testRpcClient() throws Exception {
        Client<RpcRequest, RpcResponse> client = Installer.client(RpcRequest.class, RpcResponse.class).create();
        client.connect("127.0.0.1", 8084);
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setServiceType(UserInvoker.class);
        rpcRequest.setMethodName("getName");
        rpcRequest.setArgsType(new Class[]{String.class});
        rpcRequest.setArgs(new Object[]{"欢迎关注程序猿升级课"});
        MojitoFuture<RpcResponse> future = client.sendAsync(rpcRequest);
        System.out.println(future.get());
    }

}