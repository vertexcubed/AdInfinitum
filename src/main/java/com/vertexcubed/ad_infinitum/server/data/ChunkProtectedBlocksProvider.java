package com.vertexcubed.ad_infinitum.server.data;

import com.vertexcubed.ad_infinitum.AdInfinitum;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ChunkProtectedBlocksProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    public static final ResourceLocation ID = new ResourceLocation(AdInfinitum.MODID, "protected_blocks");
    private ChunkProtectedBlocks chunkProtectedBlocks = null;
    private final LazyOptional<ChunkProtectedBlocks> lazyBlocks = LazyOptional.of(this::getOrCreate);

    private final LevelChunk owner;
    public ChunkProtectedBlocksProvider(LevelChunk owner) {
        this.owner = owner;
    }

    public ChunkProtectedBlocks getOrCreate() {
        if(chunkProtectedBlocks == null) {
            return new ChunkProtectedBlocks(owner);
        }
        return chunkProtectedBlocks;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction direction) {
        if(capability == ChunkProtectedBlocks.CAP) {
            return lazyBlocks.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        return getOrCreate().serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag compoundTag) {
        getOrCreate().deserializeNBT(compoundTag);
    }
}
