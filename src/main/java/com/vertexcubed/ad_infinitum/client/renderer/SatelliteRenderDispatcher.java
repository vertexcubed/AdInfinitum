package com.vertexcubed.ad_infinitum.client.renderer;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.vertexcubed.ad_infinitum.common.satellite.Satellite;
import com.vertexcubed.ad_infinitum.common.satellite.SatelliteType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

import java.util.Map;

/**
 * Based on EntityRenderDispatcher
 */
public class SatelliteRenderDispatcher implements ResourceManagerReloadListener {

    public static final SatelliteRenderDispatcher INSTANCE = new SatelliteRenderDispatcher();

    private Map<SatelliteType<?>, SatelliteRenderer<?>> renderers = ImmutableMap.of();
    public SatelliteRenderDispatcher() {

    }


    public <T extends Satellite> SatelliteRenderer<? super T> getRenderer(T satellite) {
        return (SatelliteRenderer<? super T>) this.renderers.get(satellite.getType());
    }


    public <T extends Satellite> void render(T satellite, double x, double y, double z, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        try {
            SatelliteRenderer<? super T> renderer = this.getRenderer(satellite);
            poseStack.pushPose();
            poseStack.translate(x, y, z);
            renderer.render(satellite, partialTick, poseStack, buffer, packedLight);
            poseStack.popPose();
        }
        catch(Exception e) {
            throw new IllegalStateException("Failed to render satellite: " + satellite.toString(), e);
        }
    }



    @Override
    public void onResourceManagerReload(ResourceManager pResourceManager) {
        this.renderers = SatelliteRenderers.createRenderers();
    }
}
