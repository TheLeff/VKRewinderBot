package main.bot;


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.regex.Pattern;

class Translator extends Processor {

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
                } catch (InterruptedException | ExecutionException ignored) {
                } catch (TimeoutException e) {
                    return "Yandex Timeout";
                }
                if (!executor.isTerminated())
                    executor.shutdownNow();
                return json[0];


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

            }
//            return "Can't connect to Yandex";

        } catch (IOException e) {
            System.out.println(e.toString());
            return "And what do you want me to do with it?";
        }
        return null;
    }

    Translator() {

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
                } catch (InterruptedException | ExecutionException ignored) {
                } catch (TimeoutException e) {
                    return "Yandex Timeout";
                }
                if (!executor.isTerminated())
                    executor.shutdownNow();

                int start = json[0].indexOf("[");
                int end = json[0].indexOf("]");
                String translated = json[0].substring(start + 2, end - 1);
                System.out.println(json[0]);
                if (enteredText.matches(translated)) {
                    History.add("ERROR - " + enteredText);
                    return "Слишком длинное выражение или ошибка";
                }

                History.add(enteredText + " - " + translated);
                return "Yandex: " + enteredText + " - " + translated;

            }
            return "Can't connect to Yandex";

        } catch (IOException e) {
            System.out.println(e.toString());
            return "And what do you want me to do with it?";
        }
    }

    class YandexTranslation {
        public int code;
        public String lang;
        public String text;
    }

    @Override
    String getHistory() {
        return null;
    }
}
