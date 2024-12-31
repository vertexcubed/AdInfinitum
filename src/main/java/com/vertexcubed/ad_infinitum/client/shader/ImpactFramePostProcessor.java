package com.vertexcubed.ad_infinitum.client.shader;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector3f;
import team.lodestar.lodestone.systems.postprocess.PostProcessHandler;
import team.lodestar.lodestone.systems.postprocess.PostProcessor;

import java.util.Arrays;

import static com.vertexcubed.ad_infinitum.AdInfinitum.modLoc;

public class ImpactFramePostProcessor extends PostProcessor {

    public static final ImpactFramePostProcessor INSTANCE = new ImpactFramePostProcessor();

    public static void register() {
        PostProcessHandler.addInstance(ImpactFramePostProcessor.INSTANCE);
    }

    private Vector3f position;
    public ImpactFramePostProcessor() {
        position = new Vector3f(0, 0, 0);
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public void setPosition(float x, float y, float z) {
        this.position = new Vector3f(x, y, z);
    }

    @Override
    public ResourceLocation getPostChainLocation() {
        return modLoc("test");
    }

    @Override
    public void beforeProcess(PoseStack viewModelStack) {
        Arrays.stream(effects).forEach(effect -> {
            effect.safeGetUniform("Center").set(position);
            effect.safeGetUniform("ViewMat").set(PostProcessor.viewModelStack.last().pose());
        });
    }

    @Override
    public void afterProcess() {

    }


}
