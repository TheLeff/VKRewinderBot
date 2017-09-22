package main.bot;

import main.Exceptions.HardResetException;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

class ChatBot {

    private static String startDate;

    private final Map<String, String> PATTERNS_FOR_ANALYSIS = new HashMap<String, String>() {{

        put("ебать", "BLACKLIST_KEY");
        put("хуй", "BLACKLIST_KEY");
        put("сука", "BLACKLIST_KEY");
        put("блять", "BLACKLIST_KEY");
        put("пидор", "BLACKLIST_KEY");
        put("пидр", "BLACKLIST_KEY");
        put("уебок", "BLACKLIST_KEY");
        put("уебище", "BLACKLIST_KEY");
        put("хуя", "BLACKLIST_KEY");

        put("fuck", "BLACKLIST_KEYEN");
        put("bitch", "BLACKLIST_KEYEN");
        put("retard", "BLACKLIST_KEYEN");
        put("dumb", "BLACKLIST_KEYEN");
        put("dick", "BLACKLIST_KEYEN");
        put("cock", "BLACKLIST_KEYEN");
        put("twat", "BLACKLIST_KEYEN");
        put("shit", "BLACKLIST_KEYEN");

        put("contact", "CONTACT_KEY");
        put("контакт", "CONTACT_KEY");


        put("telegram", "TELEGRAM_KEY");
        put("телеграм", "TELEGRAM_KEY");

        put("mail", "MAIL_KEY");
        put("почта", "MAIL_KEY");

        put("site", "SITE_KEY");
        put("сайт", "SITE_KEY");


//        put("help", "HELP_KEY");
//        put("time", "TIME_KEY");
//        put("up", "UP_KEY");
//        put("today", "TODAY_KEY");
//        put("test", "TEST_KEY");
    }};
    private final Map<String, String> ANSWERS_BY_PATTERNS = new HashMap<String, String>() {{
        startDate = new Date().toString();
        put("BLACKLIST_KEY", "Не пиши мне");
        put("BLACKLIST_KEYEN", "Don't talk to me");
        put("SITE_KEY", "https://leff.su/");
        put("MAIL_KEY", "leff@leff.su");
        put("TELEGRAM_KEY", "https://t.me/leffsu");
        put("CONTACT_KEY", "https://leff.su/ \r\n t.me/leffsu \r\n leff@leff.su");
//        put("HELP_KEY", "I am a Bot Rewinder, which can help you with various stuff. Here is the list of my commands: "); //todo: list

    }};

    ChatBot() {
    }


    private String calculateUptime() {
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - Solution.startTime;
            return totalTime / 1000 + " seconds";
    }

    String sayInReturn(String msg) throws HardResetException, IOException {

        String message = String.join(" ", msg.toLowerCase().split("[ {,|.}?]+"));

        for (Map.Entry<String, String> o : PATTERNS_FOR_ANALYSIS.entrySet()) {
            Pattern pattern = Pattern.compile(o.getKey());

            if (pattern.matcher(message).find()) {
                return ANSWERS_BY_PATTERNS.get(o.getValue());
            } else if (message.equalsIgnoreCase("time")) {
                return new Date().toString();
            } else if (message.equalsIgnoreCase("up")) {
                return "Uptime is " + calculateUptime() + ", started on " + startDate;
            } else if (message.equalsIgnoreCase("hardresetplox")) {
                throw new HardResetException("BOT WAS RESET BY ADMIN IN CHAT");
            }

        }

        return Translator.YandexTranslate(message);
    }
}
