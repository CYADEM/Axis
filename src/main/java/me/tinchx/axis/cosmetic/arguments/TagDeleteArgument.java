package me.tinchx.axis.cosmetic.arguments;

import me.tinchx.axis.AxisAPI;
import me.tinchx.axis.cosmetic.Tag;
import me.tinchx.axis.utilities.chat.ColorText;
import me.tinchx.axis.utilities.command.AxisArgument;
import me.tinchx.sponsor.SponsorPlugin;
import org.bukkit.command.CommandSender;

public class TagDeleteArgument extends AxisArgument {

    public TagDeleteArgument() {
        super("delete");
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + name + " <tagName>";
    }

    @Override
    public void onExecute(CommandSender sender, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ColorText.translate("&cUsage: " + getUsage(label)));
        } else {
            Tag tag = AxisAPI.getTagByName(args[1]);
            if (tag == null) {
                sender.sendMessage(ColorText.translate("&cA tag with that name doesn't exists."));
            } else {
                SponsorPlugin.getInstance().getRedisPublisher().write("AXIS", "cosmetics;delete;" + tag.getName());
                SponsorPlugin.getInstance().getRedisPublisher().write("AXIS", "cosmetics;update");
                sender.sendMessage(ColorText.translate("&cTag " + (tag.getPrefix() == null ? tag.getName() : tag.getPrefix()) + " &chas been successfully deleted!"));
                tag.remove();
            }
        }
    }
}