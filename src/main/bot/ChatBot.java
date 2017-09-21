package main.bot;

import main.Exceptions.HardResetException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
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

        put("help", "HELP_KEY");
        put("time", "TIME_KEY");
        put("up", "UP_KEY");
        put("today", "TODAY_KEY");
        put("test", "TEST_KEY");
    }};

    private final Map<String, String> ANSWERS_BY_PATTERNS = new HashMap<String, String>() {{
        startDate = new Date().toString();
        put("BLACKLIST_KEY", "Кто матерится - тот уебок");
        put("HELP_KEY", "I am a Bot Rewinder, which can help you with various stuff. Here is the list of my commands: "); //todo: list

    }};


    ChatBot() {
    }

    private static String translate(String lang, String enteredText) throws IOException {
        String textEscaped = enteredText.replace(" ", "%20");
        String url = "https://translate.yandex.net/api/v1.5/tr.json/translate?key=&lang=" // key
                + lang + "&text=" + textEscaped;
        URLConnection connection = null;
        try {
            connection = new URL(url).openConnection();
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.printf("No text for translate");
        }
        InputStream response = connection.getInputStream();
        String json = new java.util.Scanner(response).nextLine();
        int start = json.indexOf("[");
        int end = json.indexOf("]");
        String translated = json.substring(start + 2, end - 1);
        if (enteredText.matches(translated)) {
            return "Слишком длинное выражение";
        }
        return translated;
    }

    private String readWiki() {
        //Wiki wiki = new Wiki("en.wikipedia.org");

        return null;
    }

    private String calculateUptime() {
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - Solution.startTime;
        if (totalTime <= 60000)
            return totalTime / 1000 + " seconds";
        else if (totalTime <= 3600000)
            return totalTime / 3600000 + " minutes " + totalTime % 3600000 / 60000 + " seconds";
        else if (totalTime <= 216000000)
            return totalTime / 216000000 + " hours " + totalTime % 3600000 + " minutes " + totalTime % 60000 + " seconds";
        return null;
    }

    private String getLanguage(String message) {
        if (Pattern.matches("[a-zA-Z]+", message)) {
            return "ru";
        }
        return "en";
    }

    String sayInReturn(String msg) throws HardResetException, IOException {

        String message = String.join(" ", msg.toLowerCase().split("[ {,|.}?]+"));

        for (Map.Entry<String, String> o : PATTERNS_FOR_ANALYSIS.entrySet()) {
            Pattern pattern = Pattern.compile(o.getKey());


            if (message.contains("time")) {
                return new Date().toString();
            }

            if (message.contains("up")) {
                return "Uptime is " + calculateUptime() + ", started on " + startDate;
            }

            if (message.contains("hardresetplox")) {
                throw new HardResetException("BOT WAS RESET BY ADMIN IN CHAT");
            }

            if (pattern.matcher(message).find()) {
                return ANSWERS_BY_PATTERNS.get(o.getValue());
            }

        }

        return translate(getLanguage(message), message);
    }
}
