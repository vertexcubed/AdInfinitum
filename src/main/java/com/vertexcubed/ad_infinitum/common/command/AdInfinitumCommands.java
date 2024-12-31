package com.vertexcubed.ad_infinitum.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.vertexcubed.ad_infinitum.AdInfinitum;
import net.minecraft.commands.CommandSourceStack;

import static net.minecraft.commands.Commands.literal;

public class AdInfinitumCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralCommandNode<CommandSourceStack> cmd = dispatcher.register(
                literal(AdInfinitum.MODID)
                .then(SatellitesCommand.register())
                .then(OrbitalStrikeCommand.register())

        );
        dispatcher.register(
                literal("adinf")
                .redirect(cmd)
        );
    }
}
