package com.vertexcubed.ad_infinitum.common.registry;

import com.vertexcubed.ad_infinitum.AdInfinitum;
import com.vertexcubed.ad_infinitum.common.multiblock.data.GenericMachineData;
import com.vertexcubed.ad_infinitum.common.multiblock.data.MultiblockData;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

import static com.vertexcubed.ad_infinitum.AdInfinitum.modLoc;

public class MultiblockDataRegistry {
    public static final DeferredRegister<MultiblockData.Type<?>> MULTIBLOCK_DATA = DeferredRegister.create(modLoc("multiblock_data"), AdInfinitum.MODID);
    public static final Supplier<IForgeRegistry<MultiblockData.Type<?>>> MULTIBLOCK_DATA_REGISTRY = MULTIBLOCK_DATA.makeRegistry(RegistryBuilder::new);

    public static final RegistryObject<MultiblockData.Type<GenericMachineData>> GENERIC_MACHINE_DATA = MULTIBLOCK_DATA.register("generic_machine", () -> new MultiblockData.Type<>(GenericMachineData.CODEC));

    public static void register(IEventBus eventBus) {
        MULTIBLOCK_DATA.register(eventBus);
    }
}
