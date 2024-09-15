package com.vertexcubed.ad_infinitum.common.registry;

import com.vertexcubed.ad_infinitum.AdInfinitum;
import com.vertexcubed.ad_infinitum.common.multiblock.data.MultiblockData;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;

import static com.vertexcubed.ad_infinitum.AdInfinitum.modLoc;

public class MultiblockDataRegistry {
    public static final DeferredRegister<MultiblockData.Type<?>> MULTIBLOCK_DATA = DeferredRegister.create(modLoc("multiblock_data"), AdInfinitum.MODID);
    public static final Supplier<IForgeRegistry<MultiblockData.Type<?>>> MULTIBLOCK_DATA_REGISTRY = MULTIBLOCK_DATA.makeRegistry(RegistryBuilder::new);

}
