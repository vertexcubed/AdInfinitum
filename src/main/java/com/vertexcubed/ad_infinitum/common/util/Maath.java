package com.vertexcubed.ad_infinitum.common.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Rotation;

/**
 * Shitty math functions. Maath!
 */
public class Maath {


    /**
     * Bilinearly interpolates between four points as shown below:
     * <pre>
     *
     *  B   C
     *
     *  A   D
     * </pre>
     * where u is the horizontal direction and v is the vertical direction.
     */
    public static float blerp(float u, float v, float a, float b, float c, float d) {
        return ( (1 - u) * ( ((1 - v) * a) + (v * b) ) ) + ( u * ( ((1 - v) * d) + (v * c) ) );
    }

    public static BlockPos rotateAround(BlockPos pos, BlockPos center, Rotation rotation) {
        return pos.subtract(center).rotate(rotation).offset(center);
    }
}
