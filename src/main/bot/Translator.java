package main.bot;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

class Translator extends Processor {

    Translator() {

    }

    String YandexTest(String enteredText) {
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

    String YandexTranslate(String enteredText) {

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

//            InputStream response;

            if (connection != null) {

                Gson gson = new GsonBuilder().create();
                YandexBaseTranslation yt = gson.fromJson(new Scanner(connection.getInputStream()).nextLine(), YandexBaseTranslation.class);
                String result = yt.text.toString().replaceAll("[\\[\\](){}]", "");

                if (result.matches(enteredText)) {
                    History.add("ERROR - " + enteredText);
                    return "Слишком длинное выражение или ошибка";
                }

                History.add(enteredText + " - " + result);
                return "Yandex: " + enteredText + " - " + result;
            }
//                response = connection.getInputStream();
//
//                final String[] json = new String[1];
//
//                final Runnable stuffToDo = new Thread(() -> json[0] = new Scanner(response).nextLine());
//
//                final ExecutorService executor = Executors.newSingleThreadExecutor();
//                final Future future = executor.submit(stuffToDo);
//                executor.shutdown();
//
//                try {
//                    future.get(10, TimeUnit.SECONDS);
//                } catch (InterruptedException | ExecutionException ignored) {
//                } catch (TimeoutException e) {
//                    return "Yandex Timeout";
//                }
//                if (!executor.isTerminated())
//                    executor.shutdownNow();
//
//                int start = json[0].indexOf("[");
//                int end = json[0].indexOf("]");
//                String translated = json[0].substring(start + 2, end - 1);
//                System.out.println(json[0]);
//                if (enteredText.matches(translated)) {
//                    History.add("ERROR - " + enteredText);
//                    return "Слишком длинное выражение или ошибка";
//                }
//
//                History.add(enteredText + " - " + translated);
//                return "Yandex: " + enteredText + " - " + translated;
//
//            }
//            return "Can't connect to Yandex";
//
//        } catch (IOException e) {
//            System.out.println(e.toString());
//            return "And what do you want me to do with it?";
//        }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            return "What do you want me to do with it?";
        }
        return null;
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
