package com.vertexcubed.ad_infinitum.server.data;

import earth.terrarium.adastra.api.planets.Planet;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class AdInfinitumPlanetData {
    private static final Map<ResourceLocation, Planet> UNLANDABLE_PLANETS = new HashMap<>();


    public static Map<ResourceLocation, Planet> unlandablePlanets() {
        return UNLANDABLE_PLANETS;
    }

    public static void setUnlandablePlanets(Map<ResourceLocation, Planet> unlandablePlanets) {
        UNLANDABLE_PLANETS.clear();
        UNLANDABLE_PLANETS.putAll(unlandablePlanets);
    }
}
