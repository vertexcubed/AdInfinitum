package com.vertexcubed.ad_infinitum.common.util;

import com.vertexcubed.ad_infinitum.AdInfinitum;
import com.vertexcubed.ad_infinitum.server.data.AdInfinitumPlanetData;
import earth.terrarium.adastra.api.planets.Planet;
import net.minecraft.resources.ResourceLocation;

import static com.vertexcubed.ad_infinitum.AdInfinitum.modLoc;

public class PlanetHelper {

    public static boolean isPlanetUnlandable(Planet planet) {
        return planet.dimension().equals(AdInfinitum.UNLANDABLE_PLANET);
    }

    public static ResourceLocation getUnlandablePlanetName(Planet planet) {
        for(ResourceLocation key : AdInfinitumPlanetData.unlandablePlanets().keySet()) {
            AdInfinitum.LOGGER.info("Key: " + key);
            if(AdInfinitumPlanetData.unlandablePlanets().get(key).equals(planet)) {
                return key;
            }
        }
        return modLoc("none");
    }
}
