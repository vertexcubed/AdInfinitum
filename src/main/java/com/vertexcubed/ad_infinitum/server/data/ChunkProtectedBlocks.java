package com.vertexcubed.ad_infinitum.server.data;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChunkProtectedBlocks implements INBTSerializable<CompoundTag> {

    public static final Capability<ChunkProtectedBlocks> CAP = CapabilityManager.get(new CapabilityToken<>() {});

    private Map<Long, UUID> blockMap = new HashMap<>();
    private final LevelChunk owner;
    public ChunkProtectedBlocks(LevelChunk owner) {
        this.owner = owner;
    }

    public boolean contains(long blockPos) {
        return blockMap.containsKey(blockPos);
    }

    public boolean contains(BlockPos pos) {
        return contains(pos.asLong());
    }

    public void put(BlockPos pos, UUID uuid) {
        blockMap.put(pos.asLong(), uuid);
        owner.setUnsaved(true);
    }
    public void remove(BlockPos pos) {
        blockMap.remove(pos.asLong());
        owner.setUnsaved(true);
    }

    public Map<Long, UUID> getBlockMap() {
        return blockMap;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        ListTag list = new ListTag();
        blockMap.forEach((pos, uuid) -> {
            CompoundTag entry = new CompoundTag();
            entry.putLong("key", pos);
            entry.putUUID("uuid", uuid);
            list.add(entry);
        });
        tag.put("blocks", list);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        blockMap.clear();
        ListTag list = tag.getList("blocks", ListTag.TAG_COMPOUND);
        list.forEach(entry -> {
            blockMap.put(((CompoundTag) entry).getLong("key"), ((CompoundTag) entry).getUUID("value"));
        });
    }

    @Override
    public String toString() {
        return "ChunkProtectedBlocks{" +
                "blockMap=" + blockMap +
                '}';
    }
}
