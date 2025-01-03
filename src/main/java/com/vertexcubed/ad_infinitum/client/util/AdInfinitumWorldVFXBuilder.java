package com.vertexcubed.ad_infinitum.client.util;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.vertexcubed.ad_infinitum.common.util.Maath;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import team.lodestar.lodestone.helpers.RenderHelper;
import team.lodestar.lodestone.systems.rendering.VFXBuilders;

public class AdInfinitumWorldVFXBuilder extends VFXBuilders.WorldVFXBuilder {

    private Vector3f normal;

    private AdInfinitumWorldVFXBuilder() {}

    static {
        VFXBuilders.WorldVFXBuilder.CONSUMER_INFO_MAP.put(DefaultVertexFormat.ELEMENT_NORMAL, (consumer, last, builder, x, y, z, u, v) -> {
            consumer.normal(AdInfinitumWorldVFXBuilder.getNormal(builder).x(), AdInfinitumWorldVFXBuilder.getNormal(builder).y(), AdInfinitumWorldVFXBuilder.getNormal(builder).z());
        });
        VFXBuilders.WorldVFXBuilder.CONSUMER_INFO_MAP.put(DefaultVertexFormat.ELEMENT_PADDING, (consumer, last, builder, x, y, z, u, v) -> {
            return;
        });
    }

    public AdInfinitumWorldVFXBuilder setNormal(Vector3f normal) {
        this.normal = normal;
        return this;
    }
    public AdInfinitumWorldVFXBuilder setNormal(Vec3i normal) {
        return setNormal(new Vector3f((float) normal.getX(), (float) normal.getY(), (float) normal.getZ()));
    }

    public static Vector3f getNormal(VFXBuilders.WorldVFXBuilder builder) {
        if(builder instanceof AdInfinitumWorldVFXBuilder adInfBuilder) {
            return adInfBuilder.normal;
        }
        else {
            return new Vector3f(0, 0, 0);
        }
    }

    public static AdInfinitumWorldVFXBuilder createWorld() {
        return new AdInfinitumWorldVFXBuilder();
    }


    /**
     * Renders a quad divided into multiple smaller quads. Adapted from Catlike Coding's <a href=https://catlikecoding.com/unity/tutorials/procedural-grid/>Procedural Grid</a> tutorial.
     * @param positions     the positions of the corners of the quad mesh. Should be in the order bottom left -> bottom right -> top right -> top left
     */
    public AdInfinitumWorldVFXBuilder renderQuadMesh(PoseStack poseStack, Vector3f[] positions, int xSize, int ySize) {
        Matrix4f last = poseStack.last().pose();
        Vector3f[] vertices = new Vector3f[(xSize + 1) * (ySize + 1)];
        Vector2f[] uvs = new Vector2f[vertices.length];


        int i = 0;
        for(int y = 0; y <= ySize; y++) {
            for(int x = 0; x <= xSize; x++) {
                float lerpWidth = (float) x / xSize;
                float lerpHeight = (float) y / ySize;
                float lerpX = Maath.blerp(lerpWidth, lerpHeight, positions[0].x(), positions[3].x(), positions[2].x(), positions[1].x());
                float lerpY = Maath.blerp(lerpWidth, lerpHeight, positions[0].y(), positions[3].y(), positions[2].y(), positions[1].y());
                float lerpZ = Maath.blerp(lerpWidth, lerpHeight, positions[0].z(), positions[3].z(), positions[2].z(), positions[1].z());

                vertices[i] = new Vector3f(lerpX, lerpY, lerpZ);
                uvs[i] = new Vector2f(lerpWidth, 1.0f - lerpHeight);
                i++;
            }
        }
        int[] quads = new int[xSize * ySize * 4];
        int quadIndex = 0;
        int vertexIndex = 0;
        for(int y = 0; y < ySize; y++) {
            for(int x = 0; x < xSize; x++) {
                quads[quadIndex] = vertexIndex;
                quads[quadIndex + 1] = vertexIndex + 1;
                quads[quadIndex + 2] = vertexIndex + xSize + 2;
                quads[quadIndex + 3] = vertexIndex + xSize + 1;
                vertexIndex++;
                quadIndex += 4;
            }

            vertexIndex++;
        }
//        AdInfinitum.LOGGER.info("FRAME START");
//        AdInfinitum.LOGGER.info("===================");
//        AdInfinitum.LOGGER.info("VERTICES: " + Arrays.toString(vertices));
//        AdInfinitum.LOGGER.info("QUAD INDICES: " + Arrays.toString(quads));

        for(int t = 0; t < quads.length; t++ ) {
            int vi = quads[t];
            supplier.placeVertex(getVertexConsumer(), last, this, vertices[vi].x(), vertices[vi].y(), vertices[vi].z(), uvs[vi].x(), uvs[vi].y());
        }

        return this;
    }

    public AdInfinitumWorldVFXBuilder renderTriangle(PoseStack poseStack, Vector3f[] positions) {
        Matrix4f last = poseStack.last().pose();
        supplier.placeVertex(getVertexConsumer(), last, this, positions[0].x, positions[0].y, positions[0].z, 0, 0);
        supplier.placeVertex(getVertexConsumer(), last, this, positions[1].x, positions[1].y, positions[1].z, 0, 0);
        supplier.placeVertex(getVertexConsumer(), last, this, positions[2].x, positions[2].y, positions[2].z, 0, 0);

        return this;
    }

    public VFXBuilders.WorldVFXBuilder renderSphereNoTiling(PoseStack stack, float radius, int longs, int lats) {
        Matrix4f last = stack.last().pose();
        float startU = u0;
        float startV = v0;
        float endU = Mth.PI * 2 * u1;
        float endV = Mth.PI * v1;
        float stepU = (endU - startU) / longs;
        float stepV = (endV - startV) / lats;
        for (int i = 0; i < longs; ++i) {
            // U-points
            for (int j = 0; j < lats; ++j) {
                // V-points
                float u = i * stepU + startU;
                float v = j * stepV + startV;
                float un = (i + 1 == longs) ? endU : (i + 1) * stepU + startU;
                float vn = (j + 1 == lats) ? endV : (j + 1) * stepV + startV;
                Vector3f p0 = team.lodestar.lodestone.helpers.RenderHelper.parametricSphere(u, v, radius);
                Vector3f p1 = team.lodestar.lodestone.helpers.RenderHelper.parametricSphere(u, vn, radius);
                Vector3f p2 = team.lodestar.lodestone.helpers.RenderHelper.parametricSphere(un, v, radius);
                Vector3f p3 = RenderHelper.parametricSphere(un, vn, radius);

                float textureU = u / endU;
                float textureV = v / endV;
                float textureUN = un / endU;
                float textureVN = vn / endV;
                supplier.placeVertex(getVertexConsumer(), last, this, p0.x(), p0.y(), p0.z(), textureU, textureV);
                supplier.placeVertex(getVertexConsumer(), last, this, p2.x(), p2.y(), p2.z(), textureUN, textureV);
                supplier.placeVertex(getVertexConsumer(), last, this, p1.x(), p1.y(), p1.z(), textureU, textureVN);

                supplier.placeVertex(getVertexConsumer(), last, this, p3.x(), p3.y(), p3.z(), textureUN, textureVN);
                supplier.placeVertex(getVertexConsumer(), last, this, p1.x(), p1.y(), p1.z(), textureU, textureVN);
                supplier.placeVertex(getVertexConsumer(), last, this, p2.x(), p2.y(), p2.z(), textureUN, textureV);
            }
        }
        return this;
    }
}

