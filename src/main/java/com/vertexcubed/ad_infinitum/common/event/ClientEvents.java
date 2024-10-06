package com.vertexcubed.ad_infinitum.common.event;


import com.mojang.math.Axis;
import com.vertexcubed.ad_infinitum.AdInfinitum;
import com.vertexcubed.ad_infinitum.client.renderer.SatelliteRenderDispatcher;
import com.vertexcubed.ad_infinitum.common.satellite.TestSatellite;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = AdInfinitum.MODID)
public class ClientEvents {


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


}
