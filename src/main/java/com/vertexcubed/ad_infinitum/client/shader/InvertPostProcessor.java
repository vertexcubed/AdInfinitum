package com.vertexcubed.ad_infinitum.client.shader;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;
import team.lodestar.lodestone.systems.postprocess.PostProcessor;

import static com.vertexcubed.ad_infinitum.AdInfinitum.modLoc;

public class InvertPostProcessor extends PostProcessor {

    public static final InvertPostProcessor INSTANCE = new InvertPostProcessor();

    public InvertPostProcessor() {
        this.setActive(false);
    }

    @Override
    public ResourceLocation getPostChainLocation() {
        return modLoc("invert");
    }

    @Override
    public void beforeProcess(PoseStack viewModelStack) {

    }

    @Override
    public void afterProcess() {

    }
}
