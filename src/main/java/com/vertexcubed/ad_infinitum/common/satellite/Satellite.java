package com.vertexcubed.ad_infinitum.common.satellite;

import com.vertexcubed.ad_infinitum.AdInfinitum;
import com.vertexcubed.ad_infinitum.common.registry.SatelliteRegistry;
import com.vertexcubed.ad_infinitum.common.satellite.frequency.Frequency;
import com.vertexcubed.ad_infinitum.common.satellite.frequency.FrequencyDataType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

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
        this.dataType = FrequencyDataType.PLANET_DATA;
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

    public abstract List<InfoField<?>> getInformation();

    public static Satellite createFromTag(CompoundTag tag) {
        SatelliteType<?> type = SatelliteRegistry.SATELLITE_TYPE_REGISTRY.get().getValue(new ResourceLocation(tag.getString("type")));
        return type.createSatellite(tag);
    }

    public static class InfoField<T> {

        private Component name;
        private Component type;
        private boolean editable = false;
        private Supplier<T> getter = null;
        private Consumer<T> setter = null;

        public InfoField(Component name, Component type) {
            this.name = name;
            this.type = type;
        }

        public InfoField<T> editable(Supplier<T> sup, Consumer<T> con) {
            this.editable = true;
            this.getter = sup;
            this.setter = con;
            return this;
        }

        public Component getName() {
            return name;
        }

        public Component getType() {
            return type;
        }

        public boolean isEditable() {
            return editable;
        }

        public Supplier<T> getGetter() {
            return getter;
        }

        public Consumer<T> getSetter() {
            return setter;
        }
    }
}
