package me.tinchx.axis.commands.staff.whitelist;

import me.tinchx.axis.commands.staff.whitelist.arguments.WhitelistCreateArgument;
import me.tinchx.axis.commands.staff.whitelist.arguments.WhitelistDeleteArgument;
import me.tinchx.axis.utilities.command.AxisCommand;

public class WhitelistCommand extends AxisCommand {

    public WhitelistCommand() {
        super("wl");
        registerArgument(new WhitelistCreateArgument());
        registerArgument(new WhitelistDeleteArgument());
    }
}