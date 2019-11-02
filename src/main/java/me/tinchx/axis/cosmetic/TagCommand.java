package me.tinchx.axis.cosmetic;

import me.tinchx.axis.cosmetic.arguments.*;
import me.tinchx.axis.utilities.command.AxisCommand;

public class TagCommand extends AxisCommand {

    public TagCommand() {
        super("tag");
        registerArgument(new TagCreateArgument());
        registerArgument(new TagDeleteArgument());
        registerArgument(new TagListArgument());
        registerArgument(new TagPrefixArgument());
        registerArgument(new TagPermArgument());
        registerArgument(new TagSectionArgument());
    }
}