package com.vertexcubed.ad_infinitum.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReceiver;
import com.llamalad7.mixinextras.sugar.Local;
import com.vertexcubed.ad_infinitum.AdInfinitum;
import com.vertexcubed.ad_infinitum.common.util.PlanetHelper;
import com.vertexcubed.ad_infinitum.server.data.AdInfinitumPlanetData;
import earth.terrarium.adastra.api.planets.Planet;
import earth.terrarium.adastra.common.planets.AdAstraData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(AdAstraData.class)
public class AdAstraDataMixin {

    @Unique private static final Map<ResourceKey<Level>, Planet> DISCARD = new HashMap<>();

    @Inject(method = "apply(Ljava/util/Map;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V", at = @At("HEAD"), remap = false)
    private void ad_infinitum$clearUnlandablePlanets(CallbackInfo ci) {
        AdInfinitumPlanetData.unlandablePlanets().clear();
    }

    @ModifyReceiver(method = "lambda$apply$0(Lnet/minecraft/resources/ResourceLocation;Lcom/google/gson/JsonElement;)V", at= @At(value = "INVOKE", target = "java/util/Map.put (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", ordinal = 0), remap = false)
    private static Map<ResourceKey<Level>, Planet> ad_infinitum$swallowUnlandablePlanets(Map<ResourceKey<Level>, Planet> original, Object key, Object value, @Local(argsOnly = true) ResourceLocation name) {
        Planet planet = (Planet) value;
        if(PlanetHelper.isPlanetUnlandable(planet)) {
            AdInfinitumPlanetData.unlandablePlanets().put(name, planet);
            AdInfinitum.LOGGER.info("Discarding planet" + planet.dimension());
            return DISCARD;
        }
        //Do some additional tomfoolery
        //THINGS THIS POSSIBLY BREAKS:
        //Syncing planets from server -> client (reimplemented)
        //Getting orbit planets
        //solar systems
        return original;
    }

}
