package com.vertexcubed.ad_infinitum;

import com.mojang.logging.LogUtils;
import com.vertexcubed.ad_infinitum.common.registry.BlockRegistry;
import com.vertexcubed.ad_infinitum.common.registry.TabRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(AdInfinitum.MODID)
public class AdInfinitum {
    public static final String MODID = "ad_infinitum";
    public static final Logger LOGGER = LogUtils.getLogger();

    public AdInfinitum() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
//        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        BlockRegistry.register(modEventBus);
        modEventBus.addListener(BlockRegistry::registerBlockItems);

        TabRegistry.register(modEventBus);
    }

}
