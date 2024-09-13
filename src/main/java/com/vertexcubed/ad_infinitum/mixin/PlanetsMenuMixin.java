package com.vertexcubed.ad_infinitum.mixin;

import com.llamalad7.mixinextras.injector.ModifyReceiver;
import com.vertexcubed.ad_infinitum.AdInfinitum;
import com.vertexcubed.ad_infinitum.common.util.PlanetHelper;
import com.vertexcubed.ad_infinitum.server.data.AdInfinitumPlanetData;
import earth.terrarium.adastra.api.planets.Planet;
import earth.terrarium.adastra.common.menus.PlanetsMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.List;

@Mixin(PlanetsMenu.class)
public class PlanetsMenuMixin {


    @Inject(method = "lambda$getSortedPlanets$5(Learth/terrarium/adastra/api/planets/Planet;)Ljava/lang/String;", at = @At("HEAD"), cancellable = true, remap = false)
    private void ad_infinitum$sortUnlandablePlanets(Planet planet, CallbackInfoReturnable<String> cir) {
        AdInfinitum.LOGGER.info("Replacing planet name for " + planet.dimension());
        if(PlanetHelper.isPlanetUnlandable(planet)) {
            cir.setReturnValue(PlanetHelper.getUnlandablePlanetName(planet).toString());
        }
    }

    @ModifyReceiver(method = "getSortedPlanets", at= @At(value = "INVOKE", target = "java/util/Collection.stream ()Ljava/util/stream/Stream;"), remap = false)
    private Collection<Planet> ad_infinitum$addUnlandablePlanets(Collection<Planet> original) {
        List<Planet> list = new java.util.ArrayList<>(List.copyOf(original));
        list.addAll(AdInfinitumPlanetData.unlandablePlanets().values());
        return list;
    }

}
