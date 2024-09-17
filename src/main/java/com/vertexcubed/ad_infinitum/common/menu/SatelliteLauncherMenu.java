package com.vertexcubed.ad_infinitum.common.menu;

import com.vertexcubed.ad_infinitum.common.blockentity.SatelliteLauncherBlockEntity;
import com.vertexcubed.ad_infinitum.common.registry.MenuRegistry;
import earth.terrarium.adastra.common.menus.base.MachineMenu;
import earth.terrarium.adastra.common.menus.configuration.EnergyConfiguration;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

public class SatelliteLauncherMenu extends MachineMenu<SatelliteLauncherBlockEntity> {


    public SatelliteLauncherMenu(int id, Inventory inv, FriendlyByteBuf buf) {
        this(id, inv, (SatelliteLauncherBlockEntity) inv.player.level().getBlockEntity(buf.readBlockPos()));
    }

    public SatelliteLauncherMenu(int id, Inventory inventory, SatelliteLauncherBlockEntity entity) {
        super(MenuRegistry.SATELLITE_LAUNCHER.get(), id, inventory, entity);
    }

    @Override
    protected void addConfigSlots() {
        this.addConfigSlot(new EnergyConfiguration(0, 149, 21, entity.getEnergyStorage()));
    }

    @Override
    protected void addMenuSlots() {
        super.addMenuSlots();
        int xStart = 8;
        int yStart = 11;
        this.addSlot(new SatelliteSlot(this.entity, 1, xStart, yStart));
        this.addSlot(new SatelliteSlot(this.entity, 2, xStart + 18, yStart));
        this.addSlot(new SatelliteSlot(this.entity, 3, xStart, yStart + 18));
        this.addSlot(new SatelliteSlot(this.entity, 4, xStart + 18, yStart + 18));
    }

    @Override
    protected int startIndex() {
        return 1;
    }

    @Override
    protected int getContainerInputEnd() {
        return 4;
    }

    @Override
    protected int getInventoryStart() {
        return 4;
    }

    @Override
    public int getPlayerInvYOffset() {
        return 98;
    }


}
