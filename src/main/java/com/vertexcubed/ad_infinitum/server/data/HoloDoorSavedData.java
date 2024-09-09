package com.vertexcubed.ad_infinitum.server.data;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.*;

public class HoloDoorSavedData extends SavedData {

    public static final String DATA_ID = "ad_infinitum_holo_door_protected_blocks";

    private final Map<UUID, BlockPos> holoDoors;
    public HoloDoorSavedData() {
        this.holoDoors = new HashMap<>();
    }

    public HoloDoorSavedData(CompoundTag tag) {
        holoDoors = new HashMap<>();
        ListTag list = tag.getList(DATA_ID, ListTag.TAG_COMPOUND);
        list.forEach(entry -> {
            holoDoors.put(((CompoundTag) entry).getUUID("key"), BlockPos.of(((CompoundTag) entry).getLong("value")));
        });
    }

    public boolean containsKey(UUID uuid) {
        return holoDoors.containsKey(uuid);
    }

    public BlockPos get(UUID uuid) {
        return holoDoors.get(uuid);
    }

    public BlockPos add(UUID uuid, BlockPos pos) {
        setDirty();
        return holoDoors.put(uuid, pos);
    }
    public BlockPos remove(UUID uuid) {
        setDirty();
        return holoDoors.remove(uuid);
    }

//    public Map<UUID, BlockPos> getProtectedBlocks() {
//        return protectedBlocks;
//    }


    @Override
    public CompoundTag save(CompoundTag tag) {
        ListTag list = new ListTag();
        holoDoors.forEach((uuid, pos) -> {
            CompoundTag entry = new CompoundTag();
            entry.putUUID("key", uuid);
            entry.putLong("value", pos.asLong());
            list.add(entry);
        });
        tag.put("blocks", list);
        return tag;
    }

    public static HoloDoorSavedData load(CompoundTag tag) {
        return new HoloDoorSavedData(tag.getCompound(DATA_ID));
    }

    public static HoloDoorSavedData getOrLoad(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(HoloDoorSavedData::load, HoloDoorSavedData::new, DATA_ID);
    }
}
