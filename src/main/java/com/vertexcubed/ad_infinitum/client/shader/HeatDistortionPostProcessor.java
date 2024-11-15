package com.vertexcubed.ad_infinitum.client.shader;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;
import team.lodestar.lodestone.systems.postprocess.PostProcessor;

import static com.vertexcubed.ad_infinitum.AdInfinitum.modLoc;

public class HeatDistortionPostProcessor extends PostProcessor {

    public static final HeatDistortionPostProcessor INSTANCE = new HeatDistortionPostProcessor();


    @Override
    public ResourceLocation getPostChainLocation() {
        return modLoc("heat_distortion_post");
    }

    @Override
    public void beforeProcess(PoseStack viewModelStack) {

    }

    @Override
    public void afterProcess() {

    }
}
