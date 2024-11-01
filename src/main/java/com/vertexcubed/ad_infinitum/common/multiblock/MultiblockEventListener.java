package com.vertexcubed.ad_infinitum.common.multiblock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.NotImplementedException;

/**
 * A simple interface to listen to certain Multiblock events, for example forming and unforming.
 */
public interface MultiblockEventListener {

    /**
     * This event is fired on both sides when the multiblock is formed, from both a player forming it and when the multiblock is loaded.
     * @param level the level.
     * @param multiblock the multiblock this listener is a part of.
     * @param thisState the current Blockstate.
     * @param origin the origin of the multiblock.
     */
    default void onMultiblockForm(Level level, Multiblock multiblock, BlockState thisState, BlockPos origin) {}

    /**
     * This method is deprecated, and currently not implemented.
     * @param level the level.
     * @param multiblock the multiblock this listener is a part of.
     * @param thisState the current Blockstate.
     * @param origin the origin of the multiblock.
     */
    @Deprecated
    default void onMultiblockUnform(Level level, Multiblock multiblock, BlockState thisState, BlockPos origin) {
        throw new NotImplementedException("This method is currently not implemented! Do not call.");
    }
}
