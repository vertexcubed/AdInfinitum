package com.vertexcubed.ad_infinitum.common.satellite;

import net.minecraft.nbt.CompoundTag;

import java.util.UUID;

public class SatelliteType<T extends Satellite> {


    private final SatelliteFactory<T> factory;
    public SatelliteType(SatelliteFactory<T> factory) {
        this.factory = factory;
    }

    public T createSatellite(CompoundTag tag) {
        T s = factory.create(tag.getUUID("id"), tag.getUUID("owner"));
        s.load(tag);
        return s;
    }


    public interface SatelliteFactory<T extends Satellite> {
        T create(UUID id, UUID owner);
    }
}
