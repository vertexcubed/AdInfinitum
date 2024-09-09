package com.vertexcubed.ad_infinitum.mixin;

import com.vertexcubed.ad_infinitum.common.blockentity.HoloDoorBlockEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public class ServerLevelMixin {


    @Inject(method = "tickChunk", at = @At("HEAD"))
    private void tickChunk(LevelChunk chunk, int randomTickSpeed, CallbackInfo ci) {
        ServerLevel level = (ServerLevel) (Object) this;
        if(level.getGameTime() % 40 == 0) {
            HoloDoorBlockEntity.invalidateBlocks(level, chunk);
        }
    }
}
