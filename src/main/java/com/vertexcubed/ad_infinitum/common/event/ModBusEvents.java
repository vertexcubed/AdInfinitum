package com.vertexcubed.ad_infinitum.common.event;

import com.vertexcubed.ad_infinitum.AdInfinitum;
import com.vertexcubed.ad_infinitum.client.renderer.HoloDoorBlockEntityRenderer;
import com.vertexcubed.ad_infinitum.client.screen.HoloDoorScreen;
import com.vertexcubed.ad_infinitum.client.shader.CoreShaderRegistry;
import com.vertexcubed.ad_infinitum.common.registry.BlockRegistry;
import com.vertexcubed.ad_infinitum.common.registry.MenuRegistry;
import com.vertexcubed.ad_infinitum.server.data.ChunkProtectedBlocks;
import com.vertexcubed.ad_infinitum.server.data.ChunkProtectedBlocksProvider;
import com.vertexcubed.ad_infinitum.server.network.PacketHandler;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.io.IOException;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = AdInfinitum.MODID)
public class ModBusEvents {

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(ChunkProtectedBlocks.class);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(BlockRegistry.HOLO_DOOR_BLOCK_ENTITY.get(), HoloDoorBlockEntityRenderer::new);
    }

    @SubscribeEvent
    public static void registerShaders(RegisterShadersEvent event) throws IOException {
        CoreShaderRegistry.registerShaders(event);
    }

    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        PacketHandler.init();
    }

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        MenuScreens.register(MenuRegistry.HOLO_DOOR.get(), HoloDoorScreen::new);
    }
}
