package main.bot;


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.regex.Pattern;

public class Translator {

    public Translator() {

    }

    private static String getLanguage(String message) {
        String[] splitted = message.split(" ");

        boolean EU = true;
        for (String ss : splitted) {
            if (!Pattern.matches("[0-9a-zA-Z]+", ss)) {
                EU = false;
            }
        }
        if (EU)
            return "en-ru";
        else return "ru-en";
    }


    static String YandexTranslate(String enteredText) throws IOException {

        String textEscaped = enteredText.replace(" ", "%20");
        String url = "https://translate.yandex.net/api/v1.5/tr.json/translate?key=&lang=" // key
                + getLanguage(enteredText) + "&text=" + textEscaped;
        URLConnection connection = null;
        try {
            connection = new URL(url).openConnection();
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.printf("No text for translate");
        }
        InputStream response;
        if (connection != null) {
            response = connection.getInputStream();

            final String[] json = new String[1];

            final Runnable stuffToDo = new Thread(() -> json[0] = new Scanner(response).nextLine());

            final ExecutorService executor = Executors.newSingleThreadExecutor();
            final Future future = executor.submit(stuffToDo);
            executor.shutdown();

            try {
                future.get(10, TimeUnit.SECONDS);
            } catch (InterruptedException ie) {
            } catch (ExecutionException ee) {
            } catch (TimeoutException te) {
                return "Yandex Timeout";
            }
            if (!executor.isTerminated())
                executor.shutdownNow();

            int start = json[0].indexOf("[");
            int end = json[0].indexOf("]");
            String translated = json[0].substring(start + 2, end - 1);
            System.out.println(json[0]);
            if (enteredText.matches(translated)) { // todo: fix fucked up return of en>ru (json is fine)
                return "Слишком длинное выражение или ошибка";
            }
            return "Yandex: " + enteredText + " - " + translated;

        }
        return "Can't connect to Yandex";

    }
}
