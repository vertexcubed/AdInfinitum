package com.vertexcubed.ad_infinitum.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.vertexcubed.ad_infinitum.AdInfinitum;
import com.vertexcubed.ad_infinitum.common.item.SatelliteItem;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CreativeModeInventoryScreen.class)
public class CreativeModeInventoryScreenMixin {

    @ModifyExpressionValue(method = "slotClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;copyWithCount(I)Lnet/minecraft/world/item/ItemStack;", ordinal = 1))
    private ItemStack ad_infinitum$CloneSatellite(ItemStack original) {
        if(!(original.getItem() instanceof SatelliteItem)) return original;
        AdInfinitum.LOGGER.info("Copying satellite item");
        return new ItemStack(original.getItem(), original.getCount(), new CompoundTag());
    }

}