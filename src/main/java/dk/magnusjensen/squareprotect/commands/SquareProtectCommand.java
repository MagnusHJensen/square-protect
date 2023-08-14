package dk.magnusjensen.squareprotect.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class SquareProtectCommand {
    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        return Commands.literal("sq")
            .then(LookupSubCommands.build());
    }
}
