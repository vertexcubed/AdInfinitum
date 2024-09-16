package com.vertexcubed.ad_infinitum.client.screen;

import com.vertexcubed.ad_infinitum.AdInfinitum;
import com.vertexcubed.ad_infinitum.common.blockentity.SatelliteLauncherBlockEntity;
import com.vertexcubed.ad_infinitum.common.menu.SatelliteLauncherMenu;
import earth.terrarium.adastra.client.components.machines.OptionsBarWidget;
import earth.terrarium.adastra.client.screens.base.MachineScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class SatelliteLauncherScreen extends MachineScreen<SatelliteLauncherMenu, SatelliteLauncherBlockEntity> {

    public static final ResourceLocation TEXTURE = new ResourceLocation("ad_astra", "textures/gui/container/etrionic_blast_furnace.png");
    public static final ResourceLocation SLOT = new ResourceLocation("ad_astra", "textures/gui/container/slots/steel.png");


    public SatelliteLauncherScreen(SatelliteLauncherMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component, TEXTURE, SLOT, 177, 201);
    }
}
