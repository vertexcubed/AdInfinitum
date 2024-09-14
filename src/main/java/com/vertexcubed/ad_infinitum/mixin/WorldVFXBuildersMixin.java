package com.vertexcubed.ad_infinitum.mixin;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import com.vertexcubed.ad_infinitum.AdInfinitum;
import com.vertexcubed.ad_infinitum.client.util.AdInfinitumWorldVFXBuilder;
import org.checkerframework.checker.units.qual.A;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import team.lodestar.lodestone.systems.rendering.VFXBuilders;

@Mixin(VFXBuilders.WorldVFXBuilder.class)
public class WorldVFXBuildersMixin {

    @Inject(method = "<clinit>", at=@At("TAIL"), remap = false)
    private static void ad_infinitum$adNormalSupport(CallbackInfo ci) {

    }
}
