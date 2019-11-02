package me.tinchx.axis.listeners;

import com.mongodb.client.model.Filters;
import me.tinchx.axis.AxisAPI;
import me.tinchx.axis.AxisConfiguration;
import me.tinchx.axis.AxisPlugin;
import me.tinchx.axis.cosmetic.Tag;
import me.tinchx.axis.listeners.events.PlayerRankChangedEvent;
import me.tinchx.axis.profile.Profile;
import me.tinchx.axis.punishment.Punishment;
import me.tinchx.axis.punishment.PunishmentHelper;
import me.tinchx.axis.punishment.PunishmentType;
import me.tinchx.axis.rank.Rank;
import me.tinchx.axis.server.Server;
import me.tinchx.axis.server.ServerProfile;
import me.tinchx.axis.utilities.AxisUtils;
import me.tinchx.axis.utilities.chat.ChatUtil;
import me.tinchx.axis.utilities.chat.ColorText;
import me.tinchx.axis.utilities.chat.MessageUtil;
import me.tinchx.axis.utilities.task.TaskUtil;
import me.tinchx.sponsor.events.PacketReceiveEvent;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;

public class PacketListener implements Listener {

    @EventHandler
    public void onPacketReceive(PacketReceiveEvent event) {
        if (!event.getChannel().equalsIgnoreCase("AXIS")) {
            for (int i = 0; i < 5; i++) {
                System.out.println("[CHANNEL] " + event.getChannel());
            }
            return;
        }
        String argument = event.getArguments()[1], sender, server;
        switch (event.getCommand()) {
            case "serverprofile": {
                server = argument;

                if (Server.getServerByName(server) == null) {
                    Server.addServer(new ServerProfile(server));
                }

                ServerProfile profile = Server.getServerByName(server);

                profile.setOnlinePlayers(Integer.valueOf(event.getArguments()[2]));
                profile.setMaxPlayers(Integer.valueOf(event.getArguments()[3]));
                profile.setOnlineStaffs(Integer.valueOf(event.getArguments()[4]));
                profile.setOnlineOperators(Integer.valueOf(event.getArguments()[5]));
                profile.setWhitelisted(Boolean.valueOf(event.getArguments()[6]));
                break;
            }
            case "staffchat": {
                server = argument;
                sender = event.getArguments()[2];
                String staffMessage = event.getArguments()[3];
                AxisUtils.getOnlineStaff().forEach(player -> player.sendMessage(new MessageUtil().setVariable("{0}", server).setVariable("{1}", sender).setVariable("{2}", staffMessage).format(AxisConfiguration.getString("staff_chat_format"))));
                AxisAPI.sendToConsole(new MessageUtil().setVariable("{0}", server).setVariable("{1}", sender).setVariable("{2}", staffMessage).format(AxisConfiguration.getString("staff_chat_format")));
                break;
            }
            case "adminchat": {
                server = argument;
                sender = event.getArguments()[2];
                String staffMessage = event.getArguments()[3];
                AxisUtils.getOnlineStaff().forEach(player -> {
                    if (player.hasPermission(AxisUtils.STAFF_PERMISSION + ".admin")) {
                        player.sendMessage(new MessageUtil().setVariable("{0}", server).setVariable("{1}", sender).setVariable("{2}", staffMessage).format(AxisConfiguration.getString("admin_chat_format")));
                    }
                });
                AxisAPI.sendToConsole(new MessageUtil().setVariable("{0}", server).setVariable("{1}", sender).setVariable("{2}", staffMessage).format(AxisConfiguration.getString("admin_chat_format")));
                break;
            }
            case "ownerchat": {
                server = argument;
                sender = event.getArguments()[2];
                String staffMessage = event.getArguments()[3];
                AxisUtils.getOnlineOperators().forEach(player -> player.sendMessage(new MessageUtil().setVariable("{0}", server).setVariable("{1}", sender).setVariable("{2}", staffMessage).format(AxisConfiguration.getString("owner_chat_format"))));
                AxisAPI.sendToConsole(new MessageUtil().setVariable("{0}", server).setVariable("{1}", sender).setVariable("{2}", staffMessage).format(AxisConfiguration.getString("owner_chat_format")));
                break;
            }
            case "staffjoin": {
                sender = argument;
                server = event.getArguments()[2];
                AxisUtils.getOnlineStaff().forEach(player -> player.sendMessage(new MessageUtil().setVariable("{0}", sender).setVariable("{1}", server).format(AxisConfiguration.getString("staff_join"))));
                AxisAPI.sendToConsole(new MessageUtil().setVariable("{0}", sender).setVariable("{1}", server).format(AxisConfiguration.getString("staff_join")));
                break;
            }
            case "staffleave": {
                sender = argument;
                server = event.getArguments()[2];
                AxisUtils.getOnlineStaff().forEach(player -> player.sendMessage(new MessageUtil().setVariable("{0}", sender).setVariable("{1}", server).format(AxisConfiguration.getString("staff_leave"))));
                AxisAPI.sendToConsole(new MessageUtil().setVariable("{0}", sender).setVariable("{1}", server).format(AxisConfiguration.getString("staff_leave")));
                break;
            }
            case "serverstart": {
                server = argument;
                AxisUtils.getOnlineOperators().forEach(player -> player.sendMessage(new MessageUtil().setVariable("{0}", server).format(AxisConfiguration.getString("server_start"))));
                AxisAPI.sendToConsole(new MessageUtil().setVariable("{0}", server).format(AxisConfiguration.getString("server_start")));
                break;
            }
            case "serverstop": {
                server = argument;
                AxisUtils.getOnlineOperators().forEach(player -> player.sendMessage(new MessageUtil().setVariable("{0}", server).format(AxisConfiguration.getString("server_stop"))));
                AxisAPI.sendToConsole(new MessageUtil().setVariable("{0}", server).format(AxisConfiguration.getString("server_stop")));
                break;
            }
            case "request": {
                sender = argument;
                server = event.getArguments()[2];
                String request = event.getArguments()[3];
                AxisUtils.getOnlineStaff().forEach(player -> player.sendMessage(new MessageUtil().setVariable("{0}", server).setVariable("{1}", sender).setVariable("{2}", request).format(AxisConfiguration.getString("request"))));
                AxisAPI.sendToConsole(new MessageUtil().setVariable("{0}", server).setVariable("{1}", sender).setVariable("{2}", request).format(AxisConfiguration.getString("request")));
                break;
            }
            case "report": {
                sender = argument;
                server = event.getArguments()[2];
                String reported = event.getArguments()[3];
                String reason = event.getArguments()[4];
                AxisUtils.getOnlineStaff().forEach(player -> player.sendMessage(new MessageUtil().setVariable("{0}", server).setVariable("{1}", reported).setVariable("{2}", sender).setVariable("{3}", reason).format(AxisConfiguration.getString("report"))));
                AxisAPI.sendToConsole(new MessageUtil().setVariable("{0}", server).setVariable("{1}", reported).setVariable("{2}", sender).setVariable("{3}", reason).format(AxisConfiguration.getString("report")));
                break;
            }
            case "rank": {
                if (argument.equalsIgnoreCase("reload")) {
                    Rank.reloadCache();
                }
                break;
            }
            case "profile": {
                if (argument.equalsIgnoreCase("setrank")) {
                    Profile profile = AxisAPI.getProfile(UUID.fromString(event.getArguments()[2]));
                    Rank rank = AxisAPI.getRankByName(event.getArguments()[3]);
                    if (rank != null) {
                        profile.setRank(rank);
                        profile.save();
                        profile.updateProfile();

                        profile.updateTab();

                        Player player = profile.getPlayer();
                        if (AxisUtils.isOnline(player)) {
                            new PlayerRankChangedEvent(player, rank).call();
                        }
                    }
                } else if (argument.equalsIgnoreCase("reload")) {
                    if (event.getArguments()[2].equalsIgnoreCase("all")) {
                        for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                            Profile profile = AxisAPI.getProfile(online);

                            profile.updateProfile();
                        }
                    } else if (event.getArguments()[2].equalsIgnoreCase("uuid")) {
                        Profile profile = AxisAPI.getProfile(UUID.fromString(event.getArguments()[3]));
                        profile.updateProfile();
                    }
                }
                break;
            }
            case "punishment": {
                UUID uuid = UUID.fromString(argument);
                sender = event.getArguments()[2];
                String target = event.getArguments()[3];
                Punishment punishment = new Punishment();

                punishment.setUuid(uuid);
                PunishmentHelper.load((Document) AxisPlugin.getPlugin().getAxisDatabase().getPunishments().find(Filters.eq("uuid", uuid.toString())).first(), punishment);
                punishment.send(target, sender);

                Profile profile = AxisAPI.getProfile(punishment.getTargetID());
                Player player = profile.getPlayer();

                profile.getPunishments().removeIf(check -> check.getUuid().equals(punishment.getUuid()));

                if (punishment.getType() == PunishmentType.BLACKLIST) {
                    for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                        if (online.getAddress().getAddress().getHostAddress().equalsIgnoreCase(profile.getAddress())) {
                            TaskUtil.runTask(() -> online.kickPlayer(ColorText.translate(PunishmentType.BLACKLIST.getMessage())));
                        }
                    }
                    return;
                }

                profile.getPunishments().add(punishment);

                if (AxisUtils.isOnline(player)) {

                    if (punishment.getType() == PunishmentType.KICK || punishment.getType() == PunishmentType.WARN) {
                        return;
                    }

                    if (profile.getBannedPunishment() != null) {
                        String kickMessage = new MessageUtil().setVariable("{expires}", punishment.getRemaining()).format(punishment.getType().getMessage());

                        profile.setFrozen(false);

                        TaskUtil.runTask(() -> player.kickPlayer(kickMessage));

                    } else if (profile.getMutedPunishment() != null) {
                        player.sendMessage(ColorText.translate("&cYou have been muted."));
                        if (!profile.getMutedPunishment().isPermanent()) {
                            player.sendMessage(ColorText.translate("&cTime left: " + profile.getMutedPunishment().getRemaining()));
                        }
                    }

                }
                break;
            }
            case "cosmetics": {
                if (argument.equalsIgnoreCase("update")) {
                    Tag.getTags().clear();
                    Tag.loadTags();
                } else if (argument.equalsIgnoreCase("delete")) {
                    Tag tag = AxisAPI.getTagByName(event.getArguments()[2]);
                    if (tag != null) {
                        for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                            Profile profile = AxisAPI.getProfile(online);
                            if (profile.getTag() != null && profile.getTag().equals(tag)) {
                                profile.setTag(null);
                                profile.updateProfile();
                            }
                        }
                        tag.remove();
                    }
                }
                break;
            }
            case "whitelist": {
                server = event.getArguments()[2];
                if (argument.equalsIgnoreCase("running")) {
                    String duration = event.getArguments()[3];
                    Bukkit.broadcastMessage(new MessageUtil().setVariable("{0}", server).setVariable("{1}", duration).format(AxisConfiguration.getString("whitelist_running")));
                } else if (argument.equalsIgnoreCase("unwhitelisted")) {
                    boolean isJoin = AxisConfiguration.getBoolean("join_command");
                    ChatUtil chatUtil = new ChatUtil(new MessageUtil().setVariable("{0}", server).setVariable("{1}", server.toLowerCase()).format(AxisConfiguration.getString("whitelist_unwhitelisted")), (isJoin ? AxisConfiguration.getString("whitelist_unwhitelisted_tooltip") : null), (isJoin ? "/join " + server.toLowerCase() : null));
                    for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                        chatUtil.send(online);
                    }
                }
                break;
            }
            case "freeze": {
                server = event.getArguments()[2];
                sender = event.getArguments()[3];
                if (argument.equalsIgnoreCase("add")) {
                    AxisUtils.getOnlineStaff().forEach(player -> player.sendMessage(new MessageUtil().setVariable("{0}", server).setVariable("{1}", sender).setVariable("{2}", event.getArguments()[4]).format(AxisConfiguration.getString("target_frozen_alert"))));
                    AxisAPI.sendToConsole(new MessageUtil().setVariable("{0}", server).setVariable("{1}", sender).setVariable("{2}", event.getArguments()[4]).format(AxisConfiguration.getString("target_frozen_alert")));
                } else if (argument.equalsIgnoreCase("remove")) {
                    AxisUtils.getOnlineStaff().forEach(player -> player.sendMessage(new MessageUtil().setVariable("{0}", server).setVariable("{1}", sender).setVariable("{2}", event.getArguments()[4]).format(AxisConfiguration.getString("target_unfrozen_alert"))));
                    AxisAPI.sendToConsole(new MessageUtil().setVariable("{0}", server).setVariable("{1}", sender).setVariable("{2}", event.getArguments()[4]).format(AxisConfiguration.getString("target_unfrozen_alert")));
                } else if (argument.equalsIgnoreCase("joined")) {
                    AxisUtils.getOnlineStaff().forEach(player -> player.sendMessage(new MessageUtil().setVariable("{0}", server).setVariable("{1}", sender).format(AxisConfiguration.getString("target_frozen_joined_teamspeak"))));
                    AxisAPI.sendToConsole(new MessageUtil().setVariable("{0}", server).setVariable("{1}", sender).format(AxisConfiguration.getString("target_frozen_joined_teamspeak")));
                } else {
                    AxisUtils.getOnlineStaff().forEach(player -> player.sendMessage(new MessageUtil().setVariable("{0}", server).setVariable("{1}", sender).format(AxisConfiguration.getString("target_left_frozen"))));
                    AxisAPI.sendToConsole(new MessageUtil().setVariable("{0}", server).setVariable("{1}", sender).format(AxisConfiguration.getString("target_left_frozen")));
                }
                break;
            }
        }
    }
}