package com.vertexcubed.ad_infinitum.client.shader;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.RegisterShadersEvent;
import team.lodestar.lodestone.registry.client.LodestoneShaderRegistry;
import team.lodestar.lodestone.systems.rendering.shader.ShaderHolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.vertexcubed.ad_infinitum.AdInfinitum.modLoc;

public class CoreShaderRegistry {
    public static final List<ShaderHolder> SHADERS = new ArrayList<>();

    public static ShaderHolder
            HOLO_DOOR = register(modLoc("holo_door"), DefaultVertexFormat.POSITION_TEX_COLOR);



    public static ShaderHolder register(ResourceLocation location, VertexFormat vertexFormat, String... uniformsToCache) {
        ShaderHolder holder = new ShaderHolder(location, vertexFormat, uniformsToCache);
        SHADERS.add(holder);
        return holder;
    }


    public static void registerShaders(RegisterShadersEvent event) throws IOException {
        for (ShaderHolder shader : SHADERS) {
            LodestoneShaderRegistry.registerShader(event, shader);
        }
    }
}
