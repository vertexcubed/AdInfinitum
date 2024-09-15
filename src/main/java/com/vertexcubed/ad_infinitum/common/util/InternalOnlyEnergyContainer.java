package com.vertexcubed.ad_infinitum.common.util;

import earth.terrarium.botarium.common.energy.impl.SimpleEnergyContainer;
import net.minecraft.util.Mth;

/**
 * An energy container that can only be inserted/extracted from internally. Mainly for use in multiblocks
 */
public class InternalOnlyEnergyContainer extends SimpleEnergyContainer {

    public InternalOnlyEnergyContainer(int maxCapacity) {
        super(maxCapacity);
    }

    public InternalOnlyEnergyContainer(long maxCapacity, long maxTransfer) {
        super(maxCapacity, maxTransfer, maxTransfer);
    }

    @Override
    public long maxExtract() {
        return 0;
    }

    @Override
    public boolean allowsExtraction() {
        return false;
    }

    @Override
    public long extractEnergy(long maxAmount, boolean simulate) {
        return 0;
    }

    @Override
    public long internalExtract(long maxAmount, boolean simulate) {
        long extracted = (long) Mth.clamp(maxAmount, 0, getStoredEnergy());
        if (simulate) return extracted;
        this.setEnergy(getStoredEnergy() - extracted);
        return extracted;
    }

    @Override
    public long maxInsert() {
        return 0;
    }

    @Override
    public boolean allowsInsertion() {
        return false;
    }

    @Override
    public long insertEnergy(long maxAmount, boolean simulate) {
        return 0;
    }

    @Override
    public long internalInsert(long maxAmount, boolean simulate) {
        long inserted = (long) Mth.clamp(maxAmount, 0, getMaxCapacity() - getStoredEnergy());
        if (simulate) return inserted;
        this.setEnergy(getStoredEnergy() + inserted);
        return inserted;
    }
}
