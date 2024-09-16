package com.vertexcubed.ad_infinitum.common.multiblock.data;

import com.mojang.serialization.Codec;
import com.vertexcubed.ad_infinitum.common.multiblock.Multiblock;
import com.vertexcubed.ad_infinitum.common.registry.MultiblockDataRegistry;
import net.minecraft.util.ExtraCodecs;


public abstract class MultiblockData {

    public static final Codec<MultiblockData> CODEC = ExtraCodecs.lazyInitializedCodec(() -> MultiblockDataRegistry.MULTIBLOCK_DATA_REGISTRY.get().getCodec()).dispatch(MultiblockData::getType, MultiblockData.Type::codec);

    protected Multiblock multiblock;

    public void init(Multiblock multiblock) {
        this.multiblock = multiblock;
    }
    public abstract void setupData(int x, int y, int z, String key);

    public abstract MultiblockData.Type<?> getType();





    public record Type<S extends MultiblockData>(Codec<S> codec) {}
}
