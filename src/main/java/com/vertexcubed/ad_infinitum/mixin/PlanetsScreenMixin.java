package com.vertexcubed.ad_infinitum.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReceiver;
import com.llamalad7.mixinextras.sugar.Local;
import com.vertexcubed.ad_infinitum.common.util.PlanetHelper;
import earth.terrarium.adastra.api.planets.Planet;
import earth.terrarium.adastra.client.screens.PlanetsScreen;
import earth.terrarium.adastra.common.menus.PlanetsMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlanetsScreen.class)
public abstract class PlanetsScreenMixin {

    @Shadow private Planet selectedPlanet;

    @Shadow protected abstract void addSpaceStationButtons(ResourceKey<Level> dimension);

    @Inject(method = "createSelectedPlanetButtons", at = @At("HEAD"), cancellable = true, remap = false)
    private void ad_infinitum$nukeLandButton(CallbackInfo ci) {
        if(selectedPlanet != null && PlanetHelper.isPlanetUnlandable(selectedPlanet)) {
            addSpaceStationButtons(selectedPlanet.orbitIfPresent());
            ci.cancel();
        }
    }

    @ModifyExpressionValue(method = "lambda$init$3(Lnet/minecraft/client/gui/components/Button;)V", at = @At(value = "INVOKE", target = "earth/terrarium/adastra/api/planets/Planet.dimension ()Lnet/minecraft/resources/ResourceKey;"), remap = false)
    private ResourceKey<Level> ad_infinitum$fixPlanet(ResourceKey<Level> original) {
        if(selectedPlanet != null && PlanetHelper.isPlanetUnlandable(selectedPlanet)) {
            return selectedPlanet.orbitIfPresent();
        }
        return original;
    }

    @ModifyExpressionValue(method = "createPlanetButtons", at = @At(value = "INVOKE", target = "earth/terrarium/adastra/common/menus/PlanetsMenu.getPlanetName (Lnet/minecraft/resources/ResourceKey;)Lnet/minecraft/network/chat/Component;"), remap = false)
    private Component ad_infinitum$fixUnlandablePlanetName(Component original, @Local Planet planet) {
        if(PlanetHelper.isPlanetUnlandable(planet)) {
            ResourceLocation name = PlanetHelper.getUnlandablePlanetName(planet);
            return Component.translatable("planet.%s.%s".formatted(name.getNamespace(), name.getPath()));
        }
        return original;
    }
}
