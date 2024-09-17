package com.vertexcubed.ad_infinitum.common.block;

import com.vertexcubed.ad_infinitum.AdInfinitum;
import com.vertexcubed.ad_infinitum.common.blockentity.SatelliteLauncherBlockEntity;
import com.vertexcubed.ad_infinitum.common.registry.BlockRegistry;
import earth.terrarium.adastra.common.blocks.base.MachineBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class SatelliteLauncherBlock extends MachineBlock {
    public SatelliteLauncherBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntityType<?> entity(BlockState state) {
        return BlockRegistry.SATELLITE_LAUNCHER_BLOCK_ENTITY.get();
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!(level.getBlockEntity(pos) instanceof SatelliteLauncherBlockEntity be)) return InteractionResult.PASS;

        if(be.isFormed()) {
            return super.use(state, level, pos, player, hand, hit);
        }

        boolean successForm = be.attemptToForm(state,level,pos,player,hand,hit);
        if(successForm) {

            if(!level.isClientSide) {
                player.sendSystemMessage(Component.literal("Multiblock formed!"));
            }
            return InteractionResult.SUCCESS;
        }


        //print error message
        return InteractionResult.CONSUME;

    }
}
