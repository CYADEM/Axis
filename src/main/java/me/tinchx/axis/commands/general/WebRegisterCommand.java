package me.tinchx.axis.commands.general;

import com.google.gson.Gson;
import lombok.Getter;
import me.tinchx.axis.AxisAPI;
import me.tinchx.axis.AxisConfiguration;
import me.tinchx.axis.utilities.AxisUtils;
import me.tinchx.axis.utilities.chat.ChatUtil;
import me.tinchx.axis.utilities.chat.ColorText;
import me.tinchx.axis.utilities.chat.MessageUtil;
import me.tinchx.axis.utilities.command.AxisCommand;
import org.apache.commons.io.IOUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class WebRegisterCommand extends AxisCommand {

    public WebRegisterCommand() {
        super("webregister");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(AxisUtils.ONLY_PLAYERS);
            return false;
        }
        Player player = (Player) sender;
        if (args.length < 1) {
            player.sendMessage(ColorText.translate("&cUsage: /" + label + " <email>"));
            return false;
        }
        String email = args[0];
        if (!email.endsWith(".com")) {
            player.sendMessage(ColorText.translate("&cInvalid email!"));
            return false;
        }
        AxisAPI.getProfile(player).setEmail(email);
        post(player, AxisConfiguration.getString("web_direct_url"), "{ \"uuid\" : \"" + player.getUniqueId() + "\", \"email\": \"" + email + "\"}");
        return true;
    }

    private void post(Player player, String completeUrl, String body) {
        try {
            URL obj = new URL(completeUrl);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setUseCaches(false);
            con.setConnectTimeout(5000);
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestMethod("POST");

            OutputStream os = con.getOutputStream();
            os.write(body.getBytes(StandardCharsets.UTF_8));
            os.close();

            InputStream in = new BufferedInputStream(con.getInputStream());
            String result = IOUtils.toString(in, "UTF-8");
            Gson gson = new Gson();

            System.out.println(player.getName() + " | " + result);

            PasswordResponse password = gson.fromJson(result, PasswordResponse.class);

            in.close();
            con.disconnect();

            switch (password.getError()) {
                case "0": {
                    player.sendMessage(ColorText.translate("&cCould not connect the API."));
                    return;
                }
                case "1": {
                    player.sendMessage(AxisConfiguration.getString("teamspeak_player_already_registered"));
                    return;
                }
                case "2": {
                    player.sendMessage(ColorText.translate("&cEmail in use."));
                    return;
                }
            }
            new ChatUtil("").copy(new MessageUtil().setVariable("{0}", password.getPassword()).format(AxisConfiguration.getString("web_player_registered")), "&7Click to copy", password.getPassword()).send(player);

        } catch (IOException ignored) {
            ignored.printStackTrace();
            player.sendMessage(ColorText.translate("&cCould not connect the API."));
        }
    }

    private class PasswordResponse {
        @Getter
        private String password, error;
    }
}