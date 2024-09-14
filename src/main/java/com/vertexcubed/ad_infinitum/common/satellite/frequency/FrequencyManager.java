package com.vertexcubed.ad_infinitum.common.satellite.frequency;

import com.vertexcubed.ad_infinitum.server.data.SatelliteSavedData;
import net.minecraft.server.level.ServerLevel;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FrequencyManager {

    private static final Map<UUID, Frequency> frequencies = new HashMap<>();


    /**
     * Read from disk. Only call on the SERVER.
     */
    public static void init(ServerLevel serverLevel) {
        frequencies.clear();
        frequencies.putAll(SatelliteSavedData.getOrLoad(serverLevel.getServer()).getFrequencies());
    }

    /**
     * Only ever call this on the SERVER.
     */
    public static void addFrequency(ServerLevel serverLevel, Frequency frequency) {
        frequencies.put(frequency.getId(), frequency);
        //Save to disk
        getData(serverLevel).addFrequency(frequency);
        //sync to players
    }

    /**
     * This is side safe
     */
    public static Frequency getFrequency(UUID id) {
        return frequencies.get(id);
    }

    /**
     * Only ever call this on the SERVER.
     */
    public static void removeFrequency(ServerLevel serverLevel, Frequency frequency) {
        removeFrequency(serverLevel, frequency.getId());
    }

    /**
     * Only ever call this on the SERVER.
     */
    public static void removeFrequency(ServerLevel serverLevel, UUID id) {
        frequencies.remove(id);
        //save to disk
        getData(serverLevel).removeFrequency(id);
        //sync to players
    }

    private static SatelliteSavedData getData(ServerLevel serverLevel) {
        return SatelliteSavedData.getOrLoad(serverLevel.getServer());
    }
}