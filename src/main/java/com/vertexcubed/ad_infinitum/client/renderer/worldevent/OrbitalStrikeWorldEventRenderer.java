package com.vertexcubed.ad_infinitum.client.renderer.worldevent;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import com.vertexcubed.ad_infinitum.AdInfinitum;
import com.vertexcubed.ad_infinitum.client.util.AdInfinitumWorldVFXBuilder;
import com.vertexcubed.ad_infinitum.common.worldevent.OrbitalStrikeWorldEvent;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector3f;
import team.lodestar.lodestone.registry.client.LodestoneRenderTypeRegistry;
import team.lodestar.lodestone.registry.client.LodestoneShaderRegistry;
import team.lodestar.lodestone.systems.rendering.StateShards;
import team.lodestar.lodestone.systems.rendering.VFXBuilders;
import team.lodestar.lodestone.systems.rendering.rendeertype.RenderTypeProvider;
import team.lodestar.lodestone.systems.rendering.rendeertype.RenderTypeToken;
import team.lodestar.lodestone.systems.worldevent.WorldEventRenderer;

import static team.lodestar.lodestone.registry.client.LodestoneRenderTypeRegistry.CULL;
import static team.lodestar.lodestone.registry.client.LodestoneRenderTypeRegistry.LIGHTMAP;

public class OrbitalStrikeWorldEventRenderer extends WorldEventRenderer<OrbitalStrikeWorldEvent> {

    public static final ResourceLocation TEXTURE = new ResourceLocation("textures/block/crafting_table_front.png");
    public static final RenderTypeProvider SPHERE_PROVIDER = new RenderTypeProvider(token ->
        LodestoneRenderTypeRegistry.createGenericRenderType("ad_infinitum:sphere", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.TRIANGLES,
                LodestoneRenderTypeRegistry.builder(token, StateShards.NORMAL_TRANSPARENCY, LodestoneShaderRegistry.LODESTONE_TEXTURE, CULL, LIGHTMAP)
        )
    );
    public static final RenderType SPHERE = SPHERE_PROVIDER.apply(RenderTypeToken.createToken(TEXTURE));





    @Override
    public boolean canRender(OrbitalStrikeWorldEvent instance) {
        return true;
    }

    @Override
    public void render(OrbitalStrikeWorldEvent instance, PoseStack poseStack, MultiBufferSource bufferSource, float partialTicks) {
        if(instance.shouldRenderExplosion()) {

            Vector3f position = instance.getPosition();

            poseStack.pushPose();
            poseStack.translate(position.x, position.y, position.z);

            float time = (instance.getAge() - OrbitalStrikeWorldEvent.EXPLOSION_START + partialTicks);
            float radius = time;

            VFXBuilders.WorldVFXBuilder builder = AdInfinitumWorldVFXBuilder.createWorld()
                    .setRenderType(SPHERE)
                    .setLight(LightTexture.FULL_BRIGHT)
                    .setAlpha(1.0f);

            ((AdInfinitumWorldVFXBuilder) builder).renderSphereNoTiling(poseStack, radius, 30, 30);

            poseStack.popPose();
        }
    }
}
