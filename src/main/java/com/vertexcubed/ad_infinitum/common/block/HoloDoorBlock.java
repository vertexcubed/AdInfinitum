package com.vertexcubed.ad_infinitum.common.block;

import com.vertexcubed.ad_infinitum.common.registry.BlockRegistry;
import earth.terrarium.adastra.common.blocks.base.MachineBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class HoloDoorBlock extends MachineBlock {


    public HoloDoorBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntityType<?> entity(BlockState state) {
        return BlockRegistry.HOLO_DOOR_BLOCK_ENTITY.get();
    }

}
