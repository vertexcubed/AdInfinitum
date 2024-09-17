package com.vertexcubed.ad_infinitum.server.capability;

import com.vertexcubed.ad_infinitum.common.satellite.Satellite;
import com.vertexcubed.ad_infinitum.server.data.AdInfinitumPlanetData;
import com.vertexcubed.ad_infinitum.server.network.ClientboundSyncSatelliteItemStoragePacket;
import com.vertexcubed.ad_infinitum.server.network.ClientboundSyncUnlandablePlanetsPacket;
import com.vertexcubed.ad_infinitum.server.network.PacketHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;

public class SatelliteItemStorage implements INBTSerializable<CompoundTag> {

    public static final Capability<SatelliteItemStorage> CAP = CapabilityManager.get(new CapabilityToken<>() {});

    private Satellite satellite;

    public SatelliteItemStorage() {
    }

    /**
     * Should only set from player inv
     */
    public void setSatellite(Satellite satellite, Player player, int slotIndex) {
        this.satellite = satellite;
        syncToClient(player, slotIndex);
    }

    public Satellite getSatellite() {
        return satellite;
    }

    private void syncToClient(Player player, int slotIndex) {
        if(!player.level().isClientSide) {
            PacketHandler.CHANNEL.sendToAllPlayers(new ClientboundSyncSatelliteItemStoragePacket(player.getId(), slotIndex, this.serializeNBT()), player.getServer());
        }
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        if(satellite != null) {
            tag.put("satellite", satellite.save(new CompoundTag()));
        }

        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        if(tag.contains("satellite")) {
            this.satellite = Satellite.createFromTag(tag.getCompound("satellite"));
        }
    }
}
