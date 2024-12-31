package com.vertexcubed.ad_infinitum.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.vertexcubed.ad_infinitum.AdInfinitum;
import com.vertexcubed.ad_infinitum.common.satellite.SatelliteManager;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.UuidArgument;
import net.minecraft.network.chat.Component;

import java.util.UUID;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;


public class SatellitesCommand {


    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        return
                literal("satellites")
                .requires(source -> source.hasPermission(2))
                .then(
                        literal("get")
                        .executes(ctx -> getSatellite(ctx.getSource(), null))
                        .then(
                                argument("uuid", UuidArgument.uuid())
                                .executes(ctx -> getSatellite(ctx.getSource(), UuidArgument.getUuid(ctx, "uuid")))
                        )
                )
                .then(
                        literal("clear")
                        .executes(ctx -> clearSatellites(ctx.getSource()))
                )
        ;
    }


    public static int getSatellite(CommandSourceStack ctx, UUID uuid) {
        if(uuid == null) {
            ctx.sendSuccess(() -> Component.literal(SatelliteManager.getAllSatellites().toString()), true);
        }
        else {
            ctx.sendSuccess(() -> Component.literal(SatelliteManager.getSatellite(uuid).toString()), true);
        }
        return 1;
    }

    public static int clearSatellites(CommandSourceStack ctx) {
        SatelliteManager.clearSatellites(ctx.getLevel());
        ctx.sendSuccess(() -> Component.translatable("command.ad_infinitum.clear_satellites"), true);
        return 1;
    }
}
