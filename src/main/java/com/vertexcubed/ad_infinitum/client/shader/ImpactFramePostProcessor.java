package com.vertexcubed.ad_infinitum.client.shader;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.vertexcubed.ad_infinitum.AdInfinitum;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import team.lodestar.lodestone.helpers.RenderHelper;
import team.lodestar.lodestone.helpers.VecHelper;
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
            Vector3f centerTexCoord = worldPosToTexCoord(position, viewModelStack);
//            AdInfinitum.LOGGER.info("centerTexCoord: " + centerTexCoord);
            effect.safeGetUniform("Center").set(centerTexCoord.x, centerTexCoord.y);
            effect.safeGetUniform("WorldCenter").set(position);
        });
    }

    @Override
    public void afterProcess() {

    }

    private static Vector3f worldPosToTexCoord(Vector3f worldPos, PoseStack viewModelStack) {
        Matrix4f viewMat = viewModelStack.last().pose();
        Matrix4f projMat = RenderSystem.getProjectionMatrix();

        Vector3f localPos = new Vector3f(worldPos);
        localPos.sub(Minecraft.getInstance().gameRenderer.getMainCamera().getPosition().toVector3f());

        Vector4f pos = new Vector4f(localPos, 0);
        pos.mul(viewMat);
        pos.mul(projMat);
        perspectiveDivide(pos);

        return new Vector3f(pos.x() / 2.0f + 0.5f, pos.y() / 2.0f + 0.5f, pos.z() / 2.0f + 0.5f);
    }

    private static void perspectiveDivide(Vector4f v) {
        v.div(v.w);
    }


}
