package com.vertexcubed.ad_infinitum.server.capability;

import com.vertexcubed.ad_infinitum.common.item.SatelliteItem;
import com.vertexcubed.ad_infinitum.common.satellite.Satellite;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.vertexcubed.ad_infinitum.AdInfinitum.modLoc;

public class SatelliteItemStorageProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    public static final ResourceLocation ID = modLoc("satellite");
    private SatelliteItemStorage satelliteStorage = null;
    private LazyOptional<SatelliteItemStorage> lazyOptional = LazyOptional.of(this::getOrCreate);


    public SatelliteItemStorageProvider() {
    }

    public SatelliteItemStorage getOrCreate() {
        if(satelliteStorage == null) {
            this.satelliteStorage = new SatelliteItemStorage();
        }
        return satelliteStorage;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == SatelliteItemStorage.CAP) {
            return lazyOptional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        return getOrCreate().serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        getOrCreate().deserializeNBT(tag);
    }
}
