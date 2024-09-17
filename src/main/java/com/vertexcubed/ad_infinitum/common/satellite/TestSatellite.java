package com.vertexcubed.ad_infinitum.common.satellite;

import com.vertexcubed.ad_infinitum.common.registry.SatelliteRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.UUID;

public class TestSatellite extends Satellite {

    public TestSatellite(UUID id, UUID owner) {
        super(SatelliteRegistry.TEST.get(), id, owner);
    }

    /**
     * Should only be executed on the client.
     */
    @Override
    public List<Satellite.InfoField<?>> getInformation() {
        String freqName = (this.frequency == null) ? "None" : this.frequency.getName();
        return List.of(
                new InfoField<>(Component.literal("Name"), Component.literal(this.getName())),
                new InfoField<>(Component.literal("Type"), Component.literal(SatelliteRegistry.SATELLITE_TYPE_REGISTRY.get().getKey(this.type).toString())),
                new InfoField<>(Component.literal("Freq"), Component.literal(freqName)),
                new InfoField<>(Component.literal("Owner"), Minecraft.getInstance().level.getPlayerByUUID(this.owner).getDisplayName())
        );
    }

    @Override
    protected CompoundTag saveAdditional(CompoundTag tag) {
        return tag;
    }

    @Override
    protected void loadAdditional(CompoundTag tag) {

    }
}
