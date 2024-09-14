package com.vertexcubed.ad_infinitum.common.menu;

import com.vertexcubed.ad_infinitum.common.blockentity.HoloDoorBlockEntity;
import com.vertexcubed.ad_infinitum.common.registry.MenuRegistry;
import earth.terrarium.adastra.common.menus.base.MachineMenu;
import earth.terrarium.adastra.common.menus.configuration.EnergyConfiguration;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SimpleContainerData;
import org.jetbrains.annotations.Nullable;

public class HoloDoorMenu extends MachineMenu<HoloDoorBlockEntity> {


    public HoloDoorMenu(int id, Inventory inv, FriendlyByteBuf buf) {
        this(id, inv, (HoloDoorBlockEntity) inv.player.level().getBlockEntity(buf.readBlockPos()));
    }

    public HoloDoorMenu(int id, Inventory inventory, HoloDoorBlockEntity entity) {
        super(MenuRegistry.HOLO_DOOR.get(), id, inventory, entity);
    }

    @Override
    protected void addConfigSlots() {
        this.addConfigSlot(new EnergyConfiguration(2, 147, 50, entity.getEnergyStorage()));
    }



    @Override
    protected int getContainerInputEnd() {
        return 0;
    }

    @Override
    protected int getInventoryStart() {
        return 0;
    }

    @Override
    public int getPlayerInvYOffset() {
        return 102;
    }


}
