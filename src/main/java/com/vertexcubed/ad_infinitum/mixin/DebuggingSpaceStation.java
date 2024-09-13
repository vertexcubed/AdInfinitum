package com.vertexcubed.ad_infinitum.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.vertexcubed.ad_infinitum.AdInfinitum;
import earth.terrarium.adastra.common.network.messages.ServerboundConstructSpaceStationPacket;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(targets = "earth/terrarium/adastra/common/network/messages/ServerboundConstructSpaceStationPacket$Type")
public class DebuggingSpaceStation {

    @Inject(method = "lambda$handle$0(Learth/terrarium/adastra/common/network/messages/ServerboundConstructSpaceStationPacket;Lnet/minecraft/world/entity/player/Player;)V", at=@At("RETURN"), remap = false)
    private static void debugHandle(ServerboundConstructSpaceStationPacket packet, Player player, CallbackInfo ci) {
        AdInfinitum.LOGGER.info("Returning from creating station! Printing debug info: ");
        AdInfinitum.printDebugInfoHandle(packet, player);
    }

}
