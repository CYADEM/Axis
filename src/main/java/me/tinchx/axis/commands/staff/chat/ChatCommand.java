package me.tinchx.axis.commands.staff.chat;

import me.tinchx.axis.commands.staff.chat.arguments.ChatSlowArgument;
import me.tinchx.axis.commands.staff.chat.arguments.ChatToggleArgument;
import me.tinchx.axis.utilities.command.AxisCommand;

public class ChatCommand extends AxisCommand {

    public ChatCommand() {
        super("chat");
        registerArgument(new ChatToggleArgument());
        registerArgument(new ChatSlowArgument());
    }
}