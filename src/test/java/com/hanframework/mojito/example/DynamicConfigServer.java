package com.hanframework.mojito.example;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.hanframework.mojito.config.Installer;
import com.hanframework.mojito.protocol.http.HttpMethod;
import com.hanframework.mojito.protocol.http.HttpResponseFacade;

import java.util.Map;
import java.util.Properties;

/**
 * 枚举是天然的单例
 *
 * @author liuxin
 * 2020-09-14 01:31
 */
public enum DynamicConfigServer {

    START(12321);

    private final Properties properties;

    DynamicConfigServer(int port) {
        this.properties = new Properties();
        this.createServer(port);
    }

    private void createServer(int port) {
        Installer.httpServer((channelContext, request) -> {
            String requestURI = request.getRequestURI();
            if (request.method() != HttpMethod.POST) {
                return HttpResponseFacade.JSON("{\"code\":-1,\"message\":\"仅仅支持POST方式提交\"}");
            }
            String type = request.getHeader("type");
            if (StrUtil.isBlank(type)) {
                return HttpResponseFacade.JSON("{\"code\":-1,\"message\":\"请确定操作类型\"}");
            }
            Map<String, String> requestParams = request.getRequestParams();
            if (StrUtil.equalsIgnoreCase(type, "update")) {
                update(requestParams);
            }
            return HttpResponseFacade.JSON(JSONUtil.toJsonStr(properties));
        }).startAsync(port);
    }

    public Properties getProperties() {
        return properties;
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public Object getProperty(String key, Object value) {
        return properties.getOrDefault(key, value);
    }

    private void update(Map<String, String> requestParams) {
        properties.putAll(requestParams);
    }

    private void remove(Map<String, String> requestParams) {
        String removeKey = requestParams.get("removeKey");
        properties.remove(removeKey);
    }

    public static void main(String[] args) throws Exception {
        /**
         * curl --location --request POST 'http://127.0.0.1:12321/update' \
         * --header 'Connection: keep-alive' \
         * --header 'type: update' \
         * --form 'name=mojito77'
         * 1. 启动时候如果是null,可以从中间件中去读取。
         */
        System.out.println(DynamicConfigServer.START.getProperty("name"));
        while (true) {
            Thread.sleep(1000);
            System.out.println(DynamicConfigServer.START.getProperty("name"));
        }
    }
}
