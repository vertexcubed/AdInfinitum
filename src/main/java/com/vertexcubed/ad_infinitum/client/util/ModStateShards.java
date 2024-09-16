package com.vertexcubed.ad_infinitum.client.util;

import net.minecraft.client.renderer.RenderStateShard;

public class ModStateShards extends RenderStateShard {
    public ModStateShards(String pName, Runnable pSetupState, Runnable pClearState) {
        super(pName, pSetupState, pClearState);
    }

    public static final RenderStateShard.WriteMaskStateShard COLOR_WRITE = new RenderStateShard.WriteMaskStateShard(true, false);
    public static final RenderStateShard.WriteMaskStateShard DEPTH_WRITE = new RenderStateShard.WriteMaskStateShard(false, true);
}
