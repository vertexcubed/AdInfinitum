package com.vertexcubed.ad_infinitum.common.worldevent;

import com.vertexcubed.ad_infinitum.client.screenshake.LocalScreenshake;
import com.vertexcubed.ad_infinitum.client.screenshake.ScreenshakeHandler;
import com.vertexcubed.ad_infinitum.client.shader.ImpactFramePostProcessor;
import com.vertexcubed.ad_infinitum.client.shader.InvertPostProcessor;
import com.vertexcubed.ad_infinitum.common.registry.WorldEventRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import org.joml.Vector3f;
import team.lodestar.lodestone.systems.worldevent.WorldEventInstance;
import team.lodestar.lodestone.systems.worldevent.WorldEventType;

public class OrbitalStrikeWorldEvent extends WorldEventInstance {

    public static final int EXPLOSION_START = 10;

    private boolean renderExplosion = false;
    private Vector3f position;
    private int age;
    public OrbitalStrikeWorldEvent() {
        super(WorldEventRegistry.ORBITAL_STRIKE);
    }

    @Override
    public void start(Level level) {
        this.age = 0;
        super.start(level);

        if(level.isClientSide) {
            float dist = Minecraft.getInstance().player.position().toVector3f().sub(position).length();
            if(dist > 200) {
                return;
            }
            ImpactFramePostProcessor.INSTANCE.setPosition(position);
            ImpactFramePostProcessor.INSTANCE.setActive(true);

            ScreenshakeHandler.addScreenshake(new LocalScreenshake(20, 0.7f));
        }
    }

    @Override
    public CompoundTag serializeNBT(CompoundTag tag) {
        tag.putFloat("x", position.x);
        tag.putFloat("y", position.y);
        tag.putFloat("z", position.z);
        tag.putBoolean("renderExplosion", renderExplosion);
        return super.serializeNBT(tag);
    }

    @Override
    public WorldEventInstance deserializeNBT(CompoundTag tag) {
        this.position = new Vector3f(tag.getFloat("x"), tag.getFloat("y"), tag.getFloat("z"));
        this.renderExplosion = tag.getBoolean("renderExplosion");
        return super.deserializeNBT(tag);
    }

    @Override
    public void tick(Level level) {
        age++;

        if(age == EXPLOSION_START) {
            renderExplosion = true;

        }



        if(age >= 40) {
            end(level);
        }
    }

    @Override
    public void end(Level level) {
        if(level.isClientSide) {
            ImpactFramePostProcessor.INSTANCE.setActive(false);
            InvertPostProcessor.INSTANCE.setActive(false);
        }
        super.end(level);
    }

    public Vector3f getPosition() {
        return position;
    }

    public OrbitalStrikeWorldEvent setPosition(Vector3f position) {
        this.position = position;
        return this;
    }

    public boolean shouldRenderExplosion() {
        return renderExplosion;
    }

    public int getAge() {
        return age;
    }
}
