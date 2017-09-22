package main.bot;


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Pattern;

public class Translator {

    public Translator() {

    }

    private static String getLanguage(String message) {
        if (Pattern.matches("[a-zA-Z]+", message)) {
            return "ru";
        }
        return "en";
    }


//    ExecutorService executor = Executors.newCachedThreadPool(); //todo: put translations into the executor, timeout = 10 seconds
//    Callable<Object> task = new Callable<Object>() {
//        public Object call() {
//            return something.blockingMethod();
//        }
//    };
//    Future<Object> future = executor.submit(task);
//try {
//        Object result = future.get(5, TimeUnit.SECONDS);
//    } catch (TimeoutException ex) {
//        // handle the timeout
//    } catch (InterruptedException e) {
//        // handle the interrupts
//    } catch (ExecutionException e) {
//        // handle other exceptions
//    } finally {
//        future.cancel(true); // may or may not desire this
//    }


    static String YandexTranslate(String enteredText) throws IOException {
        //todo: Timeout
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

            String json = new java.util.Scanner(response).nextLine();
            int start = json.indexOf("[");
            int end = json.indexOf("]");
            String translated = json.substring(start + 2, end - 1);
            if (enteredText.matches(translated)) { // todo: fix fucked up return of en>ru (json is fine)
                return "Слишком длинное выражение или ошибка";
            }
            return "Yandex: " + enteredText + " - " + translated;

//        if(timeout) try {
//            throw new YandexTimeoutException("Yandex Timeout");
//        } catch (YandexTimeoutException e) {
//            return "Yandex Timeout";
//        }
        }
        return "Can't connect to Yandex";
    }

    static String GoogleTranslate(String lang, String enteredText) throws IOException {

        return null;


//        if(timeout) try {
//            throw new GoogleTimeoutException("Google Timeout");
//        } catch (GoogleTimeoutException e) {
//            return "Google Timeout";
//        }
    }

}
