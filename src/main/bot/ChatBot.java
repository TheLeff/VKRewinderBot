package main.bot;

import main.Exceptions.HardResetException;
import main.bot.Processors.AttachmentProcessor;
import main.bot.Processors.CommandProcessor;
import main.bot.Processors.Translator;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

public class ChatBot {


//    List<String> incomingMessages;

    public static String startDate;
    private final ArrayList<String> Quotes = new ArrayList() {{
        add("In search of a main class manifest since 2013");
        add("Existential crisis as a lifestyle");
        add("Every day of work on this bot passes and jokes about suicide become less and less funnier");
        add("Sometimes I think Synyster Gates needs some serious mental help. No sane human can play like that");
        add("REEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE NORMIES");
    }};
    private final Map<String, String> ANSWERS_BY_PATTERNS = new HashMap<String, String>() {{
        startDate = new Date().toString();
        put("SITE_KEY", "https://leff.su/");
        put("MAIL_KEY", "leff@leff.su");
        put("TELEGRAM_KEY", "https://t.me/theleff");
        put("CONTACT_KEY", "https://leff.su/ \r\n t.me/theleff \r\n leff@leff.su");

//        put("HELP_KEY", "I am a Bot Rewinder, which can help you with various stuff. Here is the list of my commands: "); //todo: list

    }};
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
    private AttachmentProcessor ATTACH;
    private CommandProcessor COMMAND;
    private Translator TRANSLATOR;
    private Random r = new Random();

    ChatBot(int code) throws Exception {
        this.ATTACH = new AttachmentProcessor();
        this.COMMAND = new CommandProcessor();

        switch (code) {
            case 0:
                this.TRANSLATOR = new Translator().new Yandex();
                break;
            case 1:
                this.TRANSLATOR = new Translator().new Yandex().new Extended();
                break;
            default:
                throw new Exception("TRANSLATOR TYPE RECOGNITION ERROR");
        }

    }

    private String quoteGenerator() {
        return Quotes.get(r.nextInt(Quotes.size()));
    }

    AttachmentProcessor getATTACH() {
        return ATTACH;
    }

    String sayInReturn(int userId, String msg) throws HardResetException, IOException {

//        incomingMessages.add(msg);

        String message = String.join(" ", msg.toLowerCase().split("[ {,|.}?]+"));

        if (msg.startsWith("!")) {
            return COMMAND.command(userId, msg);
        } else
            for (Map.Entry<String, String> o : PATTERNS_FOR_ANALYSIS.entrySet()) {
                Pattern pattern = Pattern.compile(o.getKey());

                if (pattern.matcher(message).find()) {
                    return ANSWERS_BY_PATTERNS.get(o.getValue());
                } else if (message.equalsIgnoreCase("quote") || message.equalsIgnoreCase("цитата")) {
                    return quoteGenerator();
                }
            }
        return TRANSLATOR.translate(userId, message);
    }
}
