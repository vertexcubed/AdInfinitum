package com.vertexcubed.ad_infinitum.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.vertexcubed.ad_infinitum.AdInfinitum;
import com.vertexcubed.ad_infinitum.client.shader.CoreShaderRegistry;
import com.vertexcubed.ad_infinitum.client.util.AdInfinitumWorldVFXBuilder;
import com.vertexcubed.ad_infinitum.client.util.ModStateShards;
import com.vertexcubed.ad_infinitum.client.util.ModVertexFormats;
import com.vertexcubed.ad_infinitum.common.block.HoloDoorBlock;
import com.vertexcubed.ad_infinitum.common.blockentity.HoloDoorBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3f;
import team.lodestar.lodestone.handlers.RenderHandler;
import team.lodestar.lodestone.registry.client.LodestoneRenderTypeRegistry;
import team.lodestar.lodestone.systems.rendering.LodestoneRenderType;
import team.lodestar.lodestone.systems.rendering.StateShards;
import team.lodestar.lodestone.systems.rendering.VFXBuilders;
import team.lodestar.lodestone.systems.rendering.rendeertype.RenderTypeProvider;
import team.lodestar.lodestone.systems.rendering.rendeertype.RenderTypeToken;

import static com.mojang.blaze3d.vertex.VertexFormat.Mode.QUADS;
import static team.lodestar.lodestone.registry.client.LodestoneRenderTypeRegistry.createGenericRenderType;

public class HoloDoorBlockEntityRenderer implements BlockEntityRenderer<HoloDoorBlockEntity> {

    //Texture is currently unused
    public static final ResourceLocation TEXTURE = new ResourceLocation("textures/block/crafting_table_front.png");
    public static final RenderTypeToken TOKEN = RenderTypeToken.createCachedToken(TEXTURE);

    public static final ResourceLocation RENDER_TYPE = new ResourceLocation(AdInfinitum.MODID, "holo_door");

    public static final RenderTypeProvider HOLO_DOOR_PROVIDER = new RenderTypeProvider((token) ->
            createGenericRenderType("holo_door", ModVertexFormats.POSITION_COLOR_TEX_LIGHTMAP_NORMAL, QUADS, LodestoneRenderTypeRegistry.builder()
                    .setShaderState(CoreShaderRegistry.HOLO_DOOR)
                    .setTransparencyState(StateShards.ADDITIVE_TRANSPARENCY)
                    .setLightmapState(LodestoneRenderTypeRegistry.LIGHTMAP)
                    .setCullState(LodestoneRenderTypeRegistry.NO_CULL)
                    .setWriteMaskState(ModStateShards.COLOR_WRITE)
                    .setTextureState(token.get())
            )
    );

    public static final LodestoneRenderType HOLO_DOOR = HOLO_DOOR_PROVIDER.apply(RenderTypeToken.createToken(TEXTURE));

    public static final LodestoneRenderType TEST = LodestoneRenderTypeRegistry.TRANSPARENT_TEXTURE.apply(RenderTypeToken.createToken(TEXTURE));


    public HoloDoorBlockEntityRenderer(BlockEntityRendererProvider.Context pContext) {

    }

    @Override
    public void render(HoloDoorBlockEntity pBlockEntity, float pPartialTick, PoseStack poseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
//        AdInfinitum.LOGGER.info("Rendering!!!");

        //absolute pain

        if(!pBlockEntity.isVisible()) {
            return;
        }

        poseStack.pushPose();
//        poseStack.translate(pBlockEntity.getBlockPos().getX(), pBlockEntity.getBlockPos().getY() + 3, pBlockEntity.getBlockPos().getZ());
//        poseStack.translate(0, 3, 0);

        float width = pBlockEntity.getWidth() * 0.998f;
        float height = pBlockEntity.getHeight() * 0.998f;
        float depth = pBlockEntity.getDepth() * 0.998f * 0.5f;

//        AdInfinitum.LOGGER.info("Width: " + width + " Height: " + height + " Depth: " + depth);


//        AdInfinitum.LOGGER.info("Pos 1: " + pos1 + ", Pos 2: " + pos2);
        BlockState state = pBlockEntity.getBlockState();
        Direction direction = state.getValue(HoloDoorBlock.FACING);

//        poseStack.scale(0.5f, 0.5f, 0.5f);

        poseStack.translate(0.5F, 0.0F, 0.5F);
        poseStack.mulPose(Axis.YN.rotationDegrees(direction.toYRot()));
        poseStack.translate(-0.5F, 0.0F, -0.5F);

        poseStack.translate(-(width / 2) + 0.5f, 1.001f, (depth / 2) + 0.001f);
        LodestoneRenderTypeRegistry.applyUniformChanges(HOLO_DOOR, instance -> {
            instance.safeGetUniform("nearPlaneDistance").set(GameRenderer.PROJECTION_Z_NEAR);
            instance.safeGetUniform("farPlaneDistance").set(Minecraft.getInstance().gameRenderer.getDepthFar());
            instance.safeGetUniform("Time").set(Minecraft.getInstance().level.getGameTime() + pPartialTick);
//            AdInfinitum.LOGGER.info("Applying uniforms");
        });
        renderRectangularPrism(poseStack, pBuffer, direction, width, height, depth);
        poseStack.popPose();
    }


    private void renderRectangularPrism(PoseStack poseStack, MultiBufferSource buffer, Direction direction, float width, float height, float depth) {

        //Just testing stuff at this point cuz this shit wont work


//        AdInfinitum.LOGGER.info("x1: " + x1 + ", y1: " + y1 + ", z1: " + z1 + ", x2: " + x2 + ", y2: " + y2 + ", z2: " + z2);
        VFXBuilders.WorldVFXBuilder builder = AdInfinitumWorldVFXBuilder.createWorld()
                .replaceBufferSource(RenderHandler.DELAYED_RENDER)
                .setColor(3, 194, 252)
                .setAlpha(1.0f)
                .setRenderType(HOLO_DOOR);

        Vector3f[] front = new Vector3f[]{
                new Vector3f(0, 0, depth),
                new Vector3f(width, 0, depth),
                new Vector3f(width, height, depth),
                new Vector3f(0, height, depth),
        };
        Vector3f[] back = new Vector3f[]{
                new Vector3f(width, 0, 0),
                new Vector3f(0, 0, 0),
                new Vector3f(0, height, 0),
                new Vector3f(width, height, 0),
        };
        Vector3f[] left = new Vector3f[]{
                new Vector3f(0, 0, 0),
                new Vector3f(0, 0, depth),
                new Vector3f(0, height, depth),
                new Vector3f(0, height, 0),
        };
        Vector3f[] right = new Vector3f[]{
                new Vector3f(width, 0, depth),
                new Vector3f(width, 0, 0),
                new Vector3f(width, height, 0),
                new Vector3f(width, height, depth),
        };
        Vector3f[] top = new Vector3f[]{
                new Vector3f(0, height, depth),
                new Vector3f(width, height, depth),
                new Vector3f(width, height, 0),
                new Vector3f(0, height, 0),
        };
        Vector3f[] bottom = new Vector3f[]{
                new Vector3f(0, 0, 0),
                new Vector3f(width, 0, 0),
                new Vector3f(width, 0, depth),
                new Vector3f(0, 0, depth),
        };

//        RenderHandler.copyDepthBuffer(RenderHandler.LODESTONE_DEPTH_CACHE);

//        AdInfinitum.LOGGER.info("Rendering");
//        builder
//                .renderQuad(poseStack, front, 1)
//                .renderQuad(poseStack, back, 1)
//                .renderQuad(poseStack, left, 1)
//                .renderQuad(poseStack, right, 1);
//        builder
//                .setUV(0.0f, 0.0f, 0.125f, 0.125f)
//                .renderQuad(poseStack, top, 1)
//                .renderQuad(poseStack, bottom, 1)

//        AdInfinitum.LOGGER.info("Normal: " + direction.getNormal());
        int xSubdivisions = 40;
        int ySubdivisions = 40;
        ((AdInfinitumWorldVFXBuilder) builder)
                .setNormal(new Vector3f(1, 0, 0))
                .renderQuadMesh(poseStack, front, xSubdivisions, ySubdivisions)
                .renderQuadMesh(poseStack, back, xSubdivisions, ySubdivisions)
                .setNormal(new Vector3f(0, 0, 0))
                .renderQuadMesh(poseStack, left, xSubdivisions, ySubdivisions)
                .renderQuadMesh(poseStack, right, xSubdivisions, ySubdivisions)
                .renderQuadMesh(poseStack, top, xSubdivisions, ySubdivisions)
                .renderQuadMesh(poseStack, bottom, xSubdivisions, ySubdivisions)
        ;

        ;

    }
}
