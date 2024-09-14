package com.vertexcubed.ad_infinitum.common.satellite.frequency;

import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

import static com.vertexcubed.ad_infinitum.AdInfinitum.modLoc;

public class FrequencyDataType {

    public static final Map<ResourceLocation, FrequencyDataType> DATA_TYPES = new HashMap<>();

    public static final Codec<FrequencyDataType> CODEC = ResourceLocation.CODEC.xmap(DATA_TYPES::get, FrequencyDataType::id);



    public static final FrequencyDataType ENERGY = new FrequencyDataType(modLoc("energy"));
    public static final FrequencyDataType ITEM = new FrequencyDataType(modLoc("item"));
    public static final FrequencyDataType FLUID = new FrequencyDataType(modLoc("fluid"));
    public static final FrequencyDataType REDSTONE = new FrequencyDataType(modLoc("redstone"));
    public static final FrequencyDataType PLANET_DATA = new FrequencyDataType(modLoc("planet_data"));
    public static final FrequencyDataType ME = new FrequencyDataType(modLoc("ae2_me"));

    private final ResourceLocation id;
    public FrequencyDataType(ResourceLocation id) {
        this.id = id;
        DATA_TYPES.put(id, this);
    }
    private ResourceLocation id() {
        return id;
    }

}
