package com.vertexcubed.ad_infinitum.client.renderer;

import com.google.common.collect.ImmutableMap;
import com.vertexcubed.ad_infinitum.common.registry.SatelliteRegistry;
import com.vertexcubed.ad_infinitum.common.satellite.Satellite;
import com.vertexcubed.ad_infinitum.common.satellite.SatelliteType;

import java.util.HashMap;
import java.util.Map;

public class SatelliteRenderers {

    public static final Map<SatelliteType<?>, SatelliteRenderer.Provider<?>> SATELLITE_RENDERERS = new HashMap<>();




    public static <T extends Satellite> void register(SatelliteType<T> type, SatelliteRenderer.Provider<T> renderer) {
        SATELLITE_RENDERERS.put(type, renderer);
    }

    public static Map<SatelliteType<?>, SatelliteRenderer<?>> createRenderers() {
        ImmutableMap.Builder<SatelliteType<?>, SatelliteRenderer<?>> builder = ImmutableMap.builder();
        SATELLITE_RENDERERS.forEach((type, provider) -> {
            try {
                builder.put(type, provider.create());
            }
            catch (Exception e) {
                throw new IllegalArgumentException("Failed to create renderer for satellite " + SatelliteRegistry.SATELLITE_TYPE_REGISTRY.get().getKey(type), e);
            }
        });
        return builder.build();
    }
}
