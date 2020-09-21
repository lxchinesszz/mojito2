package com.hanframework.mojito.config;

import com.hanframework.kit.util.StopWatch;
import com.hanframework.mojito.client.Client;
import com.hanframework.mojito.future.MojitoFuture;
import com.hanframework.mojito.future.listener.MojitoListener;
import com.hanframework.mojito.protocol.mojito.model.RpcProtocolHeader;
import org.junit.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 自定义通信模型演示
 * 强调一下，都必须要继承RpcProtocolHeader
 *
 * @author liuxin
 * 2020-09-18 21:32
 */
public class Installer2Test implements Serializable {

    class RpcUserRequest extends RpcProtocolHeader {
        private String message;

        private RpcUserRequest(String message) {
            this.message = message;
        }

        @Override
        public String toString() {
            return "RpcUserRequest{" +
                    "message='" + message + '\'' +
                    '}';
        }
    }

    class RpcUserResponse extends RpcProtocolHeader {
        private String message;

        private RpcUserResponse(String message) {
            this.message = message;
        }

        @Override
        public String toString() {
            return "RpcUserResponse{" +
                    "message='" + message + '\'' +
                    '}';
        }
    }

    /**
     * 如何快速创建一个服务端
     * 1. 序列化协议
     * 2. 是否池化
     * 3. 单线程还是多线程
     * 4. 小数据缓存等待还是直接发送
     * 配置信息
     *
     * @throws Exception 未知异常
     */
    @Test
    public void serverTest() throws Exception {
        Installer.server(RpcUserRequest.class, RpcUserResponse.class)
                //这里接受客户端的请求,并返回一个相应
                .serverHandler((channel, rpcRequest) -> new RpcUserResponse("服务端返回: " + rpcRequest.message))
                .create()
                .start(12306);
    }

    /**
     * 如何快速构建一个客户端
     * <p>
     * 长时间没有心跳,如何处理。自动重连还是放弃。
     *
     * @throws Exception 未知异常
     */
    @Test
    public void clientTest() throws Exception {
        Client<RpcUserRequest, RpcUserResponse> client = Installer.client(RpcUserRequest.class, RpcUserResponse.class)
                .conncet("127.0.0.1", 12306);
        MojitoFuture<RpcUserResponse> mojitoFuture = client.sendAsync(new RpcUserRequest("关注微信公众号:程序猿升级课"));
//        System.out.println(mojitoFuture.get());

        mojitoFuture.addListener(new MojitoListener<RpcUserResponse>() {
            @Override
            public void onSuccess(RpcUserResponse result) throws Exception {
                System.out.println("监听模式:" + result);
            }

            @Override
            public void onThrowable(Throwable throwable) throws Exception {
                System.out.println("是否异常");
            }
        });
         while (true);
//        List<MojitoFuture<RpcUserResponse>> result = new ArrayList<>();
//        StopWatch stopWatch = new StopWatch("请求验证");
//        stopWatch.start();
//        for (int i = 0; i < 10000; i++) {
//            MojitoFuture<RpcUserResponse> async = client.sendAsync(new RpcUserRequest("I'am RpcUser_" + i));
//            result.add(async);
//        }
//        stopWatch.stop();
//        for (MojitoFuture<RpcUserResponse> rpcUserResponseMojitoFuture : result) {
//            System.out.println(rpcUserResponseMojitoFuture.get());
//        }
//        System.out.println(stopWatch.prettyPrint());
//        client.close();

    }


}