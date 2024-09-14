package com.vertexcubed.ad_infinitum;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.logging.LogUtils;
import com.vertexcubed.ad_infinitum.client.util.AdInfinitumWorldVFXBuilder;
import com.vertexcubed.ad_infinitum.common.registry.BlockRegistry;
import com.vertexcubed.ad_infinitum.common.registry.MenuRegistry;
import com.vertexcubed.ad_infinitum.common.registry.TabRegistry;
import earth.terrarium.adastra.api.planets.PlanetApi;
import earth.terrarium.adastra.common.network.messages.ServerboundConstructSpaceStationPacket;
import earth.terrarium.adastra.common.utils.ModUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import team.lodestar.lodestone.systems.rendering.VFXBuilders;


@Mod(AdInfinitum.MODID)
public class AdInfinitum {
    public static final String MODID = "ad_infinitum";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static ResourceLocation modLoc(String id) {
        return new ResourceLocation(MODID, id);
    }

    public static final ResourceKey<Level> UNLANDABLE_PLANET = ResourceKey.create(Registries.DIMENSION, modLoc("unlandable"));

    public static void printDebugInfoHandle(ServerboundConstructSpaceStationPacket packet, Player player) {
        AdInfinitum.LOGGER.info(packet.dimension().toString());
        var planet = PlanetApi.API.getPlanet(packet.dimension());
        if (planet == null) {
            AdInfinitum.LOGGER.info("Planet is null!");
        }
        ServerLevel targetLevel = ((ServerLevel) player.level()).getServer().getLevel(planet.orbitIfPresent());
        if (targetLevel == null) {
            AdInfinitum.LOGGER.info("Target level is null!");
        }
        if (!PlanetApi.API.isSpace(targetLevel)) {
            AdInfinitum.LOGGER.info("Target is not a space!");
        };
        if (!ModUtils.canTeleportToPlanet(player, planet)) {
            AdInfinitum.LOGGER.info("Can't teleport to planet for some reason!");
        }
    }

    public AdInfinitum() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
//        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        BlockRegistry.register(modEventBus);
        modEventBus.addListener(BlockRegistry::registerBlockItems);

        TabRegistry.register(modEventBus);
        MenuRegistry.register(modEventBus);
    }

}
