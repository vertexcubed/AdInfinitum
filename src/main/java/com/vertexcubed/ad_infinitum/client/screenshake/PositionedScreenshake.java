package com.vertexcubed.ad_infinitum.client.screenshake;

import net.minecraft.client.Camera;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;


/**
 * Code largely lifted from <a href=https://github.com/LodestarMC/Lodestone/tree/main>Lodestone</a>, which is licensed under LGPL 3
 */
public class PositionedScreenshake extends LocalScreenshake {

    public float falloffDistance;
    public float maxDistance;
    private Vec3 pos = Vec3.ZERO;
    public PositionedScreenshake(int duration, float power) {
        super(duration, power);
    }

    public PositionedScreenshake(int duration, float xPower, float yPower, float zPower) {
        super(duration, xPower, yPower, zPower);
    }

    public PositionedScreenshake setPos(Vec3 pos) {
        this.pos = pos;
        return this;
    }

    public PositionedScreenshake setFalloffDistance(float falloffDistance) {
        this.falloffDistance = falloffDistance;
        return this;
    }

    public PositionedScreenshake setMaxDistance(float maxDistance) {
        this.maxDistance = maxDistance;
        return this;
    }


    @Override
    protected float powerEquation(Camera camera, float power) {
        float original = super.powerEquation(camera, power);
        float distance = (float) pos.distanceTo(camera.getPosition());
        if (distance > maxDistance) {
            return 0;
        }
        float distanceMultiplier = 1;
        if (distance > falloffDistance) {
            float remaining = maxDistance - falloffDistance;
            float current = distance - falloffDistance;
            distanceMultiplier = 1 - current / remaining;
        }
        Vector3f lookDirection = camera.getLookVector();
        Vec3 directionToScreenshake = pos.subtract(camera.getPosition()).normalize();
        float angle = Math.max(0, lookDirection.dot(directionToScreenshake.toVector3f()));
        return ((original * distanceMultiplier) + (original * angle)) * 0.5f;
    }
}
