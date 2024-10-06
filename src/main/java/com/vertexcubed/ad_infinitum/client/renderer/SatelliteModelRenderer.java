package com.vertexcubed.ad_infinitum.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.vertexcubed.ad_infinitum.AdInfinitum;
import com.vertexcubed.ad_infinitum.common.satellite.Satellite;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;

public class SatelliteModelRenderer<T extends Satellite, M extends Model> extends SatelliteRenderer<T> {

    private final M model;
    private final ResourceLocation texture;
    public SatelliteModelRenderer(ResourceLocation texture, M model) {
        this.model = model;
        this.texture = texture;
    }


    @Override
    public void render(T satellite, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
//        AdInfinitum.LOGGER.info("Rendering satellite");
        pPoseStack.pushPose();
        pPoseStack.mulPose(Axis.XP.rotationDegrees(180));
        model.renderToBuffer(pPoseStack, pBuffer.getBuffer(model.renderType(texture)), pPackedLight, 0, 1.0f, 1.0f, 1.0f, 1.0f);
        pPoseStack.popPose();
    }
}
