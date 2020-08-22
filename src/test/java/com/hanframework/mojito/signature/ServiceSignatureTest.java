package com.hanframework.mojito.signature;


import com.hanframework.mojito.protocol.ServiceURL;
import com.hanframework.mojito.signature.method.MethodSignature;
import com.hanframework.mojito.signature.service.PrototypeServiceServiceSignature;
import com.hanframework.mojito.signature.service.ServiceSignature;
import com.hanframework.mojito.signature.service.SignatureBuilder;
import com.hanframework.mojito.signature.service.SingletonServiceServiceSignature;
import com.hanframework.mojito.test.pojo.User;
import com.hanframework.mojito.test.service.UserService;
import org.junit.Test;

import java.io.Serializable;
import java.util.List;

/**
 * @author liuxin
 * 2020-07-31 16:53
 */
public class ServiceSignatureTest implements Serializable {


    @Test
    public void test() {
        //构建一个原型模式的方式1
        ServiceSignature prototypeServiceServiceSignature = new PrototypeServiceServiceSignature<>(User.class, "1.0.1", User::new);
        //构建一个原型模式的方式2
        prototypeServiceServiceSignature = SignatureBuilder.buildPrototypeServiceSignature(User.class, "1.0.1", User::new);


        User singleUser = new User();
        ServiceSignature signatureServiceServiceSignature = SignatureBuilder.buildSingletonSignature(User.class, "1.0.1", singleUser);
        signatureServiceServiceSignature = new SingletonServiceServiceSignature<>(User.class, "1", singleUser);
    }

    @Test
    public void testExportServiceURL() {
        SingletonServiceServiceSignature<UserService> signature = SignatureBuilder.buildSingletonSignature(UserService.class, "1", new UserService());
        List<MethodSignature> export = signature.getMethodSignature();
        System.out.println(export);
    }
}