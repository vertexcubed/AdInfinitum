package com.vertexcubed.ad_infinitum.client.util;

import team.lodestar.lodestone.systems.rendering.VFXBuilders;

public class AdInfinitumWorldVFXBuilder extends VFXBuilders.WorldVFXBuilder {

    public AdInfinitumWorldVFXBuilder() {}


    public static AdInfinitumWorldVFXBuilder createWorld() {
        return new AdInfinitumWorldVFXBuilder();
    }
}
