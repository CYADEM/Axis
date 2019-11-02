package me.tinchx.axis.cosmetic.arguments;

import me.tinchx.axis.cosmetic.Tag;
import me.tinchx.axis.utilities.chat.ColorText;
import me.tinchx.axis.utilities.command.AxisArgument;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;

public class TagListArgument extends AxisArgument {

    public TagListArgument() {
        super("list");
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + name;
    }

    @Override
    public void onExecute(CommandSender sender, String label, String[] args) {
        if (Tag.getTags().isEmpty()) {
            sender.sendMessage(ColorText.translate("&cThere are no tags created."));
        } else {
            sender.sendMessage(ColorText.translate("&7&m" + StringUtils.repeat("-", 45)));
            sender.sendMessage(ColorText.translate("&6Tags&7: &f(" + Tag.getTags().size() + ')'));
            sender.sendMessage("");
            for(Tag tag : Tag.getTags()) {
                sender.sendMessage(ColorText.translate(" &7- " + (tag.getPrefix() == null ? tag.getName() : tag.getPrefix() + " &7(&f" + tag.getName() + "&7)")));
            }
            sender.sendMessage(ColorText.translate("&7&m" + StringUtils.repeat("-", 45)));
        }
    }
}