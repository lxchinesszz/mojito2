package com.hanframework.mojito.signature.service;

import com.hanframework.mojito.protocol.mojito.model.RpcRequest;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author liuxin
 * 2020-08-01 18:56
 */
public class DefaultSignatureManager implements SignatureManager {

    private Map<ServiceSignatureKey, SignatureHodler> serviceSignatureCacheMap = new ConcurrentHashMap<>();


    private static class ServiceSignatureKey {

        private Class<?> serviceType;

        private String version;

        private Class<?> returnType;

        private Class<?>[] argTypes;

        public ServiceSignatureKey(Class<?> serviceType, String version, Class<?> returnType, Class<?>[] argTypes) {
            this.serviceType = serviceType;
            this.version = version;
            this.returnType = returnType;
            this.argTypes = argTypes;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ServiceSignatureKey)) return false;
            ServiceSignatureKey that = (ServiceSignatureKey) o;
            return Objects.equals(serviceType, that.serviceType) &&
                    Objects.equals(version, that.version) &&
                    Objects.equals(returnType, that.returnType) &&
                    Arrays.equals(argTypes, that.argTypes);
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(serviceType, version, returnType);
            result = 31 * result + Arrays.hashCode(argTypes);
            return result;
        }
    }


    private ServiceSignatureKey createSignatureKey(RpcRequest rpcRequest) {
        Class<?> serviceType = rpcRequest.getServiceType();
        String version = rpcRequest.getVersion();
        Class returnType = rpcRequest.getReturnType();
        Class[] argsType = rpcRequest.getArgsType();
        return new ServiceSignatureKey(serviceType, version, returnType, argsType);
    }


    @Override
    public SignatureHodler fetchSignatureHodler(RpcRequest rpcRequest) {
        ServiceSignatureKey signatureKey = createSignatureKey(rpcRequest);
        return serviceSignatureCacheMap.get(signatureKey);
    }


}
