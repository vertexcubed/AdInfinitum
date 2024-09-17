package com.vertexcubed.ad_infinitum.common.menu;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class SatelliteSlot extends Slot {
    public SatelliteSlot(Container pContainer, int pSlot, int pX, int pY) {
        super(pContainer, pSlot, pX, pY);
    }

    @Override
    public boolean mayPlace(ItemStack pStack) {
        return super.mayPlace(pStack);
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }
}
