package com.vertexcubed.ad_infinitum.common.registry;

import com.vertexcubed.ad_infinitum.AdInfinitum;
import com.vertexcubed.ad_infinitum.common.item.DebugItem;
import com.vertexcubed.ad_infinitum.common.item.TestSatelliteItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, AdInfinitum.MODID);
    public static final RegistryObject<Item> DEBUG_ITEM = ITEMS.register("debug", () -> new DebugItem(new Item.Properties()));

    public static final RegistryObject<Item> TEST_SATELLITE = ITEMS.register("test_satellite", () -> new TestSatelliteItem(new Item.Properties().stacksTo(1)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
