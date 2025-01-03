package com.vertexcubed.ad_infinitum.server.network;

import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import com.teamresourceful.resourcefullib.common.network.base.ServerboundPacketType;
import com.vertexcubed.ad_infinitum.AdInfinitum;
import com.vertexcubed.ad_infinitum.common.blockentity.HoloDoorBlockEntity;
import com.vertexcubed.ad_infinitum.common.blockentity.SatelliteLauncherBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.function.Consumer;

import static com.vertexcubed.ad_infinitum.AdInfinitum.modLoc;

public record ServerboundLaunchSatellitesPacket(BlockPos pos) implements Packet<ServerboundLaunchSatellitesPacket> {

    public static final Type TYPE = new Type();

    @Override
    public PacketType<ServerboundLaunchSatellitesPacket> type() {
        return TYPE;
    }


    public static class Type implements ServerboundPacketType<ServerboundLaunchSatellitesPacket> {

        @Override
        public Consumer<Player> handle(ServerboundLaunchSatellitesPacket message) {
            return player -> {
                BlockEntity be = player.level().getBlockEntity(message.pos());
                if(!(be instanceof SatelliteLauncherBlockEntity launcher)) {
                    AdInfinitum.LOGGER.error("Cannot handle launch satellites packet, no BE!");
                    return;
                }
                launcher.startLaunching();

            };
        }

        @Override
        public Class<ServerboundLaunchSatellitesPacket> type() {
            return ServerboundLaunchSatellitesPacket.class;
        }

        @Override
        public ResourceLocation id() {
            return modLoc("launch_satellites");
        }

        @Override
        public void encode(ServerboundLaunchSatellitesPacket message, FriendlyByteBuf buffer) {
            buffer.writeBlockPos(message.pos());
        }

        @Override
        public ServerboundLaunchSatellitesPacket decode(FriendlyByteBuf buffer) {
            return new ServerboundLaunchSatellitesPacket(buffer.readBlockPos());
        }
    }
}
