package me.tinchx.axis.chat;

import lombok.Getter;
import lombok.Setter;
import me.tinchx.axis.AxisConfiguration;
import me.tinchx.axis.utilities.chat.MessageUtil;
import me.tinchx.axis.utilities.time.TimeUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatManager {

    private static Pattern URL_REGEX = Pattern.compile("^(http://www\\.|https://www\\.|http://|https://)?[a-z0-9]+([\\-.][a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(/.*)?$");
    private static Pattern IP_REGEX = Pattern.compile("^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])([.,])){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$");
    @Getter
    @Setter
    private static long chatDelay = TimeUtils.parse("5s");
    @Getter
    @Setter
    private static boolean publicChatAllowed = true;

    public static boolean canBeFiltered(String message) {
        String msg = new MessageUtil().setVariable("3", "e").setVariable("1", "i").setVariable("!", "i")
                .setVariable("@", "a").setVariable("7", "t").setVariable("0", "o")
                .setVariable("4", "a")
                .setVariable("5", "s").setVariable("8", "b").setVariable("\\p{Punct}|\\d", "").setVariable(" ", "").format(message);

        String[] words = msg.trim().split(" ");

        for(String word : words) {
            for(String filteredWord : AxisConfiguration.getArray("filtered_words")) {
                if(word.contains(filteredWord)) return true;
            }
        }

        for (String word : msg.replace("(dot)", ".").replace("[dot]", ".").replace("(.)", ".").replace("./", ".").trim().split(" ")) {
            boolean breakIt = false;

            for (String phrase : AxisConfiguration.getArray("allowed_links")) {
                if (word.toLowerCase().contains(phrase)) {
                    breakIt = true;
                    break;
                }
            }

            if (breakIt) {
                continue;
            }

            Matcher matcher = ChatManager.IP_REGEX.matcher(word);

            if (matcher.matches()) {
                return true;
            }

            matcher = ChatManager.URL_REGEX.matcher(word);

            if (matcher.matches()) {
                return true;
            }
        }

        return false;
    }
}
