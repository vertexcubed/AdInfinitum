package com.vertexcubed.ad_infinitum.server.network;

import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.ClientboundPacketType;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import com.vertexcubed.ad_infinitum.server.capability.SatelliteItemStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

import static com.vertexcubed.ad_infinitum.AdInfinitum.modLoc;

public record ClientboundSyncSatelliteItemStoragePacket(int ownerId, int slot, CompoundTag storage) implements Packet<ClientboundSyncSatelliteItemStoragePacket> {

    public static final Type TYPE = new Type();

    @Override
    public PacketType<ClientboundSyncSatelliteItemStoragePacket> type() {
        return TYPE;
    }

    private static class Type implements ClientboundPacketType<ClientboundSyncSatelliteItemStoragePacket> {
        @Override
        public Runnable handle(ClientboundSyncSatelliteItemStoragePacket message) {
            return () -> {
                Player player = (Player) Minecraft.getInstance().level.getEntity(message.ownerId);
                ItemStack stack = player.getInventory().getItem(message.slot);
                stack.getCapability(SatelliteItemStorage.CAP).ifPresent(itemStorage -> {
                    itemStorage.deserializeNBT(message.storage);
                });
            };
        }

        @Override
        public Class<ClientboundSyncSatelliteItemStoragePacket> type() {
            return ClientboundSyncSatelliteItemStoragePacket.class;
        }

        @Override
        public ResourceLocation id() {
            return modLoc("sync_satellite_item_storage");
        }

        @Override
        public void encode(ClientboundSyncSatelliteItemStoragePacket message, FriendlyByteBuf buffer) {
            buffer.writeInt(message.ownerId);
            buffer.writeInt(message.slot);
            buffer.writeNbt(message.storage);
        }

        @Override
        public ClientboundSyncSatelliteItemStoragePacket decode(FriendlyByteBuf buffer) {
            return new ClientboundSyncSatelliteItemStoragePacket(buffer.readInt(), buffer.readInt(), buffer.readNbt());
        }
    }
}
