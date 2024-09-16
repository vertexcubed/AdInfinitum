package com.vertexcubed.ad_infinitum.mixin;

import com.vertexcubed.ad_infinitum.common.blockentity.HoloDoorBlockEntity;
import earth.terrarium.adastra.common.utils.floodfill.FloodFill3D;
import it.unimi.dsi.fastutil.longs.LongArrayFIFOQueue;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(FloodFill3D.class)
public class FloodFill3DMixin {


//    @ModifyExpressionValue(method = "<clinit>()V", at = @At(value = "FIELD", target = "earth/terrarium/adastra/common/utils/floodfill/FloodFill3D.TEST_FULL_SEAL : Learth/terrarium/adastra/common/utils/floodfill/FloodFill3D$SolidBlockPredicate;", opcode = Opcodes.PUTSTATIC), remap = false)
//    @Final private static FloodFill3D.SolidBlockPredicate ad_infinitum$modifyTestFullSeal(FloodFill3D.SolidBlockPredicate original) {
//        return (level, pos, state, positions, queue, direction) -> {
//
//            return original.test(level, pos, state, positions, queue, direction);
//        };
//    }

    @Inject(method = "lambda$static$0(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lit/unimi/dsi/fastutil/longs/LongSet;Lit/unimi/dsi/fastutil/longs/LongArrayFIFOQueue;Lnet/minecraft/core/Direction;)Z", at=@At("HEAD"), remap = false, cancellable = true)
    private static void ad_infinitum$modifyTestFullSeal(Level level, BlockPos pos, BlockState state, LongSet positions, LongArrayFIFOQueue queue, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        if(HoloDoorBlockEntity.testBlock(level, pos, state)) {
            cir.setReturnValue(false);
        }

    }
}
