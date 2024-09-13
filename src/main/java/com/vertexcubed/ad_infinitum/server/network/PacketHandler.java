package com.vertexcubed.ad_infinitum.server.network;


import com.teamresourceful.resourcefullib.common.network.NetworkChannel;
import com.teamresourceful.resourcefullib.common.networking.base.NetworkDirection;
import com.vertexcubed.ad_infinitum.AdInfinitum;


public class PacketHandler {
    public static final NetworkChannel CHANNEL = new NetworkChannel(AdInfinitum.MODID, 1, "main");

    public static void init() {
        CHANNEL.register(ClientboundSyncUnlandablePlanetsPacket.TYPE);
    }
}
