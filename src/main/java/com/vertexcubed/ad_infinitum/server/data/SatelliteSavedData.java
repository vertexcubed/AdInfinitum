package com.vertexcubed.ad_infinitum.server.data;

import com.vertexcubed.ad_infinitum.AdInfinitum;
import com.vertexcubed.ad_infinitum.common.registry.SatelliteRegistry;
import com.vertexcubed.ad_infinitum.common.satellite.Satellite;
import com.vertexcubed.ad_infinitum.common.satellite.SatelliteType;
import com.vertexcubed.ad_infinitum.common.satellite.frequency.Frequency;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SatelliteSavedData extends SavedData {

    public static final String DATA_ID = "ad_infinitum_frequency_data";

    private final Map<UUID, Frequency> frequencies;
    private final Map<UUID, Satellite> satellites;

    public SatelliteSavedData() {
        AdInfinitum.LOGGER.info("Creating new satellite data");
        frequencies = new HashMap<>();
        satellites = new HashMap<>();
    }

    public SatelliteSavedData(CompoundTag tag) {
        AdInfinitum.LOGGER.info(tag.toString());
        frequencies = new HashMap<>();
        ListTag list = tag.getList("frequencies", ListTag.TAG_COMPOUND);
        list.forEach(entry -> {
            frequencies.put(((CompoundTag) entry).getUUID("key"), Frequency.CODEC.parse(NbtOps.INSTANCE, tag.get("value")).getOrThrow(false, AdInfinitum.LOGGER::error));
        });
        satellites = new HashMap<>();
        ListTag list2 = tag.getList("satellites", ListTag.TAG_COMPOUND);
        list2.forEach(entry -> {
            CompoundTag value = ((CompoundTag) entry).getCompound("value");
            SatelliteType<?> type = SatelliteRegistry.SATELLITE_TYPE_REGISTRY.get().getValue(new ResourceLocation(value.getString("type")));
            if(type == null) {
                throw new NullPointerException("Undefined satellite type: " + value.getString("type"));
            }
            Satellite s = type.createSatellite(value);
            satellites.put(((CompoundTag) entry).getUUID("key"), s);
        });
    }

    public Map<UUID, Frequency> getFrequencies() {
        return frequencies;
    }

    public void addFrequency(Frequency frequency) {
        frequencies.putIfAbsent(frequency.getId(), frequency);
        setDirty();
    }

    public void addSatellite(Satellite satellite) {
        satellites.putIfAbsent(satellite.getId(), satellite);
        setDirty();
    }

    public void removeFrequency(Frequency frequency) {
        removeFrequency(frequency.getId());
    }

    public void removeFrequency(UUID uuid) {
        frequencies.remove(uuid);
        setDirty();
    }


    public void removeSatellite(Satellite satellite) {
        removeSatellite(satellite.getId());
    }

    public void removeSatellite(UUID uuid) {
        satellites.remove(uuid);
        setDirty();
    }

    public void clearSatellites() {
        satellites.clear();
        setDirty();
    }

    public Map<UUID, Satellite> getSatellites() {
        return satellites;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        AdInfinitum.LOGGER.info("Saving satellite data to disk!");
        ListTag list1 = new ListTag();
        frequencies.forEach((uuid, frequency) -> {
            CompoundTag entry = new CompoundTag();
            entry.putUUID("key", uuid);
            entry.put("value", Frequency.CODEC.encodeStart(NbtOps.INSTANCE, frequency).getOrThrow(false, AdInfinitum.LOGGER::error));
            list1.add(entry);
        });
        tag.put("frequencies", list1);
        ListTag list2 = new ListTag();
        satellites.forEach((uuid, satellite) -> {
            CompoundTag entry = new CompoundTag();
            entry.putUUID("key", uuid);
            entry.put("value", satellite.save(new CompoundTag()));
            list2.add(entry);
        });
        tag.put("satellites", list2);
        return tag;
    }

    public static SatelliteSavedData load(CompoundTag tag) {
        AdInfinitum.LOGGER.info("Loading satellite data from disk!");
        AdInfinitum.LOGGER.info(tag.toString());
        return new SatelliteSavedData(tag);
    }

    public static SatelliteSavedData getOrLoad(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(SatelliteSavedData::load, SatelliteSavedData::new, DATA_ID);
    }
}
