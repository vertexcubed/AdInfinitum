package com.vertexcubed.ad_infinitum.common.registry;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.common.util.Lazy;
import org.lwjgl.glfw.GLFW;

public class KeyMappings {
    public static final Lazy<KeyMapping> SHADER_TEST = Lazy.of(
            () -> new KeyMapping(
                    "key.ad_infinitum.shader_test",
                    KeyConflictContext.IN_GAME,
                    KeyModifier.NONE,
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_O,
                    "key.categories.ad_infinitum"
            ));



    public static void register(RegisterKeyMappingsEvent event) {
        event.register(SHADER_TEST.get());
    }
}
