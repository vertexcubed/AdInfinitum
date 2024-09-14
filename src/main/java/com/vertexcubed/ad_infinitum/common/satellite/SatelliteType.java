package com.vertexcubed.ad_infinitum.common.satellite;

import net.minecraft.nbt.CompoundTag;

public class SatelliteType<T extends Satellite> {


    private final SatelliteFactory<T> factory;
    public SatelliteType(SatelliteFactory<T> factory) {
        this.factory = factory;
    }

    public T createSatellite(CompoundTag tag) {
        T s = factory.create(this);
        s.load(tag);
        return s;
    }


    public interface SatelliteFactory<T extends Satellite> {
        T create(SatelliteType<T> type);
    }
}
