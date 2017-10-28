package main.bot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Scanner;

public class Misc {

    public static String getUserName(int userId) {
        String url = "https://api.vk.com/method/users.get?user_id=" + userId;
        URLConnection connection = null;

        try {
            connection = new URL(url).openConnection();
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.printf("No text for translate");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ParseUser yt = null;

        if (connection != null) {

            Gson gson = new GsonBuilder().create();
            try {
                yt = gson.fromJson(new Scanner(connection.getInputStream()).nextLine(), ParseUser.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        {"response":[{"uid":275752427,"first_name":"Нильс","last_name":"Линд"}]}
//        try {
//            System.out.println(new Scanner(connection.getInputStream()).nextLine());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        return '[' + yt.response.toString().replaceAll("[\\[\\](){}]", "") + ']';
    }

    public class ParseUser {
        public ArrayList<UserInfo> response;
    }

    class UserInfo {
        int uid;
        String first_name;
        String last_name;

        @Override
        public String toString() {
            return '[' + uid + "] - " + first_name + " " + last_name;
        }
    }

}
