package com.vertexcubed.ad_infinitum.common.block;

import com.vertexcubed.ad_infinitum.common.registry.BlockRegistry;
import earth.terrarium.adastra.common.blocks.base.MachineBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class SatelliteLauncherBlock extends MachineBlock {
    public SatelliteLauncherBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntityType<?> entity(BlockState state) {
        return BlockRegistry.SATELLITE_LAUNCHER_BLOCK_ENTITY.get();
    }
}
