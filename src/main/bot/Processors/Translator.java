package main.bot.Processors;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import main.bot.Misc;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Translator extends Processor {

    public String translate(int userId, String enteredText) {
        return "OOPS";
    }

    public class Yandex extends Translator {

        @Override
        public String translate(int userId, String enteredText) {

            try {
                String textEscaped = enteredText.replace(" ", "%20");
                String url = "https://translate.yandex.net/api/v1.5/tr.json/translate?key=trnsl.1.1.20170921T152134Z.93cdd37345a03a7f.3c0d40e83517a07da4922a1e2bf3902f6012822c&lang=" // key
                        + getLanguage(enteredText) + "&text=" + textEscaped;

                URLConnection connection = null;

                try {
                    connection = new URL(url).openConnection();
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.printf("No text for translate");
                }

                if (connection != null) {

                    Gson gson = new GsonBuilder().create();
                    YandexBaseTranslation yt = gson.fromJson(new Scanner(connection.getInputStream()).nextLine(), YandexBaseTranslation.class);
                    String result = yt.text.toString().replaceAll("[\\[\\](){}]", "");

                    if (result.matches(enteredText)) {
                        History.add("ERROR - " + enteredText);
                        return "Слишком длинное выражение или ошибка";
                    }
                    if (!History.isEmpty())
                        History.add(System.lineSeparator() + Misc.getUserName(userId) + " [Yandex: " + enteredText + " - " + result + ']');
                    else
                        History.add((Misc.getUserName(userId) + " [Yandex: " + enteredText + " - " + result + ']').substring(1));
                    return "Yandex: " + enteredText + " - " + result;
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                return "What do you want me to do with it?";
            }
            return null;
        }

        public class Extended extends Translator {
            @Override
            public String translate(int userId, String enteredText) {
                try {
                    String textEscaped = enteredText.replace(" ", "%20");
                    String url = "https://dictionary.yandex.net/api/v1/dicservice.json/lookup?key=dict.1.1.20171021T155608Z.623dc548e790e279.d38cc13bbc2cb9e168bbed6d953e74595d6991b8&lang=" // key
                            + getLanguage(enteredText) + "&text=" + textEscaped;
                    URLConnection connection = null;
                    try {
                        connection = new URL(url).openConnection();
                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.out.printf("No text for translate");
                    }
                    if (connection != null) {
                        Gson gson = new GsonBuilder().create();
                        JsonObject js = (new JsonParser()).parse(new Scanner(connection.getInputStream()).nextLine()).getAsJsonObject();


//                https://dictionary.yandex.net/api/v1/dicservice.json/lookup?key=dict.1.1.20171021T155608Z.623dc548e790e279.d38cc13bbc2cb9e168bbed6d953e74595d6991b8&lang=en-ru&text=time
//                {"head":{},"def":[{"text":"time","pos":"noun","ts":"taɪm","tr":[{"text":"время","pos":"noun","gen":"ср","syn":[{"text":"раз","pos":"noun","gen":"м"},{"text":"момент","pos":"noun","gen":"м"},{"text":"срок","pos":"noun","gen":"м"},{"text":"пора","pos":"noun"},{"text":"период","pos":"noun","gen":"м"}],"mean":[{"text":"period"},{"text":"once"},{"text":"moment"},{"text":"pore"}],"ex":[{"text":"daylight saving time","tr":[{"text":"летнее время"}]},{"text":"take some time","tr":[{"text":"занять некоторое время"}]},{"text":"real time mode","tr":[{"text":"режим реального времени"}]},{"text":"expected arrival time","tr":[{"text":"ожидаемое время прибытия"}]},{"text":"external time source","tr":[{"text":"внешний источник времени"}]},{"text":"next time","tr":[{"text":"следующий раз"}]},{"text":"initial time","tr":[{"text":"начальный момент"}]},{"text":"reasonable time frame","tr":[{"text":"разумный срок"}]},{"text":"winter time","tr":[{"text":"зимняя пора"}]},{"text":"incubation time","tr":[{"text":"инкубационный период"}]}]},{"text":"час","pos":"noun","gen":"м","mean":[{"text":"hour"}],"ex":[{"text":"checkout time","tr":[{"text":"расчетный час"}]}]},{"text":"эпоха","pos":"noun","gen":"ж","mean":[{"text":"era"}]},{"text":"век","pos":"noun","gen":"м","mean":[{"text":"age"}]},{"text":"такт","pos":"noun","gen":"м","syn":[{"text":"темп","pos":"noun","gen":"м"}],"mean":[{"text":"cycle"},{"text":"rate"}]},{"text":"жизнь","pos":"noun","gen":"ж","mean":[{"text":"life"}]}]},{"text":"time","pos":"verb","ts":"taɪm","tr":[{"text":"приурочивать","pos":"verb","asp":"несов"},{"text":"рассчитывать","pos":"verb","asp":"несов","mean":[{"text":"count"}]}]},{"text":"time","pos":"adjective","ts":"taɪm","tr":[{"text":"временный","pos":"adjective","syn":[{"text":"временной","pos":"adjective"}],"mean":[{"text":"temporary"}],"ex":[{"text":"time series model","tr":[{"text":"модель временных рядов"}]},{"text":"time correlation function","tr":[{"text":"временная корреляционная функция"}]},{"text":"time code","tr":[{"text":"временной код"}]}]},{"text":"повременный","pos":"adjective","ex":[{"text":"time payment","tr":[{"text":"повременная оплата"}]}]}]}]}

                        //todo: parse this garbage

                    }
                } catch (IOException e) {
                    System.out.println(e.toString());
                    return "And what do you want me to do with it?";
                }
                return null;
            }
        }
    }

    private String getLanguage(String message) {
        String[] split = message.split(" ");

        boolean EU = true;
        for (String ss : split) {
            if (!Pattern.matches("[0-9a-zA-Z]+", ss)) {
                EU = false;
            }
        }
        if (EU)
            return "en-ru";
        else return "ru-en";
    }

    @Override
    String getHistory() {
        return null;
    }

    class YandexBaseTranslation {
        public int code;
        public String lang;
        ArrayList<String> text;
    }
}
