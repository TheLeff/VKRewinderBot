package main.bot;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.messages.Message;
import main.Exceptions.HardResetException;
import main.Exceptions.ManualRemoteReset;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import static java.lang.System.exit;



public class Solution {

    private static List<Calendar> FullCalendar = new ArrayList<>();
    private static SimpleDateFormat sdf = new SimpleDateFormat("hh-mm");

    public static void main(String[] args) throws Exception {
        ChatBot chatBot = new ChatBot();

        TransportClient transportClient = HttpTransportClient.getInstance();
        VkApiClient vk = new VkApiClient(transportClient);

        FileInputStream fileInputStream = new FileInputStream(new File("src/main/resources/config.properties"));
        Properties properties = new Properties();
        properties.load(fileInputStream);

        UserActor actor = new UserActor(Integer.parseInt(properties.getProperty("userId")), properties.getProperty("accessToken"));


        //todo: check calendar


        while (true) {
            try {
                AppCheck();
            } catch (ManualRemoteReset e) {
                System.out.println("BOT TURNED OFF MANUALLY VIA APP");
                exit(1);
            }
            if (!FullCalendar.isEmpty()) {
                Calendar RightNow = CalendarCheck();
                if (RightNow != null) {
                    vk.messages().send(actor).
                            userId(RightNow.getUserID()).message(RightNow.getNote()).execute();
                }
            }
            Thread.sleep(1000);
            List<Message> messageList = vk.messages().get(actor).execute().getItems();
            for (Message message : messageList) {
                if (!message.isReadState()) {
                    int userId = message.getUserId();
                    System.out.println(message.getBody());
                    try {
                        System.out.println(vk.messages().send(actor).
                                userId(userId).message(chatBot.sayInReturn(message.getBody())).execute());
                    } catch (HardResetException e) {
                        System.out.println("BOT TURNED OFF BY " + userId);
                        exit(1);
                    }

                }
            }
        }
    }

    private static void AppCheck() throws ManualRemoteReset {
        boolean RemoteStatus = true;


        //todo: controlling app

        if (!RemoteStatus)
            throw new ManualRemoteReset("BOT TURNED OFF REMOTELY");
    }

    private static Calendar CalendarCheck() {
        Date brb = new Date();

        for (int i = 0; i < FullCalendar.size(); i++) {
            if ((FullCalendar.get(i).getTime().getTime() - brb.getTime()) < 60000) {

                Calendar ToReturn = FullCalendar.get(i);
                FullCalendar.remove(i);
                return ToReturn;
            }
        }
        return null;
    }

}
