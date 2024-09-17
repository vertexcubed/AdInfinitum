package com.vertexcubed.ad_infinitum.common.item;

import com.vertexcubed.ad_infinitum.common.satellite.Satellite;
import com.vertexcubed.ad_infinitum.common.satellite.TestSatellite;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class TestSatelliteItem extends SatelliteItem {
    public TestSatelliteItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    protected Satellite initializeSatellite(ItemStack stack, Level level, Player player) {
        return new TestSatellite(UUID.randomUUID(), player.getUUID());
    }
}
