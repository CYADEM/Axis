package me.tinchx.axis.commands.general.ignore;

import me.tinchx.axis.commands.general.ignore.arguments.IgnoreAddArgument;
import me.tinchx.axis.commands.general.ignore.arguments.IgnoreClearArgument;
import me.tinchx.axis.commands.general.ignore.arguments.IgnoreListArgument;
import me.tinchx.axis.commands.general.ignore.arguments.IgnoreRemoveArgument;
import me.tinchx.axis.utilities.command.AxisCommand;

public class IgnoreCommand extends AxisCommand {

    public IgnoreCommand() {
        super("ignore");
        registerArgument(new IgnoreAddArgument());
        registerArgument(new IgnoreRemoveArgument());
        registerArgument(new IgnoreListArgument());
        registerArgument(new IgnoreClearArgument());
    }
}