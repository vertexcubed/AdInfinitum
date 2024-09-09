package com.vertexcubed.ad_infinitum.common.event;

import com.vertexcubed.ad_infinitum.AdInfinitum;
import com.vertexcubed.ad_infinitum.server.data.ChunkProtectedBlocks;
import com.vertexcubed.ad_infinitum.server.data.ChunkProtectedBlocksProvider;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = AdInfinitum.MODID)
public class ModBusEvents {



    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(ChunkProtectedBlocks.class);
    }
}
