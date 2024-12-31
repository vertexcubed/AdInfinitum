package com.vertexcubed.ad_infinitum.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.vertexcubed.ad_infinitum.common.worldevent.OrbitalStrikeWorldEvent;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.UuidArgument;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.world.phys.Vec3;
import team.lodestar.lodestone.handlers.WorldEventHandler;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class OrbitalStrikeCommand {

    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        return
                literal("orbital_strike")
                .requires(source -> source.hasPermission(2))
                .then(
                        argument("pos", Vec3Argument.vec3())
                        .executes(context -> spawnOrbitalStrike(context.getSource(), Vec3Argument.getVec3(context, "pos")))

                )
        ;
    }

    public static int spawnOrbitalStrike(CommandSourceStack source, Vec3 pos) {
        WorldEventHandler.addWorldEvent(source.getLevel(), new OrbitalStrikeWorldEvent().setPosition(pos.toVector3f()));
        return 1;
    }
}
