package com.vertexcubed.ad_infinitum.common.satellite;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.vertexcubed.ad_infinitum.AdInfinitum;
import com.vertexcubed.ad_infinitum.common.registry.SatelliteRegistry;
import com.vertexcubed.ad_infinitum.common.satellite.frequency.Frequency;
import com.vertexcubed.ad_infinitum.common.satellite.frequency.FrequencyDataType;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.UUID;

@ParametersAreNonnullByDefault
public abstract class Satellite {


    protected final SatelliteType<?> type;
    protected final UUID id;
    protected String name;
    protected FrequencyDataType dataType;
    protected final UUID owner;
    @Nullable protected Frequency frequency;

    public Satellite(SatelliteType<?> type, UUID id, UUID owner) {
        this.type = type;
        this.id = id;
        this.name = "Unnamed Satellite";
        this.dataType = FrequencyDataType.ENERGY;
        this.owner = owner;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public UUID getOwner() {
        return owner;
    }

    public FrequencyDataType getDataType() {
        return dataType;
    }

    @Nullable
    public Frequency getFrequency() {
        return frequency;
    }

    @Override
    public String toString() {
        return this.getName() + " (" + this.getId() + ")";
    }

    public CompoundTag save(CompoundTag tag) {
        tag.putString("type", SatelliteRegistry.SATELLITE_TYPE_REGISTRY.get().getKey(type).toString());
        tag.putUUID("id", id);
        tag.putString("name", name);
        tag.put("dataType", FrequencyDataType.CODEC.encodeStart(NbtOps.INSTANCE, dataType).getOrThrow(false, AdInfinitum.LOGGER::error));
        tag.putUUID("owner", owner);
        if(frequency != null) {
            tag.put("frequency", Frequency.CODEC.encodeStart(NbtOps.INSTANCE, frequency).getOrThrow(false, AdInfinitum.LOGGER::error));
        }
        return saveAdditional(tag);
    }
    protected abstract CompoundTag saveAdditional(CompoundTag tag);

    public void load(CompoundTag tag) {
        this.name = tag.getString("name");
        this.dataType = FrequencyDataType.CODEC.parse(NbtOps.INSTANCE, tag.get("dataType")).getOrThrow(false, AdInfinitum.LOGGER::error);
        if(tag.contains("frequency")) {
            this.frequency = Frequency.CODEC.parse(NbtOps.INSTANCE, tag.get("frequency")).getOrThrow(false, AdInfinitum.LOGGER::error);
        }
        loadAdditional(tag);
    }
    protected abstract void loadAdditional(CompoundTag tag);
}
