package com.vertexcubed.ad_infinitum.common.registry;

import com.mojang.serialization.Codec;
import com.vertexcubed.ad_infinitum.AdInfinitum;
import com.vertexcubed.ad_infinitum.common.multiblock.matcher.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

import static com.vertexcubed.ad_infinitum.AdInfinitum.modLoc;

public class StateMatcherRegistry {
    public static final DeferredRegister<StateMatcher.Type<?>> STATE_MATCHERS = DeferredRegister.create(modLoc("state_matchers"), AdInfinitum.MODID);
    public static final Supplier<IForgeRegistry<StateMatcher.Type<?>>> STATE_MATCHER_REGISTRY = STATE_MATCHERS.makeRegistry(RegistryBuilder::new);


    public static final RegistryObject<StateMatcher.Type<AnyMatcher>> ANY = register("any", AnyMatcher.CODEC);
    public static final RegistryObject<StateMatcher.Type<AirMatcher>> AIR = register("air", AirMatcher.CODEC);
    public static final RegistryObject<StateMatcher.Type<BlockMatcher>> BLOCK = register("block", BlockMatcher.CODEC);
    public static final RegistryObject<StateMatcher.Type<TagMatcher>> TAG = register("tag", TagMatcher.CODEC);


    private static <S extends StateMatcher> RegistryObject<StateMatcher.Type<S>> register(String name, Codec<S> codec) {
        return STATE_MATCHERS.register(name, () -> new StateMatcher.Type<>(codec));
    }


    public static void register(IEventBus eventBus) {
        STATE_MATCHERS.register(eventBus);
    }
}
