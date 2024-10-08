package com.vertexcubed.ad_infinitum.common.registry;

import com.vertexcubed.ad_infinitum.AdInfinitum;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class TabRegistry {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, AdInfinitum.MODID);

    public static final RegistryObject<CreativeModeTab> TAB = TABS.register(AdInfinitum.MODID,
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup." + AdInfinitum.MODID))
                    .icon(() -> new ItemStack(Items.BARRIER))
                    .displayItems((parameters, output) -> {
                        ItemRegistry.ITEMS.getEntries().forEach(item -> output.accept(item.get()));
//                        FluidRegistry.FLUIDS.getEntries().forEach(fluid -> output.accept(fluid.bucket()));
                        BlockRegistry.BLOCKS.getEntries().forEach(block -> output.accept(block.get().asItem()));

                    }).build());

    public static void register(IEventBus eventBus) {
        TABS.register(eventBus);
    }

}