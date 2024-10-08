package com.vertexcubed.ad_infinitum.common.event;

import com.vertexcubed.ad_infinitum.AdInfinitum;
import com.vertexcubed.ad_infinitum.client.model.EnergySatelliteModel;
import com.vertexcubed.ad_infinitum.client.renderer.HoloDoorBlockEntityRenderer;
import com.vertexcubed.ad_infinitum.client.renderer.SatelliteModelRenderer;
import com.vertexcubed.ad_infinitum.client.renderer.SatelliteRenderDispatcher;
import com.vertexcubed.ad_infinitum.client.renderer.SatelliteRenderers;
import com.vertexcubed.ad_infinitum.client.screen.HoloDoorScreen;
import com.vertexcubed.ad_infinitum.client.screen.SatelliteLauncherScreen;
import com.vertexcubed.ad_infinitum.client.shader.CoreShaderRegistry;
import com.vertexcubed.ad_infinitum.common.multiblock.Multiblock;
import com.vertexcubed.ad_infinitum.common.registry.BlockRegistry;
import com.vertexcubed.ad_infinitum.common.registry.MenuRegistry;
import com.vertexcubed.ad_infinitum.common.registry.SatelliteRegistry;
import com.vertexcubed.ad_infinitum.server.capability.SatelliteItemStorage;
import com.vertexcubed.ad_infinitum.server.data.ChunkProtectedBlocks;
import com.vertexcubed.ad_infinitum.server.network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DataPackRegistryEvent;

import java.io.IOException;

import static com.vertexcubed.ad_infinitum.AdInfinitum.modLoc;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = AdInfinitum.MODID)
public class ModBusEvents {

    //================
    //     SERVER
    //================

    @SubscribeEvent
    public static void datapackRegistries(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(Multiblock.REGISTRY, Multiblock.CODEC, Multiblock.CODEC);
    }



    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        PacketHandler.init();
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(ChunkProtectedBlocks.class);
        event.register(SatelliteItemStorage.class);
    }









    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(MenuRegistry.SATELLITE_LAUNCHER.get(), SatelliteLauncherScreen::new);
            MenuScreens.register(MenuRegistry.HOLO_DOOR.get(), HoloDoorScreen::new);

            SatelliteRenderers.register(SatelliteRegistry.TEST.get(), () -> new SatelliteModelRenderer<>(
                    modLoc("textures/satellite/energy_satellite.png"),
                    new EnergySatelliteModel(Minecraft.getInstance().getEntityModels().bakeLayer(EnergySatelliteModel.LAYER_LOCATION)))
            );
        });
    }

    @SubscribeEvent
    public static void registerClientReloadListeners(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(SatelliteRenderDispatcher.INSTANCE);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(BlockRegistry.HOLO_DOOR_BLOCK_ENTITY.get(), HoloDoorBlockEntityRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        AdInfinitum.LOGGER.info("Registering layer definitions!");
        event.registerLayerDefinition(EnergySatelliteModel.LAYER_LOCATION, EnergySatelliteModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerShaders(RegisterShadersEvent event) throws IOException {
        CoreShaderRegistry.registerShaders(event);
    }
}
