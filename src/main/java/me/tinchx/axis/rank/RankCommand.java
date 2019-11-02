package me.tinchx.axis.rank;

import me.tinchx.axis.rank.arguments.*;
import me.tinchx.axis.utilities.command.AxisCommand;

public class RankCommand extends AxisCommand {

    public RankCommand() {
        super("rank");
        registerArgument(new RankCreateArgument());
        registerArgument(new RankDeleteArgument());
        registerArgument(new RankPrefixArgument());
        registerArgument(new RankSuffixArgument());
        registerArgument(new RankColorArgument());
        registerArgument(new RankPriorityArgument());
        registerArgument(new RankListArgument());
        registerArgument(new RankSetArgument());
        registerArgument(new RankImportArgument());
        registerArgument(new RankInfoArgument());
        registerArgument(new RankPermsArgument());
        registerArgument(new RankInheritanceArgument());
    }
}
