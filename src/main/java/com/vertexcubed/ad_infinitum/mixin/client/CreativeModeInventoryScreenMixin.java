package com.vertexcubed.ad_infinitum.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.vertexcubed.ad_infinitum.AdInfinitum;
import com.vertexcubed.ad_infinitum.common.item.SatelliteItem;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreativeModeInventoryScreen.class)
public class CreativeModeInventoryScreenMixin {

    @WrapOperation(method = "slotClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/CreativeModeInventoryScreen$ItemPickerMenu;setCarried(Lnet/minecraft/world/item/ItemStack;)V", ordinal = 3))
    private void ad_infinitum$cloneSatellite(CreativeModeInventoryScreen.ItemPickerMenu instance, ItemStack stack, Operation<Void> original) {
        AdInfinitum.LOGGER.debug("Copying item...");
        if(!(stack.getItem() instanceof SatelliteItem)) {
            original.call(instance, stack);
            return;
        }
        AdInfinitum.LOGGER.debug("Copying satellite item!");
        ItemStack ret = new ItemStack(stack.getItem(), stack.getCount(), new CompoundTag());
        original.call(instance, ret);
    }
}