package me.tinchx.axis.commands.staff.gamemode;

import me.tinchx.axis.commands.staff.gamemode.arguments.GamemodeAdventureArgument;
import me.tinchx.axis.commands.staff.gamemode.arguments.GamemodeCreativeArgument;
import me.tinchx.axis.commands.staff.gamemode.arguments.GamemodeSurvivalArgument;
import me.tinchx.axis.utilities.command.AxisCommand;

public class GameModeCommand extends AxisCommand {

    public GameModeCommand() {
        super("gamemode", null, "gm");
        registerArgument(new GamemodeCreativeArgument());
        registerArgument(new GamemodeSurvivalArgument());
        registerArgument(new GamemodeAdventureArgument());
    }
}