package com.vertexcubed.ad_infinitum.common.event;


import com.vertexcubed.ad_infinitum.AdInfinitum;
import com.vertexcubed.ad_infinitum.client.renderer.SatelliteRenderDispatcher;
import com.vertexcubed.ad_infinitum.client.screenshake.ScreenshakeHandler;
import com.vertexcubed.ad_infinitum.client.shader.HeatDistortionPostProcessor;
import com.vertexcubed.ad_infinitum.client.shader.ImpactFramePostProcessor;
import com.vertexcubed.ad_infinitum.client.shader.InvertPostProcessor;
import com.vertexcubed.ad_infinitum.common.registry.KeyMappings;
import com.vertexcubed.ad_infinitum.common.registry.TagRegistry;
import com.vertexcubed.ad_infinitum.common.satellite.TestSatellite;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = AdInfinitum.MODID)
public class ClientEvents {


    @SubscribeEvent
    public static void onClientTickStart(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        ScreenshakeHandler.onClientTick(event);


    }

    @SubscribeEvent
    public static void computeCameraAngles(ViewportEvent.ComputeCameraAngles event) {
        ScreenshakeHandler.setupCamera(event);
    }

    @SubscribeEvent
    public static void renderSatellite(RenderLevelStageEvent event) {
        if(event.getStage() != RenderLevelStageEvent.Stage.AFTER_ENTITIES) return;
//        if(true) return;
        TestSatellite satellite = new TestSatellite(UUID.randomUUID(), Minecraft.getInstance().player.getUUID());
        Vec3 cameraPos = event.getCamera().getPosition();
        event.getPoseStack().pushPose();
//        event.getPoseStack().mulPose();
        SatelliteRenderDispatcher.INSTANCE.render(satellite, 0 - cameraPos.x, 0 - cameraPos.y, 0 - cameraPos.z, event.getPartialTick(), event.getPoseStack(), Minecraft.getInstance().renderBuffers().bufferSource(), LightTexture.FULL_BRIGHT);
        event.getPoseStack().popPose();
    }

    @SubscribeEvent
    public static void onClientJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if(!event.getEntity().level().isClientSide) return;
        ImpactFramePostProcessor.INSTANCE.setActive(false);
        InvertPostProcessor.INSTANCE.setActive(false);
    }


    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if(event.phase == TickEvent.Phase.START) return;
        if(Minecraft.getInstance().level == null) return;

        if(Minecraft.getInstance().level.dimensionTypeRegistration().is(TagRegistry.SUPER_HOT_DIMENSIONS)) {
            //This does trigger correctly.
            if(!HeatDistortionPostProcessor.INSTANCE.isActive()) {
                HeatDistortionPostProcessor.INSTANCE.setActive(true);
            }
        }
        else {
            if(HeatDistortionPostProcessor.INSTANCE.isActive()) {
                HeatDistortionPostProcessor.INSTANCE.setActive(false);
            }
        }


        while(KeyMappings.SHADER_TEST.get().consumeClick() && Minecraft.getInstance().player != null) {
            ImpactFramePostProcessor.INSTANCE.setPosition(Minecraft.getInstance().player.position().toVector3f());
            ImpactFramePostProcessor.INSTANCE.setActive(!ImpactFramePostProcessor.INSTANCE.isActive());
        }

    }

}
