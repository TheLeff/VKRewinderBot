package main.bot;

import main.Exceptions.HardResetException;

import java.io.FileOutputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

class ChatBot {

    private static long uptime;
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
    //    final String[] COMMON_PHRASES = {
//            "Нет ничего ценнее слов, сказанных к месту и ко времени.",
//            "Порой молчание может сказать больше, нежели уйма слов.",
//            "Перед тем как писать/говорить всегда лучше подумать.",
//            "Вежливая и грамотная речь говорит о величии души.",
//            "Приятно когда текст без орфографических ошибок.",
//            "Многословие есть признак неупорядоченного ума.",
//            "Слова могут ранить, но могут и исцелять.",
//            "Записывая слова, мы удваиваем их силу.",
//            "Кто ясно мыслит, тот ясно излагает.",
//            "Боюсь Вы что-то не договариваете."};
//    final String[] ELUSIVE_ANSWERS = {
//            "Вопрос непростой, прошу тайм-аут на раздумья.",
//            "Не уверен, что располагаю такой информацией.",
//            "Может лучше поговорим о чём-то другом?",
//            "Простите, но это очень личный вопрос.",
//            "Не уверен, что Вам понравится ответ.",
//            "Поверьте, я сам хотел бы это знать.",
//            "Вы действительно хотите это знать?",
//            "Уверен, Вы уже догадались сами.",
//            "Зачем Вам такая информация?",
//            "Давайте сохраним интригу?"};
    private final Map<String, String> ANSWERS_BY_PATTERNS = new HashMap<String, String>() {{
        put("BLACKLIST_KEY", "Кто матерится - тот уебок");
        put("HELP_KEY", "I am a Bot Rewinder, which can help you with various stuff. Here is the list of my commands: "); //todo: list
        put("UP_KEY", "Uptime is " + ManagementFactory.getRuntimeMXBean().getUptime()); //todo: fix it
        put("TIME_KEY", "It's " + new Date().toString());
        put("TEST_KEY", readWiki());

    }};
    FileOutputStream fs;
    RuntimeMXBean rb = ManagementFactory.getRuntimeMXBean();
    //    Random random; // for random answers

    ChatBot() {
//        random = new Random();
    }

    private String readWiki() {
        //Wiki wiki = new Wiki("en.wikipedia.org");


        return null;
    }

    String sayInReturn(String msg) throws HardResetException {


//       String say = (msg.trim().endsWith("?"))?
//               ELUSIVE_ANSWERS[random.nextInt(ELUSIVE_ANSWERS.length)]:
//               COMMON_PHRASES[random.nextInt(COMMON_PHRASES.length)];

        String message = String.join(" ", msg.toLowerCase().split("[ {,|.}?]+"));

        for (Map.Entry<String, String> o : PATTERNS_FOR_ANALYSIS.entrySet()) {
            Pattern pattern = Pattern.compile(o.getKey());

            if (pattern.matcher(message).find()) {
                return ANSWERS_BY_PATTERNS.get(o.getValue());
            }

            if (message.contains("hardresetplox")) {
                throw new HardResetException("BOT WAS RESET BY ADMIN IN CHAT");
            }
        }

         return "Can't understand, use " + '"' + "help" + '"' + " command";
    }
}
