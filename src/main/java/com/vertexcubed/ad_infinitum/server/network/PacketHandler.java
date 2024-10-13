package com.vertexcubed.ad_infinitum.server.network;


import com.teamresourceful.resourcefullib.common.network.NetworkChannel;
import com.vertexcubed.ad_infinitum.AdInfinitum;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import static com.vertexcubed.ad_infinitum.AdInfinitum.modLoc;


public class PacketHandler {
    public static final NetworkChannel CHANNEL = new NetworkChannel(AdInfinitum.MODID, 1, "main");

    public static SimpleChannel CHANNEL_VANILLA;


    public static void init() {
        CHANNEL.register(ClientboundSyncUnlandablePlanetsPacket.TYPE);
        CHANNEL.register(ServerboundUpdateHoloDoorSizePacket.TYPE);
        CHANNEL.register(ClientboundSyncSatelliteItemStoragePacket.TYPE);
        CHANNEL.register(ServerboundLaunchSatellitesPacket.TYPE);
    }
}
