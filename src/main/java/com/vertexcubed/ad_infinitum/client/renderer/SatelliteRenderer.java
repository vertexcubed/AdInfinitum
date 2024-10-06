package com.vertexcubed.ad_infinitum.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.vertexcubed.ad_infinitum.common.satellite.Satellite;
import net.minecraft.client.renderer.MultiBufferSource;

public abstract class SatelliteRenderer<T extends Satellite> {


    public abstract void render(T satellite, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight);



    @FunctionalInterface
    public interface Provider<T extends Satellite> {
        SatelliteRenderer<T> create();
    }
}
