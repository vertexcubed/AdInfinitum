package com.vertexcubed.ad_infinitum.common.registry;

import com.vertexcubed.ad_infinitum.AdInfinitum;
import com.vertexcubed.ad_infinitum.common.menu.HoloDoorMenu;
import earth.terrarium.adastra.common.menus.machines.CryoFreezerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MenuRegistry {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, AdInfinitum.MODID);

    public static final RegistryObject<MenuType<HoloDoorMenu>> HOLO_DOOR = MENU_TYPES.register("holo_door", () -> IForgeMenuType.create(HoloDoorMenu::new));

    public static void register(IEventBus eventBus) {
        MENU_TYPES.register(eventBus);
    }
}
