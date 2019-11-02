package me.tinchx.axis.commands.staff.server;

import me.tinchx.axis.commands.staff.server.arguments.ManagerInfoArgument;
import me.tinchx.axis.commands.staff.server.arguments.ManagerRunArgument;
import me.tinchx.axis.utilities.command.AxisCommand;

public class ServerManagerCommand extends AxisCommand {

    public ServerManagerCommand() {
        super("servermanager");
        registerArgument(new ManagerRunArgument());
        registerArgument(new ManagerInfoArgument());
    }
}