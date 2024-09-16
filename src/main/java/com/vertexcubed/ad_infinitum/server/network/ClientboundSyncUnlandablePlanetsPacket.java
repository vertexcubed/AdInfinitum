package com.vertexcubed.ad_infinitum.server.network;

import com.mojang.serialization.Codec;
import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.ClientboundPacketType;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import com.vertexcubed.ad_infinitum.AdInfinitum;
import com.vertexcubed.ad_infinitum.server.data.AdInfinitumPlanetData;
import earth.terrarium.adastra.api.planets.Planet;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

import static com.vertexcubed.ad_infinitum.AdInfinitum.modLoc;

public record ClientboundSyncUnlandablePlanetsPacket(
        Map<ResourceLocation, Planet> planets
) implements Packet<ClientboundSyncUnlandablePlanetsPacket> {

    public static final ClientboundPacketType<ClientboundSyncUnlandablePlanetsPacket> TYPE = new Type();

    @Override
    public PacketType<ClientboundSyncUnlandablePlanetsPacket> type() {
        return TYPE;
    }

    private static class Type implements ClientboundPacketType<ClientboundSyncUnlandablePlanetsPacket> {

        @Override
        public Runnable handle(ClientboundSyncUnlandablePlanetsPacket message) {
            return () -> {
                AdInfinitumPlanetData.setUnlandablePlanets(message.planets());
                AdInfinitum.LOGGER.info("Synced unlandable planets!" + message.planets().size());
            };
        }


        @Override
        public Class<ClientboundSyncUnlandablePlanetsPacket> type() {
            return ClientboundSyncUnlandablePlanetsPacket.class;
        }

        @Override
        public ResourceLocation id() {
            return modLoc("sync_unlandable_planets");
        }

        @Override
        public void encode(ClientboundSyncUnlandablePlanetsPacket message, FriendlyByteBuf buffer) {
            buffer.writeJsonWithCodec(Codec.unboundedMap(ResourceLocation.CODEC, Planet.CODEC), message.planets());
        }

        @Override
        public ClientboundSyncUnlandablePlanetsPacket decode(FriendlyByteBuf buffer) {
            return new ClientboundSyncUnlandablePlanetsPacket(buffer.readJsonWithCodec(Codec.unboundedMap(ResourceLocation.CODEC, Planet.CODEC)));
        }
    }
}
