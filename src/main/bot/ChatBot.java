package main.bot;

import main.Exceptions.HardResetException;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

class ChatBot {

    private final ArrayList<String> Quotes = new ArrayList() {{
        add("In search of a main class manifest since 2013");
        add("Existential crisis as a lifestyle");
        add("Every day of work on this bot passes and jokes about suicide become less and less funnier");
        add("Sometimes I think Synyster Gates needs some serious mental help. No sane human can play like that");

    }};
    private final Map<String, String> ANSWERS_BY_PATTERNS = new HashMap<String, String>() {{
        startDate = new Date().toString();
        put("SITE_KEY", "https://leff.su/");
        put("MAIL_KEY", "leff@leff.su");
        put("TELEGRAM_KEY", "https://t.me/theleff");
        put("CONTACT_KEY", "https://leff.su/ \r\n t.me/theleff \r\n leff@leff.su");

//        put("HELP_KEY", "I am a Bot Rewinder, which can help you with various stuff. Here is the list of my commands: "); //todo: list

    }};
    private AttachmentProcessor ATTACH;
    private CommandProcessor COMMAND;

    static String startDate;
    private Translator TRANSLATOR;
    private final Map<String, String> PATTERNS_FOR_ANALYSIS = new HashMap<String, String>() {{

        put("contact", "CONTACT_KEY");
        put("контакт", "CONTACT_KEY");

        put("telegram", "TELEGRAM_KEY");
        put("телеграм", "TELEGRAM_KEY");

        put("mail", "MAIL_KEY");
        put("почта", "MAIL_KEY");

        put("site", "SITE_KEY");
        put("сайт", "SITE_KEY");

    }};
    private Random r = new Random();

    ChatBot() {
        this.ATTACH = new AttachmentProcessor();
        this.COMMAND = new CommandProcessor();
        this.TRANSLATOR = new Translator();
    }

    private String quoteGenerator() {
        return Quotes.get(r.nextInt(Quotes.size()));
    }

    AttachmentProcessor getATTACH() {
        return ATTACH;
    }

    String sayInReturn(String msg) throws HardResetException, IOException {

        String message = String.join(" ", msg.toLowerCase().split("[ {,|.}?]+"));

        if (msg.startsWith("!")) {
            return COMMAND.command(msg);
        } else
            for (Map.Entry<String, String> o : PATTERNS_FOR_ANALYSIS.entrySet()) {
                Pattern pattern = Pattern.compile(o.getKey());

                if (pattern.matcher(message).find()) {
                    return ANSWERS_BY_PATTERNS.get(o.getValue());
                } else if (message.equalsIgnoreCase("quote") || message.equalsIgnoreCase("цитата")) {
                    return quoteGenerator();
                }
            }
        return TRANSLATOR.YandexTranslate(message);
    }
}
