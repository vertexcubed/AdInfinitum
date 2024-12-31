package com.vertexcubed.ad_infinitum.client.screenshake;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.synth.PerlinNoise;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;

import java.util.ArrayList;

/**
 * Code largely lifted from <a href=https://github.com/LodestarMC/Lodestone/tree/main>Lodestone</a>, which is licensed under LGPL 3.
 * <p>
 * Heavily modified to suit my needs.
 */
public class ScreenshakeHandler {

    public static final ArrayList<ScreenshakeInstance> INSTANCES = new ArrayList<>();

    private static float xRotPower = 0.0f;
    private static float yRotPower = 0.0f;
    private static float zRotPower = 0.0f;

    private static PerlinNoise xNoise;
    private static PerlinNoise yNoise;
    private static PerlinNoise zNoise;

    public static void init() {
        //yeah idk
        int seed = 69;
        xNoise = PerlinNoise.create(RandomSource.create(seed), -7, 1, 1, 1);
        yNoise = PerlinNoise.create(RandomSource.create(seed + 10), -7, 1, 1, 1);
        zNoise = PerlinNoise.create(RandomSource.create(seed + 20), -7, 1, 1, 1);
    }

    public static void onClientTick(TickEvent.ClientTickEvent event) {
        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        float globalScreenshakeModifier = 1.0f;


        xRotPower = 0.0f;
        yRotPower = 0.0f;
        zRotPower = 0.0f;
        for(ScreenshakeInstance i : INSTANCES) {
            i.tick(camera);
            xRotPower += i.getXRotPower(camera);
            yRotPower += i.getYRotPower(camera);
            zRotPower += i.getZRotPower(camera);
        }
        xRotPower *= globalScreenshakeModifier;
        yRotPower *= globalScreenshakeModifier;
        zRotPower *= globalScreenshakeModifier;


        INSTANCES.removeIf(i -> i.getProgress() >= i.getDuration());
    }

    public static void setupCamera(ViewportEvent.ComputeCameraAngles event) {
        if(Minecraft.getInstance().level == null) return;
        float maxAngle = 20;
        float speed = 30;
        float pitch = maxAngle * xRotPower * (float) xNoise.getValue(speed * (Minecraft.getInstance().level.getGameTime() + event.getPartialTick()), 0, 0);
        float yaw = maxAngle * yRotPower * (float) yNoise.getValue(speed * (Minecraft.getInstance().level.getGameTime() + event.getPartialTick()), 0, 0);
        float roll = maxAngle * zRotPower * (float) zNoise.getValue(speed * (Minecraft.getInstance().level.getGameTime() + event.getPartialTick()), 0, 0);

        event.setPitch(event.getPitch() + pitch);
        event.setYaw(event.getYaw() + yaw);
        event.setRoll(event.getRoll() + roll);

//        StylishCombat.LOGGER.info("Screenshakes: " + INSTANCES);
    }

    public static void addScreenshake(ScreenshakeInstance instance) {
        INSTANCES.add(instance);
    }


}
