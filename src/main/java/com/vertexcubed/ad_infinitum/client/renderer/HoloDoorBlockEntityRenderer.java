package com.vertexcubed.ad_infinitum.client.renderer;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.vertexcubed.ad_infinitum.AdInfinitum;
import com.vertexcubed.ad_infinitum.client.shader.CoreShaderRegistry;
import com.vertexcubed.ad_infinitum.client.util.AdInfinitumWorldVFXBuilder;
import com.vertexcubed.ad_infinitum.common.blockentity.HoloDoorBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector3f;
import team.lodestar.lodestone.handlers.RenderHandler;
import team.lodestar.lodestone.registry.client.LodestoneRenderTypeRegistry;
import team.lodestar.lodestone.systems.rendering.LodestoneRenderType;
import team.lodestar.lodestone.systems.rendering.StateShards;
import team.lodestar.lodestone.systems.rendering.VFXBuilders;
import team.lodestar.lodestone.systems.rendering.rendeertype.RenderTypeProvider;
import team.lodestar.lodestone.systems.rendering.rendeertype.RenderTypeToken;

import static com.vertexcubed.ad_infinitum.AdInfinitum.modLoc;

public class HoloDoorBlockEntityRenderer implements BlockEntityRenderer<HoloDoorBlockEntity> {

    public static final ResourceLocation TEXTURE = new ResourceLocation("textures/block/crafting_table_front.png");

    public static final ResourceLocation RENDER_TYPE = new ResourceLocation(AdInfinitum.MODID, "holo_door");
    public static final RenderTypeProvider HOLO_DOOR_PROVIDER = new RenderTypeProvider(texture ->
        LodestoneRenderTypeRegistry.createGenericRenderType(RENDER_TYPE.toString(),
                DefaultVertexFormat.POSITION_TEX_COLOR, VertexFormat.Mode.QUADS, LodestoneRenderTypeRegistry.builder()
                        .setShaderState(CoreShaderRegistry.HOLO_DOOR)
                        .setTransparencyState(StateShards.ADDITIVE_TRANSPARENCY)
                        .setTextureState(texture.get())
                        .setCullState(LodestoneRenderTypeRegistry.NO_CULL)
    ));

    public static final LodestoneRenderType HOLO_DOOR = HOLO_DOOR_PROVIDER.apply(RenderTypeToken.createToken(TEXTURE));

    public HoloDoorBlockEntityRenderer(BlockEntityRendererProvider.Context pContext) {

    }

    @Override
    public void render(HoloDoorBlockEntity pBlockEntity, float pPartialTick, PoseStack poseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
//        AdInfinitum.LOGGER.info("Rendering!!!");

        //absolute pain

        poseStack.pushPose();
        poseStack.translate(pBlockEntity.getBlockPos().getX(), pBlockEntity.getBlockPos().getY() + 3, pBlockEntity.getBlockPos().getZ());




        renderRectangularPrism(poseStack, pBuffer, pBlockEntity.getFirstPos(), pBlockEntity.getSecondPos());
        poseStack.popPose();
    }

    private void renderRectangularPrism(PoseStack poseStack, MultiBufferSource buffer, BlockPos pos1, BlockPos pos2) {

        renderRectangularPrism(poseStack, buffer,
                Math.min(pos1.getX(), pos2.getX()), Math.min(pos1.getY(), pos2.getY()), Math.min(pos1.getZ(), pos2.getZ()),
                Math.max(pos1.getX(), pos2.getX()), Math.max(pos1.getY(), pos2.getY()), Math.max(pos1.getZ(), pos2.getZ()));
    }

    private void renderRectangularPrism(PoseStack poseStack, MultiBufferSource buffer, int x1, int y1, int z1, int x2, int y2, int z2) {

        //Just testing stuff at this point cuz this shit wont work


//        AdInfinitum.LOGGER.info("x1: " + x1 + ", y1: " + y1 + ", z1: " + z1 + ", x2: " + x2 + ", y2: " + y2 + ", z2: " + z2);
        VFXBuilders.WorldVFXBuilder builder = AdInfinitumWorldVFXBuilder.createWorld()
                .replaceBufferSource(RenderHandler.DELAYED_RENDER)
                .setColor(255.0f, 255.0f, 255.0f)
                .setAlpha(1.0f);
        Vector3f[] positions = new Vector3f[]{
                new Vector3f(x1, y1, z1),
                new Vector3f(x2, y1, z2),
                new Vector3f(x2, y2, z2),
                new Vector3f(x1, y2, z1)
        };
//        AdInfinitum.LOGGER.info("Rendering");
        builder
                .setRenderType(LodestoneRenderTypeRegistry.TRANSPARENT_TEXTURE.applyAndCache(RenderTypeToken.createCachedToken(TEXTURE)))
                .renderQuad(poseStack, 2);

    }
}
