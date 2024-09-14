package com.vertexcubed.ad_infinitum.server.network;

import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import com.teamresourceful.resourcefullib.common.network.base.ServerboundPacketType;
import com.vertexcubed.ad_infinitum.AdInfinitum;
import com.vertexcubed.ad_infinitum.common.blockentity.HoloDoorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.function.Consumer;

import static com.vertexcubed.ad_infinitum.AdInfinitum.modLoc;

public record ServerboundUpdateHoloDoorSizePacket(BlockPos pos, OpCode opcode) implements Packet<ServerboundUpdateHoloDoorSizePacket> {

    public static final ServerboundUpdateHoloDoorSizePacket.Type TYPE = new Type();

    @Override
    public PacketType<ServerboundUpdateHoloDoorSizePacket> type() {
        return TYPE;
    }

    public static class Type implements ServerboundPacketType<ServerboundUpdateHoloDoorSizePacket> {

        @Override
        public Consumer<Player> handle(ServerboundUpdateHoloDoorSizePacket message) {

            return player -> {
                BlockEntity be = player.level().getBlockEntity(message.pos());
                if(!(be instanceof HoloDoorBlockEntity holoDoor)) {
                    AdInfinitum.LOGGER.error("Cannot handle update holo door size packet, no BE!");
                    return;
                }
                holoDoor.changeSize(message.opcode());
            };
        }

        @Override
        public Class<ServerboundUpdateHoloDoorSizePacket> type() {
            return ServerboundUpdateHoloDoorSizePacket.class;
        }

        @Override
        public ResourceLocation id() {
            return modLoc("update_holo_door_size");
        }

        @Override
        public void encode(ServerboundUpdateHoloDoorSizePacket message, FriendlyByteBuf buffer) {
            buffer.writeBlockPos(message.pos());
            buffer.writeEnum(message.opcode());
        }

        @Override
        public ServerboundUpdateHoloDoorSizePacket decode(FriendlyByteBuf buffer) {
            return new ServerboundUpdateHoloDoorSizePacket(buffer.readBlockPos(), buffer.readEnum(OpCode.class));
        }
    }

    public enum OpCode {
        INCREASE_X,
        DECREASE_X,
        INCREASE_Y,
        DECREASE_Y
    }
}
