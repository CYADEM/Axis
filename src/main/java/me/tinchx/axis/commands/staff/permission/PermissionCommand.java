package me.tinchx.axis.commands.staff.permission;

import me.tinchx.axis.commands.staff.permission.arguments.PermissionAddArgument;
import me.tinchx.axis.commands.staff.permission.arguments.PermissionRemoveArgument;
import me.tinchx.axis.utilities.command.AxisCommand;

public class PermissionCommand extends AxisCommand {

    public PermissionCommand() {
        super("permission", null, "perms");
        registerArgument(new PermissionAddArgument());
        registerArgument(new PermissionRemoveArgument());
    }
}