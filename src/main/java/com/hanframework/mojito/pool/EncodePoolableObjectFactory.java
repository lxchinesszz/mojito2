package com.hanframework.mojito.pool;

import com.hanframework.mojito.protocol.ChannelEncoder;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.util.function.Supplier;

/**
 * 编码器池化工厂
 *
 * @author liuxin
 * 2020-08-21 22:53
 */
public class EncodePoolableObjectFactory extends BasePooledObjectFactory<ChannelEncoder> {

    private Supplier<ChannelEncoder> encoderSupplier;

    public EncodePoolableObjectFactory(Supplier<ChannelEncoder> encoderSupplier) {
        this.encoderSupplier = encoderSupplier;
    }

    @Override
    public ChannelEncoder create() throws Exception {
        return encoderSupplier.get();
    }

    @Override
    public PooledObject<ChannelEncoder> wrap(ChannelEncoder encoderSupplier) {
        return new DefaultPooledObject<>(encoderSupplier);
    }
}
