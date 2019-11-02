package me.tinchx.axis.cosmetic.arguments;

import me.tinchx.axis.AxisAPI;
import me.tinchx.axis.cosmetic.Tag;
import me.tinchx.axis.utilities.chat.ColorText;
import me.tinchx.axis.utilities.command.AxisArgument;
import me.tinchx.sponsor.SponsorPlugin;
import org.bukkit.command.CommandSender;

public class TagPrefixArgument extends AxisArgument {

    public TagPrefixArgument() {
        super("prefix", null, null, "setprefix");
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + name + " <tagName> <prefix>";
    }

    @Override
    public void onExecute(CommandSender sender, String label, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(ColorText.translate("&cUsage: " + getUsage(label)));
        } else {
            Tag tag = AxisAPI.getTagByName(args[1]);
            if (tag == null) {
                sender.sendMessage(ColorText.translate("&cA tag with that name doesn't exists."));
            } else {
                tag.setPrefix(args[2]);
                tag.save();
                sender.sendMessage(ColorText.translate("&aTag " + tag.getPrefix() + " &acalled &7(&f" + tag.getName() + "&a) has been updated."));
                SponsorPlugin.getInstance().getRedisPublisher().write("AXIS", "cosmetics;update");
            }
        }
    }
}