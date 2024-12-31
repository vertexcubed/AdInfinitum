package com.vertexcubed.ad_infinitum.common.registry;

import com.vertexcubed.ad_infinitum.common.worldevent.OrbitalStrikeWorldEvent;
import team.lodestar.lodestone.registry.common.LodestoneWorldEventTypeRegistry;
import team.lodestar.lodestone.systems.worldevent.WorldEventType;

import static com.vertexcubed.ad_infinitum.AdInfinitum.modLoc;

public class WorldEventRegistry {
    public static final WorldEventType
            ORBITAL_STRIKE = LodestoneWorldEventTypeRegistry.registerEventType(new WorldEventType(modLoc("orbital_strike"), OrbitalStrikeWorldEvent::new, true));
}
