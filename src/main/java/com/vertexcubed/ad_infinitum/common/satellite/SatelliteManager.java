package com.vertexcubed.ad_infinitum.common.satellite;

import com.vertexcubed.ad_infinitum.common.satellite.frequency.Frequency;
import com.vertexcubed.ad_infinitum.server.data.SatelliteSavedData;
import net.minecraft.server.level.ServerLevel;

import java.util.*;

public class SatelliteManager {

    private static final Map<UUID, Satellite> satellites = new HashMap<>();

    /**
     * Read from disk. Only call on the SERVER.
     */
    public static void init(ServerLevel serverLevel) {

    }

    /**
     * Only ever call this on the SERVER.
     */
    public static void addSatellite(ServerLevel serverLevel, Satellite satellite) {
        satellites.put(satellite.getId(), satellite);
        //Save to disk
        getData(serverLevel).addSatellite(satellite);
        //sync to players
    }

    /**
     * This is side safe
     */
    public static Satellite getSatellite(UUID id) {
        return satellites.get(id);
    }

    /**
     * This is side safe. List is immutable.
     */
    public static List<Satellite> getSatellitesOnFrequency(Frequency frequency) {
        return satellites.values().stream().filter(s -> {
            if(s.getFrequency() == null) return false;
            return s.getFrequency().equals(frequency);
        }).toList();
    }

    /**
     * Only ever call this on the SERVER.
     */
    public static void removeSatellite(ServerLevel serverLevel, Satellite frequency) {
        removeSatellite(serverLevel, frequency.getId());
    }

    /**
     * Only ever call this on the SERVER.
     */
    public static void removeSatellite(ServerLevel serverLevel, UUID id) {
        satellites.remove(id);
        //save to disk
        getData(serverLevel).removeSatellite(id);
        //sync to players
    }

    private static SatelliteSavedData getData(ServerLevel serverLevel) {
        return SatelliteSavedData.getOrLoad(serverLevel.getServer());
    }

}
