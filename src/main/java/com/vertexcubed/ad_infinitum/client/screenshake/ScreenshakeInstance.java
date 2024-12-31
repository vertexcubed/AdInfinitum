package com.vertexcubed.ad_infinitum.client.screenshake;

import net.minecraft.client.Camera;

/**
 * Code largely lifted from <a href=https://github.com/LodestarMC/Lodestone/tree/main>Lodestone</a>, which is licensed under LGPL 3
 */
public abstract class ScreenshakeInstance {

    protected final int duration;
    protected int progress = 0;
    public ScreenshakeInstance(int duration) {
        this.duration = duration;
    }


    public void tick(Camera camera) {
        progress++;
    }

    public int getProgress() {
        return progress;
    }

    public int getDuration() {
        return duration;
    }

    public abstract float getXRotPower(Camera camera);
    public abstract float getYRotPower(Camera camera);
    public abstract float getZRotPower(Camera camera);

}
