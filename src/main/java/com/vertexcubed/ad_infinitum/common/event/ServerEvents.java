package com.vertexcubed.ad_infinitum.common.event;


import com.vertexcubed.ad_infinitum.AdInfinitum;
import com.vertexcubed.ad_infinitum.common.blockentity.HoloDoorBlockEntity;
import com.vertexcubed.ad_infinitum.server.data.AdInfinitumPlanetData;
import com.vertexcubed.ad_infinitum.server.data.ChunkProtectedBlocksProvider;
import com.vertexcubed.ad_infinitum.server.network.ClientboundSyncUnlandablePlanetsPacket;
import com.vertexcubed.ad_infinitum.server.network.PacketHandler;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = AdInfinitum.MODID)
public class ServerEvents {
    @SubscribeEvent
    public static void attachChunkCapabilities(AttachCapabilitiesEvent<LevelChunk> event) {
        event.addCapability(ChunkProtectedBlocksProvider.ID, new ChunkProtectedBlocksProvider(event.getObject()));
    }

    @SubscribeEvent
    public static void onDatapackSync(OnDatapackSyncEvent event) {
        PacketHandler.CHANNEL.sendToPlayers(new ClientboundSyncUnlandablePlanetsPacket(AdInfinitumPlanetData.unlandablePlanets()), event.getPlayers());
    }

//    @SubscribeEvent
//    public static void levelTick(TickEvent.LevelTickEvent event) {
//        if(event.level.isClientSide()) return;
//        ServerLevel level = (ServerLevel) event.level;
//        if (event.phase == TickEvent.Phase.END) {
//            if(level.getGameTime() % 40 == 0) {
//                HoloDoorBlockEntity.invalidateData(level);
//            }
//        }
//    }
}
