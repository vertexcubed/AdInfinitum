package com.vertexcubed.ad_infinitum.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.vertexcubed.ad_infinitum.AdInfinitum;
import com.vertexcubed.ad_infinitum.common.item.SatelliteItem;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractContainerMenu.class)
public class AbstractContainerMenuMixin {

    @WrapOperation(method = "doClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/AbstractContainerMenu;setCarried(Lnet/minecraft/world/item/ItemStack;)V", ordinal = 5))
    private void ad_infinitum$cloneSatellite(AbstractContainerMenu instance, ItemStack stack, Operation<Void> original) {
        if(!(stack.getItem() instanceof SatelliteItem)) {
            original.call(instance, stack);
            return;
        }
        ItemStack ret = new ItemStack(stack.getItem(), stack.getCount(), new CompoundTag());
        original.call(instance, ret);
    }}
