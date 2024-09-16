package com.vertexcubed.ad_infinitum.common.registry;

import com.vertexcubed.ad_infinitum.AdInfinitum;
import com.vertexcubed.ad_infinitum.common.satellite.SatelliteType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;

import static com.vertexcubed.ad_infinitum.AdInfinitum.modLoc;

public class SatelliteRegistry {

    public static final DeferredRegister<SatelliteType<?>> SATELLITE_TYPES = DeferredRegister.create(modLoc("satellite_types"), AdInfinitum.MODID);
    public static final Supplier<IForgeRegistry<SatelliteType<?>>> SATELLITE_TYPE_REGISTRY = SATELLITE_TYPES.makeRegistry(RegistryBuilder::new);



    public static void register(IEventBus eventBus) {
        SATELLITE_TYPES.register(eventBus);
    }
}
