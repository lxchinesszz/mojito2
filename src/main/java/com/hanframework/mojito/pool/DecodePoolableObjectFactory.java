package com.hanframework.mojito.pool;

import com.hanframework.mojito.protocol.ChannelDecoder;
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
public class DecodePoolableObjectFactory extends BasePooledObjectFactory<ChannelDecoder> {

    private Supplier<ChannelDecoder> decoderSupplier;

    public DecodePoolableObjectFactory(Supplier<ChannelDecoder> decoderSupplier) {
        this.decoderSupplier = decoderSupplier;
    }

    @Override
    public ChannelDecoder create() throws Exception {
        return decoderSupplier.get();
    }

    @Override
    public PooledObject<ChannelDecoder> wrap(ChannelDecoder channelDecoder) {
        return new DefaultPooledObject<>(channelDecoder);
    }
}
