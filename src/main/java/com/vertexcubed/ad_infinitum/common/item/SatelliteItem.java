package com.vertexcubed.ad_infinitum.common.item;

import com.vertexcubed.ad_infinitum.AdInfinitum;
import com.vertexcubed.ad_infinitum.common.satellite.Satellite;
import com.vertexcubed.ad_infinitum.server.capability.SatelliteItemStorage;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public abstract class SatelliteItem extends Item {
    public SatelliteItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void onInventoryTick(ItemStack stack, Level level, Player player, int slotIndex, int selectedIndex) {
        super.onInventoryTick(stack, level, player, slotIndex, selectedIndex);
        stack.getCapability(SatelliteItemStorage.CAP).ifPresent(satelliteStorage -> {
            if(satelliteStorage.getSatellite() == null) {
                satelliteStorage.setSatellite(initializeSatellite(stack, level, player), player, slotIndex);
            }
        });
    }

    public Satellite getSatellite(ItemStack stack) {
        SatelliteItemStorage storage = stack.getCapability(SatelliteItemStorage.CAP).orElse(null);
        if(storage == null) return null;
        return storage.getSatellite();
    }

    protected abstract Satellite initializeSatellite(ItemStack stack, Level level, Player player);
}
