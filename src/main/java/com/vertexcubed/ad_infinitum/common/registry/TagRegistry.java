package com.vertexcubed.ad_infinitum.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;

import java.rmi.registry.Registry;

import static com.vertexcubed.ad_infinitum.AdInfinitum.modLoc;


public class TagRegistry {

    public static final TagKey<DimensionType> SUPER_HOT_DIMENSIONS = dimension("super_hot");


    public static TagKey<DimensionType> dimension(final String name) {
        return TagKey.create(Registries.DIMENSION_TYPE, modLoc(name));
    }
}
