package com.vertexcubed.ad_infinitum.common.multiblock.data;

import com.mojang.serialization.Codec;
import com.vertexcubed.ad_infinitum.common.registry.MultiblockDataRegistry;
import net.minecraft.util.ExtraCodecs;


public interface MultiblockData {


    Codec<MultiblockData> CODEC = ExtraCodecs.lazyInitializedCodec(() -> MultiblockDataRegistry.MULTIBLOCK_DATA_REGISTRY.get().getCodec()).dispatch(MultiblockData::getType, MultiblockData.Type::codec);

    MultiblockData.Type<?> getType();






    record Type<S extends MultiblockData>(Codec<S> codec) {}
}
